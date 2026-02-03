package com.nbtech.ailab.biz.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nbtech.ailab.biz.dto.SysUserDto;
import com.nbtech.ailab.common.BasicModelAvailableStatusEnum;
import com.nbtech.ailab.common.BasicModelUseStatusEnum;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.constant.HttpUrlRecord;
import com.nbtech.ailab.external.vo.EnableModelVo;
import com.nbtech.ailab.external.vo.ModelInfoVo;
import com.nbtech.ailab.util.ShiroUtils;
import com.nbtech.ailab.vo.ResponseData;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.service.impl.CrudServiceImpl;
import com.nbtech.ailab.biz.dao.BasicModelDao;
import com.nbtech.ailab.biz.dto.BasicModelDto;
import com.nbtech.ailab.biz.entity.BasicModelEntity;
import com.nbtech.ailab.biz.service.IBasicModelService;
import com.nbtech.common.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础模型表
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-14
 */
@Slf4j
@Service
public class BasicModelServiceImpl extends CrudServiceImpl<BasicModelDao, BasicModelEntity, BasicModelDto>
        implements IBasicModelService {

    @Value("${basicModel.address}")
    private String basicModelAddress;

    @Override
    public QueryWrapper<BasicModelEntity> getWrapper(BasicModelDto dto) {
        QueryWrapper<BasicModelEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(dto.getUseStatus() != null, "use_status", dto.getUseStatus());
        wrapper.like(dto.getName() != null, "name", dto.getName());
        SysUserDto user = ShiroUtils.getUserEntity();
        wrapper.eq("user_id", user.getId());
        wrapper.orderByDesc("update_date");
        wrapper.select("id", "name", "chinese_name", "english_name", "chinese_desc", "user_id", "use_status",
                "basic_model_attribution", "english_desc", "context_length","available_status");
        return wrapper;
    }

    @Override
    public void saveBasicMode(BasicModelDto basicModelDto) {
        // 验证基础模型是否可用
        if (!verifyBasicModel(basicModelDto)) {
            throw new BizException(BizResponseCodeEnum.BASIC_MODEL_VERIFY);
        }
        SysUserDto user = ShiroUtils.getUserEntity();
        // 添加持有人信息
        basicModelDto.setBasicModelAttribution(user.getUsername());
        basicModelDto.setUserId(user.getId());
        // 设置初始状态为启用
        basicModelDto.setUseStatus(BasicModelUseStatusEnum.ENABLE.name());
        // 新增时默认设置为可用
        basicModelDto.setAvailableStatus(BasicModelAvailableStatusEnum.AVAILABLE.name());
        // 新增dify的基础模型
        // ModelInfoVo modelInfoVo = ConvertUtils.sourceToTarget(basicModelDto,
        // ModelInfoVo.class);
        // modelInfoVo.setUserId(basicModelDto.getUserId().intValue());
        // modelInfoVo.setModel_provider(basicModelDto.getModelType());
        // modelInfoVo.setContextLength(basicModelDto.getContextLength().toString());
        // modelInfoVo.setUrl(modelInfoVo.getUrl().replace("/v1", ""));
        // basicModelFacade.addBasicModel(modelInfoVo);
        // 保存基础模型
        save(basicModelDto);
    }

    /**
     * 校验 基础模型是否可用
     *
     * @param basicModelDto 基础模型信息
     */
    boolean verifyBasicModel(BasicModelDto basicModelDto) {
        Map<String, String> params = new HashMap<String, String>() {
            {
                put("url", basicModelDto.getUrl());
                put("apiKey", basicModelDto.getApiKey());
                put("modelName", basicModelDto.getName());
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, java.util.concurrent.TimeUnit.SECONDS) // 设置连接超时时间
                .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS) // 设置读取超时时间
                .addInterceptor(new RetryInterceptor(3)) // 添加重试拦截器，设置重试次数为3次
                .build();
        // 添加参数
        RequestBody body = RequestBody.create(JSON.toJSONString(params),
                MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(basicModelAddress + HttpUrlRecord.VERIFY_MODEL)
                .post(body)
                .build();
        log.info("发送验证基础模型是否可用的请求,请求地址为 {}", basicModelAddress + HttpUrlRecord.VERIFY_MODEL);
        try (Response response = client.newCall(request).execute()) {
            String bodyString = response.body().string();
            // 打印响应体
            log.info("验证基础模型是否可用的请求,返回结果为 {}", bodyString);
            ResponseData responseData = JSON.parseObject(bodyString, ResponseData.class);
            // 等于0就是请求成功
            if (responseData.getCode() == 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据基础模型id验证并更新可用状态
     *
     * @param id 基础模型id
     */
    @Override
    public void verifyBasicModelById(Long id) {
        BasicModelEntity basicModelEntity = baseDao.selectById(id);
        if (basicModelEntity == null) {
            throw new BizException(BizResponseCodeEnum.GLOBAL_ERROR);
        }
        BasicModelDto basicModelDto = ConvertUtils.sourceToTarget(basicModelEntity, BasicModelDto.class);
        // 调用验证方法
        boolean isAvailable = verifyBasicModel(basicModelDto);
        // 根据验证结果更新可用状态
        basicModelDto.setId(id);
        if (isAvailable) {
            basicModelDto.setAvailableStatus(BasicModelAvailableStatusEnum.AVAILABLE.name());
        } else {
            basicModelDto.setAvailableStatus(BasicModelAvailableStatusEnum.UNAVAILABLE.name());
        }
        // 更新数据库
        update(basicModelDto);
    }

    static class RetryInterceptor implements Interceptor {
        private final int maxRetry;
        private int retryCount = 0;

        public RetryInterceptor(int maxRetry) {
            this.maxRetry = maxRetry;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            while (!response.isSuccessful() && retryCount < maxRetry) {
                retryCount++;
                response = chain.proceed(request);
            }
            return response;
        }
    }

    @Override
    public void updateUseStatus(BasicModelDto dto) {
        String status = dto.getUseStatus();
        // 禁用 需要判断是否被已启用的模型引用 引用了就不能禁用
        if (BasicModelUseStatusEnum.DRAFT.name().equals(dto.getUseStatus())) {
            // 被模型引用
            Integer modelCount = baseDao.getUseModelId(dto.getId());
            // 被聊天室引用的个数
            Integer chatRoomCount = baseDao.getUseChatRoomId(dto.getId());
            if ((modelCount + chatRoomCount) > 0) {
                throw new BizException(BizResponseCodeEnum.BASIC_MODEL_FORBID);
            }
            status = "DISABLE";
        }
        update(dto);
        BasicModelEntity basicModelEntity = baseDao.selectById(dto.getId());
        EnableModelVo enableModelVo = new EnableModelVo();
        enableModelVo.setName(basicModelEntity.getName());
        enableModelVo.setUseStatus(status);
        // basicModelFacade.enableBasicModel(enableModelVo);
    }

    /**
     * 判断除自己以外的基础模型名称是否相同
     *
     * @param name 名称
     * @param type 1 名称 2 英文名称 3 中文名称
     */
    @Override
    public Boolean judgeSameName(String name, int type) {
        // 相同模型名称
        if (type == 1 && baseDao.exists(Wrappers.<BasicModelEntity>lambdaQuery()
                .eq(BasicModelEntity::getName, name))) {
            return false;
        }
        // 相同英文模型名称
        if (type == 2 && baseDao.exists(Wrappers.<BasicModelEntity>lambdaQuery()
                .eq(BasicModelEntity::getEnglishName, name))) {
            return false;
        }
        // 相同中文模型名称
        if (type == 3 && baseDao.exists(Wrappers.<BasicModelEntity>lambdaQuery()
                .eq(BasicModelEntity::getChineseName, name))) {
            return false;
        }
        return true;
    }

    @Override
    public List<BasicModelDto> getOwnerBasicModel() {
        SysUserDto user = ShiroUtils.getUserEntity();
        return baseDao.getOwnerBasicModels(BasicModelUseStatusEnum.ENABLE.name(), user.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBasicModel(List<Long> ids) {
        for (Long id : ids) {
            BasicModelEntity basicModelEntity = baseDao.selectById(id);
            ModelInfoVo modelInfoVo = ConvertUtils.sourceToTarget(basicModelEntity, ModelInfoVo.class);
            modelInfoVo.setUserId(basicModelEntity.getUserId().intValue());
            modelInfoVo.setContextLength(basicModelEntity.getContextLength().toString());
        }
        delete(ids);
    }

}