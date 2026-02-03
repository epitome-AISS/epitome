package com.nbtech.ailab.util;

import com.nbtech.ailab.biz.dto.SysUserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author nber
 */
@Slf4j
public class ShiroUtils {
    /**
     * 加密算法
     */
    public final static String hashAlgorithmName = "SHA-256";
    /**
     * 循环次数
     */
    public final static int hashIterations = 20;

    public static String sha256WithSalt(String password, String salt) {
        return new SimpleHash(hashAlgorithmName, password, salt, hashIterations).toHex();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static Session getSession(Serializable id) {
        SessionsSecurityManager sessionsSecurityManager = (SessionsSecurityManager) SecurityUtils.getSecurityManager();
        DefaultSessionManager sessionManager = (DefaultSessionManager) sessionsSecurityManager.getSessionManager();
        Collection<Session> sessionCollection = sessionManager.getSessionDAO().getActiveSessions();
        for (Session session : sessionCollection) {
            if (session.getId().equals(id)) {
                return session;
            }
        }
        return null;
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static SysUserDto getUserEntity() {
        return (SysUserDto) getSubject().getPrincipal();
    }

    public static Long getUserId() {
        SysUserDto user = getUserEntity();
        if (user == null) {
            return 0L;
        }
        return user.getId();
    }

    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipal() != null;
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    public static void kickOut(Long userId) {
        SessionsSecurityManager sessionsSecurityManager = (SessionsSecurityManager) SecurityUtils.getSecurityManager();
        DefaultSessionManager sessionManager = (DefaultSessionManager) sessionsSecurityManager.getSessionManager();
        Collection<Session> sessionCollection = sessionManager.getSessionDAO().getActiveSessions();

        sessionCollection.forEach(x -> {
            Object principalCollection = x.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (principalCollection == null) {
                return;
            }
            log.info(principalCollection.toString());
            SimplePrincipalCollection simplePrincipalCollection = (SimplePrincipalCollection) principalCollection;
            SysUserDto userDto = (SysUserDto) simplePrincipalCollection.getPrimaryPrincipal();
            if (userId.equals(userDto.getId())) {
                log.info("kick out user: " + userDto);
                x.setTimeout(0);
            }
        });
    }

    public static void kickOut(Collection<Integer> userIds) {
        SessionsSecurityManager sessionsSecurityManager = (SessionsSecurityManager) SecurityUtils.getSecurityManager();
        DefaultSessionManager sessionManager = (DefaultSessionManager) sessionsSecurityManager.getSessionManager();
        Collection<Session> sessionCollection = sessionManager.getSessionDAO().getActiveSessions();

        sessionCollection.forEach(x -> {
            Object principalCollection = x.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (principalCollection == null) {
                return;
            }
            log.info(principalCollection.toString());
            SimplePrincipalCollection simplePrincipalCollection = (SimplePrincipalCollection) principalCollection;
            SysUserDto userDto = (SysUserDto) simplePrincipalCollection.getPrimaryPrincipal();
            if (userIds.contains(userDto.getId().intValue())) {
                log.info("kick out user: " + userDto);
                x.setTimeout(0);
            }
        });
    }


}
