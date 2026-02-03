package com.nbtech.ailab.handler;

import com.nbtech.ailab.common.LanguageEnum;
import com.nbtech.ailab.util.ThreadLocalManagerUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String language = request.getHeader("Language");
        if (StringUtils.isEmpty(language) || !LanguageEnum.names().contains(language)) {
            return true;
        }
        ThreadLocalManagerUtil.HeaderInfo headerInfo = new ThreadLocalManagerUtil.HeaderInfo();
        headerInfo.setLanguage(language);
        //在ThreadLocal当前国际化信息
        ThreadLocalManagerUtil.add(headerInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //会话结束移除线程缓存
        ThreadLocalManagerUtil.remove();
    }
}
