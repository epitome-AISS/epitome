package com.nbtech.ailab.facade;

import com.nbtech.ailab.biz.dto.SysUserDto;
import com.nbtech.ailab.biz.service.ISysUserService;
import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.constant.UserConstant;
import com.nbtech.ailab.util.*;
import com.nbtech.ailab.vo.SysUserVo;
import com.nbtech.common.annotation.LogOperation;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.model.BizResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@Api(tags = "登录登出")
public class AccountFacade {

    private static final Logger log = LoggerFactory.getLogger(AccountFacade.class);
    @Autowired
    private UserFacade userFacade;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DifyApiUtil difyApiUtil;

    @Autowired
    private ISysUserService sysUserService;

    @PostMapping("/login")
    @ApiOperation("登录")
    @LogOperation("登录")
    public BizResponse<SysUserVo> login(@RequestBody UsernamePasswordToken token) {
        log.info("用户登陆 登陆用户名为 {}", token.getUsername());
        // 登出当前session用户
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        // 登录
        Subject loginUser = SecurityUtils.getSubject();
        loginUser.login(token);
        log.info("用户登陆成功 登陆用户名为 {}", token.getUsername());
        Long userId = ShiroUtils.getUserId();
        return BizResponse.success(userFacade.get(userId));
    }

    @GetMapping("/logout")
    @ApiOperation("登出")
    @LogOperation("登出")
    public BizResponse<?> logout() {
        // 登出当前session用户
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return BizResponse.success();
    }

    @RequestMapping("/unauthorized")
    @ApiOperation(value = "未认证", hidden = true)
    @ResponseBody
    public BizResponse<?> unauthorizedUrl() {
        throw new BizException(BizResponseCodeEnum.SESSION_TIMEOUT);
    }

    /**
     * 重置密码
     *
     * @param dto dto
     */
    public void resetPassword(SysUserDto dto) {
        SysUserDto user = sysUserService.get(dto.getId());
        String password = ShiroUtils.sha256WithSalt(dto.getPassword(), UserConstant.SALT_PREFIX + user.getUsername());
        dto.setPassword(password);
        sysUserService.update(dto);
    }
}
