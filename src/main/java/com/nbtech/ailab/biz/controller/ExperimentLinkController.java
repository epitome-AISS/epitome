package com.nbtech.ailab.biz.controller;

import com.nbtech.ailab.biz.service.IExperimentLinkService;
import com.nbtech.ailab.facade.ExperimentLinkFacade;
import com.nbtech.ailab.vo.LinkVo;
import com.nbtech.ailab.vo.SysUserVo;
import com.nbtech.common.model.BizResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 实验链接
 *
 * @author Joker minnan@nb-tec.cn
 * @since 1.0.0 2025-02-11
 */
@RestController
@RequestMapping("experimentlink")
@Api(tags = "实验链接")
public class ExperimentLinkController {
    @Autowired
    private IExperimentLinkService experimentLinkService;

    @Autowired
    private ExperimentLinkFacade experimentLinkFacade;

    @PostMapping
    @ApiOperation("根据链接信息登录")
    public BizResponse<?> loginByLink(@RequestBody LinkVo vo) throws Exception {
        SysUserVo sysUserVo = experimentLinkFacade.loginByLink(vo.getLink());
        return BizResponse.success(sysUserVo);
    }


}