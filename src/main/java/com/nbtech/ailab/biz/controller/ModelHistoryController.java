package com.nbtech.ailab.biz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nbtech.ailab.biz.entity.HistoryRecordEntity;
import com.nbtech.ailab.biz.entity.ModelHistoryEntity;
import com.nbtech.ailab.facade.DataCollectionFacade;
import com.nbtech.ailab.facade.ModelFacade;
import com.nbtech.ailab.vo.ElementParamVo;
import com.nbtech.ailab.vo.RecordParamVo;
import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import com.nbtech.common.utils.ExcelUtils;
import com.nbtech.ailab.biz.dto.ModelHistoryDto;
import com.nbtech.ailab.biz.service.IModelHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
 * 模型问答历史
 *
 * @author van vanchen@nb-tec.cn
 * @since 1.0.0 2024-05-10
 */
@RestController
@RequestMapping("modelhistory")
@Api(tags="模型问答历史")
public class ModelHistoryController {
    @Autowired
    private IModelHistoryService modelHistoryService;

    @Autowired
    private ModelFacade modelFacade;

    @Autowired
    private DataCollectionFacade dataCollectionFacade;

    @GetMapping("page")
    @ApiOperation("分页")
    public BizResponse<PageResult<ModelHistoryDto>> page(PageDto pageDto, ModelHistoryDto dto){
        PageResult<ModelHistoryDto> page = modelHistoryService.page(pageDto, dto);
        return BizResponse.success(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public BizResponse<ModelHistoryDto> get(@PathVariable("id") Long id){
        ModelHistoryDto data = modelHistoryService.get(id);
        return BizResponse.success(data);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    public BizResponse<?> save(@RequestBody ModelHistoryDto dto){
        modelHistoryService.save(dto);
        return BizResponse.success();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    public BizResponse<?> update(@RequestBody ModelHistoryDto dto){
        modelHistoryService.update(dto);
        return BizResponse.success();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    public BizResponse<?> delete(@RequestBody Long[] ids){
        modelHistoryService.delete(Arrays.asList(ids));
        return BizResponse.success();
    }

    @PostMapping("getRecord")
    @ApiOperation("根据实验组id，用户id, 算子id（此处的算子id是指实验组配置json中的算子的id），获取历史问答记录")
    public BizResponse<List<HistoryRecordEntity>> getRecord(@RequestBody ElementParamVo elementParamVo) throws Exception {
       return BizResponse.success(modelFacade.getRecord(elementParamVo));
    }

    @PostMapping("getElementConfig")
    @ApiOperation("根据实验组id,算子id（此处的算子id是指实验组配置json中的算子的id）获取算子的配置")
    public BizResponse<?> getElementConfig(@RequestBody ElementParamVo elementParamVo){
        return BizResponse.success(modelFacade.getElementConfig(elementParamVo));
    }

    @PostMapping("getLastModel")
    @ApiOperation("根据实验组id，算子id（此处的算子id是指实验组配置json中的算子的id），获取最近的实验者使用的大模型")
    public BizResponse<?> getLastModel(@RequestBody ElementParamVo elementParamVo){
        return BizResponse.success(modelFacade.getLastModel(elementParamVo));
    }

    @PostMapping("saveHistoryRecord")
    @ApiOperation("根据实验组id，用户id, 算子id 模型名称（此处的算子id是指实验组配置json中的算子的id），写入新的问答记录，回答时长，问答字数")
    public BizResponse<?> saveHistoryRecord(@RequestBody RecordParamVo recordParamVo){
        modelFacade.redisSaveRecord(recordParamVo);
        return BizResponse.success();
    }

    @PostMapping("getRounds")
    @ApiOperation("获取当前模型 算子 用户 的问答次数")
    public BizResponse<?> getRounds(@RequestBody ModelHistoryEntity modelHistory){
        return BizResponse.success(modelFacade.getRounds(modelHistory));
    }

    /**
     * 根据ip修改当前受试者的地域
     * @param ip ip
     * @return
     */
    @GetMapping("getIpAddress")
    @ApiOperation("根据ip修改当前受试者的地域")
    BizResponse<?> getIpAddress(String ip){
        dataCollectionFacade.getAddress(ip);
        return BizResponse.success();
    }

    /**
     * 手动修改问卷的结果统计
     * @return
     */
    @GetMapping("collectRecord")
    @ApiOperation("手动修改问卷的结果统计")
    BizResponse<?> collectRecord() throws Exception {
        dataCollectionFacade.collectRecord();
        return BizResponse.success();
    }

    /**
     * 实验组级别统计实验组问卷结果
     * @return
     */
    @GetMapping("countQuestionnaire")
    @ApiOperation("实验组级别统计实验组问卷结果")
    BizResponse<?> countQuestionnaire(long groupId) throws Exception {
//        dataCollectionFacade.countQuestionnaire(groupId);
        return BizResponse.success();
    }



}