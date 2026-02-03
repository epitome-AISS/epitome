package com.nbtech.ailab.external.controller;

import com.nbtech.ailab.external.facade.QuestionnaireStarFacade;
import com.nbtech.ailab.external.vo.QuestionStartVo.QuestionnaireRequestVo;
import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.model.BizResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("external/qnStar")
@Api(tags = "问卷星接口")
public class QuestionnaireStarController {

    @Autowired
    private QuestionnaireStarFacade questionnaireStarFacade;

    @GetMapping("getSign")
    @ApiOperation("调用数据")
    @LogOperation("调用数据")
    public BizResponse<Map> getSign(@RequestParam("subuser") String subuser) {
        Map sign = questionnaireStarFacade.getSign(subuser);
        return BizResponse.success(sign);
    }

    @GetMapping("getSha1String")
    @ApiOperation("sha1字符串加密")
    @LogOperation("sha1字符串加密")
    public BizResponse<String> getSha1String(@RequestParam("target") String target) {
        return BizResponse.success(questionnaireStarFacade.getSha1String(target));
    }

    @GetMapping("getQuestionList")
    @ApiOperation("获取问卷列表")
    @LogOperation("获取问卷列表")
    public BizResponse<Object> getQuestionList(@ApiParam("1 代表已经发布的问卷") @RequestParam(value = "status", defaultValue = "1") Integer status,
                                               @RequestParam(value = "current", defaultValue = "1") Integer current,
                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Object data = questionnaireStarFacade.getQuestionList(status, current, size);
        return BizResponse.success(data);
    }

    @GetMapping("getQuestionData")
    @ApiOperation("获取问卷详情")
    @LogOperation("获取问卷详情")
    public BizResponse<Object> getQuestionData(@RequestParam("vid") String vid) {
        Object data = questionnaireStarFacade.getQuestionData(vid);
        return BizResponse.success(data);
    }

    @GetMapping("getLoginUrl")
    @ApiOperation("获取快速登录问卷星地址")
    @LogOperation("获取快速登录问卷星地址")
    public BizResponse<String> getLoginUrl(@RequestParam(name = "subuser", required = false) String subuser) {
        return BizResponse.success(questionnaireStarFacade.getLoginUrl(subuser));
    }

    @PostMapping("getStarData")
    @ApiOperation("获取问卷星推送的答卷数据")
    public BizResponse<?> getStarData(HttpServletRequest request) throws IOException {
        questionnaireStarFacade.getStarData(request);
        return BizResponse.success();
    }

    @PostMapping("getQuestionnaireData")
    @ApiOperation("获取问卷数据")
    public BizResponse<?> getQuestionnaireData(@RequestParam("ActivityId") String activityId,
                                               @RequestParam("ActivityName") String activityName,
                                               @RequestParam("ActivityDomain") String activityDomain,
                                               @RequestParam("ActivityPCUrl") String activityPCUrl,
                                               @RequestParam("ActivityH5Url") String activityH5Url,
                                               @RequestParam(value = "wjxparams", required = false) String wjxparams,
                                               @RequestBody String Content) {

        // 获取 AES 加密的问卷内容

        QuestionnaireRequestVo request = new QuestionnaireRequestVo();
        request.setActivityId(activityId);
        request.setActivityName(activityName);
        request.setActivityDomain(activityDomain);
        request.setActivityPCUrl(activityPCUrl);
        request.setActivityH5Url(activityH5Url);
        request.setWjxparams(wjxparams);
        request.setContent(Content);
        questionnaireStarFacade.getQuestionnaireData(request);
        return BizResponse.success();
    }

}
