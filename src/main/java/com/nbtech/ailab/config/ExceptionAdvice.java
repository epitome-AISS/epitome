package com.nbtech.ailab.config;


import com.nbtech.ailab.common.BizResponseCodeEnum;
import com.nbtech.ailab.util.MessageUtil;
import com.nbtech.ailab.util.ThreadLocalManagerUtil;
import com.nbtech.common.exception.BizException;
import com.nbtech.common.model.BizResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(ShiroException.class)
    @ResponseBody
    public BizResponse<?> handleShiroException(ShiroException ex) {
        log.error("occurred shiro exception: ", ex);
        BizResponse<?> response = BizResponse.response(BizResponseCodeEnum.USERNAME_PASSWORD_ERROR);
        return this.convertLanguage(response);
    }

    @ExceptionHandler(LockedAccountException.class)
    @ResponseBody
    public BizResponse<?> LockedAccountException(ShiroException ex) {
        log.error("occurred shiro exception: ", ex);
        BizResponse<?> response = BizResponse.response(BizResponseCodeEnum.USER_FROZEN);
        return this.convertLanguage(response);
    }

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public BizResponse<?> handleBizException(BizException ex) {
        log.error("occurred biz exception: {}", ex.getMessage());
        BizResponse<?> response = BizResponse.exception(ex);
        return this.convertLanguage(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BizResponse<?> handleException(Exception ex) {
        log.error("occurred unknown exception: ", ex);
        BizResponse<?> response = BizResponse.response(BizResponseCodeEnum.GLOBAL_ERROR);
        return this.convertLanguage(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public BizResponse<?> handleException(UnauthorizedException ex) {
        log.error("occurred unknown exception: ", ex);
        BizResponse<?> response = BizResponse.response(BizResponseCodeEnum.PERMISSION_DENIED);
        return this.convertLanguage(response);
    }

    /**
     * 响应信息统一处理语言转换
     */
    private BizResponse<?> convertLanguage(BizResponse<?> response) {
        String convertMessage = MessageUtil.get(response.getCode().toString(), ThreadLocalManagerUtil.getLanguage());
        if (response.getCode().toString().equals(convertMessage)) {
            return response;
        }
        response.setMessage(convertMessage);
        return response;
    }
}
