package com.nbtech.ailab.facade;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbtech.ailab.biz.dao.*;
import com.nbtech.ailab.biz.dto.ExperimentLinkDto;
import com.nbtech.ailab.biz.dto.ExperimentPlanDto;
import com.nbtech.ailab.biz.dto.GroupsDto;
import com.nbtech.ailab.biz.dto.GroupsPersonDto;
import com.nbtech.ailab.biz.entity.*;
import com.nbtech.ailab.biz.service.IExperimentLinkService;
import com.nbtech.ailab.biz.service.IExperimentPlanService;
import com.nbtech.ailab.biz.service.IGroupsService;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.common.ElementTypeEnum;
import com.nbtech.ailab.common.ExperimentPlanProtocolEnum;
import com.nbtech.ailab.common.RoomRoleTypeEnum;
import com.nbtech.ailab.config.AesSecret;
import com.nbtech.ailab.constant.ExperimentSceneConstant;
import com.nbtech.ailab.constant.ExperimentStyle;
import com.nbtech.ailab.excel.ExcelExportCellHandle;
import com.nbtech.ailab.excel.GroupPersonExcelTwo;
import com.nbtech.ailab.excel.GroupsPersonExcel;
import com.nbtech.ailab.excel.ImageWriteHandler;
import com.nbtech.ailab.excel.ModelExportCellHandle;
import com.nbtech.ailab.util.CommonUtil;
import com.nbtech.ailab.vo.AesKeyVo;
import com.nbtech.ailab.vo.ElementVo;
import com.nbtech.ailab.vo.GroupQcVo;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nbtech.ailab.biz.service.IGroupsPersonService;
import com.nbtech.ailab.util.QRCodeExcelUtil;
import com.google.zxing.WriterException;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import javax.imageio.ImageIO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GroupPersonFacade {

    @Autowired
    private IExperimentPlanService experimentPlanService;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private GroupsPersonDao groupsPersonDao;

    @Autowired
    private GroupsDao groupsDao;

    @Autowired
    private IGroupsService groupsService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private IGroupsPersonService groupsPersonService;
    /**
     * 实验网址
     */
    @Value("${experiment.address}")
    private String address;

    @Value("${experiment.address}")
    private String experimentAddress;

    private static final String keySecret = "{\"keyArr\":\"NfMiYYvOXdzRWgLJGTbJeA==\"}";

    /**
     * 获取用户和密码以及二维码
     * 
     * @param groupId
     * @return
     */
    public List<GroupQcVo> getUserQcList(Long groupId) throws Exception {
        return commonUtil.getUserQcList(groupId);
    }

    /**
     * 导出实验网址
     *
     * @param dto
     * @param response
     * @throws Exception
     */
    public void export(GroupsPersonDto dto, HttpServletResponse response) throws Exception {
        Long groupsId = dto.getGroupsId();

        String experimentScene = groupsDao.getExperimentScene(groupsId);
        GroupsDto groupsDto = groupsService.get(groupsId);
        ExperimentPlanDto experimentPlanDto = experimentPlanService.get(groupsDto.getExperimentId());
        // 如果实验计划是 教案 教案没有人群包 无法下载
        // if
        // (ExperimentPlanProtocolEnum.TEACHING_PLAN.name().equals(experimentPlanDto.getProtocol()))
        // {
        // throw new
        // BizException(BizResponseCodeEnum.TEACHING_PLAN_NOT_EXIST_PERSONGROUP);
        // }
        switch (experimentScene) {
            case ExperimentSceneConstant.COOPERATIVE_ASSESSMENT:
            case ExperimentSceneConstant.MODEL_DIALOGUE:
                if (experimentPlanDto.getExperimentStyle().equals(ExperimentStyle.GROUPS_PERSON)) {
                    // 模型对话格式的excel
                    chatModelPerson(groupsId, response);
                } else {
                    commonUtil.exportLink(groupsId, response);
                }
                break;
            default:
        }
    }

    /**
     * 模型对话excel人群包导出格式
     */
    void chatModelPerson(Long groupsId, HttpServletResponse response) throws Exception {

        List<GroupsPersonExcel> excel = new ArrayList<>();

        String aesString = groupsDao.getKeyString(groupsId);
        ObjectMapper objectMapper = new ObjectMapper();
        AesKeyVo aesKeyVo = objectMapper.readValue(aesString, AesKeyVo.class);
        List<GroupsPersonEntity> entities = groupsPersonDao.selectList(Wrappers.<GroupsPersonEntity>lambdaQuery()
                .eq(GroupsPersonEntity::getGroupsId, groupsId));
        List<GroupsPersonDto> list = ConvertUtils.sourceToTarget(entities, GroupsPersonDto.class);
        list.forEach(x -> {
            SysUserEntity entity = sysUserDao.selectById(x.getUserId());
            // 设置账号密码 需要解密
            x.setUserName(entity.getUsername());
            x.setPassword(AesSecret.cancel(x.getPassword(), aesKeyVo.getKeyArr()));
            GroupsPersonExcel groupsPersonExcel = ConvertUtils.sourceToTarget(x, GroupsPersonExcel.class);
            excel.add(groupsPersonExcel);
        });

        commonUtil.setExportHeadParam(groupsId, response, excel, GroupsPersonExcel.class);

    }


    /**
     * 导出实验计划下所有实验组的人员信息到多个sheet
     *
     * @param experimentPlanId 实验计划id
     * @param response
     * @throws Exception
     */
    public void exportByExperimentPlan(Long experimentPlanId, HttpServletResponse response) throws Exception {
        // 获取实验计划信息
        ExperimentPlanDto experimentPlanDto = experimentPlanService.get(experimentPlanId);
        if (experimentPlanDto == null) {
            throw new BizException(BizResponseCodeEnum.GLOBAL_ERROR);
        }

        // 获取实验计划下的所有实验组
        List<Long> groupIds = groupsDao.getGroupIdList(experimentPlanId);
        if (CollectionUtil.isEmpty(groupIds)) {
            throw new BizException(BizResponseCodeEnum.GLOBAL_ERROR);
        }

        // 设置响应头
        String excelName = experimentPlanDto.getExperimentName() + "_Experiment Kit";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(excelName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 创建ExcelWriter
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream())
                .registerWriteHandler(new SimpleColumnWidthStyleStrategy(30))
                .useDefaultStyle(false)
                .build();

        try {
            // 遍历每个实验组，创建对应的sheet
            for (Long groupId : groupIds) {
                String experimentScene = groupsDao.getExperimentScene(groupId);
                GroupsDto groupsDto = groupsService.get(groupId);

                // 根据实验场景类型选择导出格式
                switch (experimentScene) {
                    case ExperimentSceneConstant.COOPERATIVE_ASSESSMENT:
                    case ExperimentSceneConstant.MODEL_DIALOGUE:
                        if (experimentPlanDto.getExperimentStyle().equals(ExperimentStyle.GROUPS_PERSON)) {
                            // 模型对话格式（账号类型）
                            writeChatModelPersonSheet(excelWriter, groupId, groupsDto.getGroupsName());
                        } else {
                            // 链接类型
                            writeLinkPersonSheet(excelWriter, groupId, groupsDto.getGroupsName());
                        }
                        break;
                    default:
                        break;
                }
            }
        } finally {
            // 关闭ExcelWriter
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 写入模型对话格式的sheet
     */
    private void writeChatModelPersonSheet(ExcelWriter excelWriter, Long groupsId, String sheetName) throws Exception {
        List<GroupsPersonExcel> excel = new ArrayList<>();

        String aesString = groupsDao.getKeyString(groupsId);
        ObjectMapper objectMapper = new ObjectMapper();
        AesKeyVo aesKeyVo = objectMapper.readValue(aesString, AesKeyVo.class);
        List<GroupsPersonEntity> entities = groupsPersonDao.selectList(Wrappers.<GroupsPersonEntity>lambdaQuery()
                .eq(GroupsPersonEntity::getGroupsId, groupsId));
        List<GroupsPersonDto> list = ConvertUtils.sourceToTarget(entities, GroupsPersonDto.class);
        list.forEach(x -> {
            SysUserEntity entity = sysUserDao.selectById(x.getUserId());
            x.setUserName(entity.getUsername());
            x.setPassword(AesSecret.cancel(x.getPassword(), aesKeyVo.getKeyArr()));
            GroupsPersonExcel groupsPersonExcel = ConvertUtils.sourceToTarget(x, GroupsPersonExcel.class);
            excel.add(groupsPersonExcel);
        });

        // 查询实验场景等头部数据
        ExperimentPlanEntity experimentPlanEntity = groupsDao.getExpermentPlan(groupsId);
        ModelExportCellHandle modelExportCellHandle = ConvertUtils.sourceToTarget(experimentPlanEntity,
                ModelExportCellHandle.class);
        modelExportCellHandle.setSheetName(sheetName);
        modelExportCellHandle.setWebsite(address + "/experiment");

        // 创建sheet
        WriteSheet writeSheet = EasyExcel.writerSheet(sheetName)
                .head(GroupsPersonExcel.class)
                .registerWriteHandler(new ImageWriteHandler())
                .registerWriteHandler(modelExportCellHandle)
                .relativeHeadRowIndex(7)
                .build();

        excelWriter.write(excel, writeSheet);
    }

    /**
     * 写入链接类型的sheet
     */
    private void writeLinkPersonSheet(ExcelWriter excelWriter, Long groupsId, String sheetName) throws Exception {
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

        // 查询实验场景等头部数据
        ExperimentPlanEntity experimentPlanEntity = groupsDao.getExpermentPlan(groupsId);
        ModelExportCellHandle modelExportCellHandle = ConvertUtils.sourceToTarget(experimentPlanEntity,
                ModelExportCellHandle.class);
        modelExportCellHandle.setSheetName(sheetName);
        modelExportCellHandle.setWebsite(address + "/experiment");

        // 创建sheet
        WriteSheet writeSheet = EasyExcel.writerSheet(sheetName)
                .head(GroupPersonExcelTwo.class)
                .registerWriteHandler(new ImageWriteHandler())
                .registerWriteHandler(modelExportCellHandle)
                .relativeHeadRowIndex(7)
                .build();

        excelWriter.write(excel, writeSheet);
    }
}
