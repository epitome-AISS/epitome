package com.nbtech.ailab.biz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nbtech.ailab.biz.dto.MaterialDto;
import com.nbtech.ailab.facade.ModelCollectionFacade;
import com.nbtech.ailab.vo.EleParamVo;
import com.nbtech.common.model.BizResponse;
import com.nbtech.common.model.PageDto;
import com.nbtech.common.model.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author nber
 */
@RestController
@RequestMapping("modelCollection")
@Api(tags = "模型图像展示")
@RequiresRoles("manager")
public class ModelCollectionController {

    @Autowired
    private ModelCollectionFacade modelCollectionFacade;

    @GetMapping("getModelElement")
    @ApiOperation("获取某个实验组下的所有模型算子id")
    public BizResponse<?> getModelElement(@ApiParam("实验组id") @RequestParam(name = "groupId") Long groupId) {
        return BizResponse.success(modelCollectionFacade.getModelElement(groupId));
    }

    @GetMapping("getModelList")
    @ApiOperation("获取某个大模型下的所有基础模型")
    public BizResponse<?> getModelList(@ApiParam("模型id") @RequestParam(name = "modelId") Long modelId) throws JsonProcessingException {
        return BizResponse.success(modelCollectionFacade.getModelList(modelId));
    }

    @PostMapping("getModelUser")
    @ApiOperation("获取选中的模型的使用人数")
    public BizResponse<?> getModelUser(@RequestBody EleParamVo paramVo) throws JsonProcessingException {
        return BizResponse.success(modelCollectionFacade.getModelUser(paramVo));
    }

    @PostMapping("getModelUseNumber")
    @ApiOperation("统计每个模型每个数据段的对话个数")
    public BizResponse<?> getModelUseNumber(@RequestBody EleParamVo paramVo) throws JsonProcessingException {
        return BizResponse.success(modelCollectionFacade.getModelUseNumber(paramVo));
    }

    @PostMapping("getAvgInput")
    @ApiOperation("统计每个模型对话的平均字数")
    public BizResponse<?> getAvgInput(@RequestBody EleParamVo paramVo) throws JsonProcessingException {
        return BizResponse.success(modelCollectionFacade.getAvgInput(paramVo));
    }

    @PostMapping("getAvgUseTime")
    @ApiOperation("统计每个模型的每个人平均使用时长")
    public BizResponse<?> getAvgUseTime(@RequestBody EleParamVo paramVo) throws JsonProcessingException {
        return BizResponse.success(modelCollectionFacade.getAvgUseTime(paramVo));
    }


}
