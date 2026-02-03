package com.nbtech.ailab.util;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbtech.ailab.biz.dao.GroupsDao;
import com.nbtech.ailab.biz.dao.GroupsPersonDao;
import com.nbtech.ailab.biz.dto.*;
import com.nbtech.ailab.biz.entity.*;
import com.nbtech.ailab.biz.service.IExperimentLinkService;
import com.nbtech.ailab.biz.service.IExperimentPlanService;
import com.nbtech.ailab.biz.service.IGroupsPersonService;
import com.nbtech.ailab.biz.service.IGroupsService;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.common.CompletedStatusEnum;
import com.nbtech.ailab.config.AesSecret;
import com.nbtech.ailab.excel.GroupPersonExcelTwo;
import com.nbtech.ailab.excel.GroupsPersonExcel;
import com.nbtech.ailab.excel.ImageWriteHandler;
import com.nbtech.ailab.excel.ModelExportCellHandle;
import com.nbtech.ailab.facade.UserFacade;
import com.nbtech.ailab.vo.AesKeyVo;
import com.nbtech.ailab.vo.GroupQcVo;
import com.nbtech.ailab.vo.SysUserVo;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.utils.ConvertUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

import com.google.zxing.WriterException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CommonUtil {
    @Autowired
    private UserFacade userFacade;

    @Autowired
    private IExperimentPlanService experimentPlanService;

    @Autowired
    private IExperimentLinkService experimentLinkService;

    @Autowired
    private IGroupsPersonService groupsPersonService;

    @Autowired
    private IGroupsService groupsService;

    @Autowired
    private GroupsPersonDao groupsPersonDao;

    @Autowired
    private GroupsDao groupsDao;

    private static final String keySecret = "{\"keyArr\":\"NfMiYYvOXdzRWgLJGTbJeA==\"}";

    /**
     * 实验网址
     */
    @Value("${experiment.address}")
    private String address;

    @Value("${experiment.address}")
    private String experimentAddress;

    /**
     * 创建实验组人群包
     *
     * @param groupsDto  实验组对象
     * @param needNum    需要生成人的数量
     * @param prefixName 用户名前缀
     * @param style      方式 当前是属于新增实验组人群包(0) 还是初次人群包(1)
     * @return
     * @throws Exception
     */
    public List<GroupsPersonDto> createGroupsPerson(GroupsDto groupsDto, Integer needNum, String prefixName,
                                                    Integer style) throws Exception {
        // 获取加密秘钥
        String aesString = groupsDao.getKeyString(groupsDto.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        AesKeyVo aesKeyVo = objectMapper.readValue(aesString, AesKeyVo.class);

        int havaCount = 0;
        if (style == 0) {// 不是初次添加
            havaCount = groupsDto.getGroupsPersonNumber() - needNum;
        }

        List<GroupsPersonDto> groupsPersons = new ArrayList<>();
        List<GroupsPersonEntity> groupsPersonEntities = new ArrayList<>();
        List<SysUserVo> sysUserVos = new ArrayList<>();
        while (needNum > 0) {
            needNum--;
            havaCount++;
            Thread.sleep(1L);
            String userName = prefixName + CodeUtil.getStringNum(havaCount);
            String password = CodeUtil.fixCode(8, System.currentTimeMillis() + "PASS" + groupsDto.getId());

            SysUserVo sysUserVo = new SysUserVo();
            sysUserVo.setUsername(userName);
            sysUserVo.setPassword(password);
            sysUserVo.setRealName(userName);
            sysUserVos.add(sysUserVo);

        }
        List<SysUserEntity> sysUserEntities = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(sysUserVos)) {
           sysUserEntities = userFacade.batchSave(sysUserVos);
        }
        if (CollectionUtil.isNotEmpty(sysUserEntities)) {
            for (SysUserEntity sysUserEntity : sysUserEntities) {
                // 密码加密
                String secret = AesSecret.addSecret(sysUserEntity.getOriginPassword(), aesKeyVo.getKeyArr());
                GroupsPersonEntity groupsPersonEntity = GroupsPersonEntity.builder()
                        .groupsName(groupsDto.getGroupsName())
                        .groupsId(groupsDto.getId())
                        // 保存加密密码
                        .password(secret)
                        .experimentName(groupsDto.getExperimentName())
                        .experimentId(groupsDto.getExperimentId())
                        .experimentStatus(CompletedStatusEnum.BEEND.getDesc())
                        .experimentCode(groupsDto.getExperimentCode())
                        .userId(sysUserEntity.getId())
                        .build();
                groupsPersonEntities.add(groupsPersonEntity);
                GroupsPersonDto groupsPersonDto = ConvertUtils.sourceToTarget(groupsPersonEntity, GroupsPersonDto.class);
                groupsPersonDto.setUserName(sysUserEntity.getUsername());
                // 获取当前实验组创建的所有账号 按照创建时间顺序获得
                groupsPersons.add(groupsPersonDto);
            }
            groupsPersonService.insertBatch(groupsPersonEntities, 1000);
        }
        return groupsPersons;
    }

    public String getJsessionId(String userName, String password) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", userName);
        jsonObject.put("password", password);
        String url = address + "/api/account/login";
        String json = jsonObject.toJSONString();
        String jsessionId = "";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = httpClient.execute(post)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Response: " + responseBody);

                // 获取 JSESSIONID
                Header[] cookies = response.getHeaders("Set-Cookie");
                for (Header cookie : cookies) {
                    if (cookie.getValue().startsWith("JSESSIONID=")) {
                        jsessionId = cookie.getValue().split(";")[0]; // 获取 JSESSIONID 值
                    }
                }
            }
        }

        return jsessionId;
    }

    /**
     * 设置导出数据头部参数
     *
     * @param groupsId
     * @param response
     * @param excel
     * @param clazz
     * @throws Exception
     */
    public void setExportHeadParam(Long groupsId, HttpServletResponse response, List<?> excel, Class<?> clazz)
            throws Exception {
        GroupsDto groupsDto = groupsService.get(groupsId);
        ExperimentPlanDto experimentPlanDto = experimentPlanService.get(groupsDto.getExperimentId());

        // 查询实验场景等头部数据
        ExperimentPlanEntity experimentPlanEntity = groupsDao.getExpermentPlan(groupsId);
        ModelExportCellHandle modelExportCellHandle = ConvertUtils.sourceToTarget(experimentPlanEntity,
                ModelExportCellHandle.class);
        modelExportCellHandle.setSheetName("sheet0");
        modelExportCellHandle.setWebsite(address + "/experiment");
        // excel名称
        String excelName = experimentPlanDto.getExperimentName() + "_" + groupsDto.getGroupsName() + "_Experiment Kit";

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(excelName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), clazz)
                .registerWriteHandler(new ImageWriteHandler())
                .registerWriteHandler(modelExportCellHandle)
                .useDefaultStyle(false)
                .relativeHeadRowIndex(7)
                .sheet("sheet0")
                .doWrite(excel);
    }

    /**
     * 导出实验链接
     *
     * @param groupsId
     * @param response
     */
    public void exportLink(Long groupsId, HttpServletResponse response) throws Exception {
        List<GroupPersonExcelTwo> excel = new ArrayList<>();

        // 获取当前实验组下的人群包
        List<GroupsPersonDto> groupsPersons = groupsPersonService.getByGroupId(groupsId);

        ObjectMapper objectMapper = new ObjectMapper();
        AesKeyVo aesKeyVo = objectMapper.readValue(keySecret, AesKeyVo.class);

        for (GroupsPersonDto groupsPerson : groupsPersons) {
            GroupPersonExcelTwo groupPersonExcelTwo = new GroupPersonExcelTwo();
            String userId = AesSecret.addSecret(groupsPerson.getUserId().toString(), aesKeyVo.getKeyArr());
            String linkName = experimentAddress + "/link-login?token=" + userId;

            // 生成二维码图片
            try {
                BufferedImage qrCodeImage = QRCodeExcelUtil.generateQRCode(linkName, 200, 200);
                // 将二维码图片转换为字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(qrCodeImage, "png", baos);
                byte[] imageBytes = baos.toByteArray();

                groupPersonExcelTwo.setQrCode(imageBytes);
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }

            groupPersonExcelTwo.setExperimentLink(linkName);
            excel.add(groupPersonExcelTwo);

        }

        setExportHeadParam(groupsId, response, excel, GroupPersonExcelTwo.class);

    }

    /**
     *
     */
    public List<GroupQcVo> getUserQcList(Long groupsId) throws Exception {
        List<GroupQcVo> groupQcVos = new ArrayList<>();
        GroupsDto groupsDto = groupsService.get(groupsId);

        ObjectMapper objectMapper = new ObjectMapper();
        AesKeyVo aesKeyVo = objectMapper.readValue(groupsDto.getSecret(), AesKeyVo.class);
        AesKeyVo useKeyVo = objectMapper.readValue(keySecret, AesKeyVo.class);
        // 获取当前实验组下的人群包
        List<GroupsPersonDto> groupsPersons = groupsPersonService.getByGroupId(groupsId);
        for (GroupsPersonDto groupsPerson : groupsPersons) {
            String userRealName = userFacade.getUserRealName(groupsPerson.getUserId());
            GroupQcVo groupQcVo = new GroupQcVo();
            groupQcVo.setUsername(userRealName);
            String password = AesSecret.cancel(groupsPerson.getPassword(), aesKeyVo.getKeyArr());
            groupQcVo.setPassword(password);

            String userId = AesSecret.addSecret(groupsPerson.getUserId().toString(), useKeyVo.getKeyArr());
            String linkName = experimentAddress + "/link-login?token=" + userId;

            // 生成二维码图片
            try {
                BufferedImage qrCodeImage = QRCodeExcelUtil.generateQRCode(linkName, 200, 200);
                // 将二维码图片转换为字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(qrCodeImage, "png", baos);
                byte[] imageBytes = baos.toByteArray();
                groupQcVo.setQcBase64("data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes));
                // 关闭字节流
                baos.close();
                groupQcVos.add(groupQcVo);
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }
        }
        return groupQcVos;

    }

}
