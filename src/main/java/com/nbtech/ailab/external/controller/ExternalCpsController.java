package com.nbtech.ailab.external.controller;

import com.nbtech.ailab.external.facade.ExternalCpsFacade;
import com.nbtech.ailab.external.vo.InitialRequestVo;
import com.nbtech.ailab.external.vo.PushRequestVo;
import com.nbtech.ailab.external.vo.ScoreRequestVo;
import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)@RestController
@RequestMapping("external/cps")
@Api(tags = "对外cps接口")
public class ExternalCpsController {

    @Autowired
    private ExternalCpsFacade externalCpsFacade;

    @GetMapping("initial")
    @ApiOperation("合作测评界面初始化")
    @LogOperation("合作测评界面初始化")
    public BizResponse<?> initial(@RequestParam("element_id") String element_id,
                                  @RequestParam("user_id") String user_id,
                                  @RequestParam("role") String role,
                                  @RequestParam("scene") String scene) {
        externalCpsFacade.initial(element_id, user_id, role, scene);
        return BizResponse.success();
    }

    @PostMapping("push")
    @ApiOperation("初始表单数据推送")
    @LogOperation("初始表单数据推送")
    public BizResponse<?> push(@RequestBody InitialRequestVo request) {
        externalCpsFacade.push(request);
        return BizResponse.success();
    }

    @PostMapping("selectPush")
    @ApiOperation("选择结果推送")
    @LogOperation("选择结果推送")
    public BizResponse<?> selectPush(@RequestBody PushRequestVo request) {
        externalCpsFacade.selectPush(request);
        return BizResponse.success();
    }

    @PostMapping("scorePush")
    @ApiOperation("测评结果推送")
    @LogOperation("测评结果推送")
    public BizResponse<?> scorePush(@RequestBody ScoreRequestVo request) {
        externalCpsFacade.scorePush(request);
        return BizResponse.success();
    }

}
