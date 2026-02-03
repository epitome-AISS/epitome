package com.nbtech.ailab.security;

import com.nbtech.ailab.util.RedisService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_HOUR;

/**
 * Shiro的配置文件
 *
 * @author chenj
 */
@Configuration
@Component
//@DependsOn(value = "redisProperties")
public class ShiroConfig implements ApplicationContextAware {

    RedisProperties redisProperties;

    @Bean
    public DefaultWebSessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 默认30分钟有效时间 - 每5分钟轮训一次
        sessionManager.setGlobalSessionTimeout(MILLIS_PER_HOUR * 24 * 2);
        sessionManager.setSessionValidationInterval(MILLIS_PER_HOUR / 12);
        // 配置缓存的redis信息
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }


    @Bean("securityManager")
    public SecurityManager securityManager(Oauth2Realm oAuth2Realm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setRememberMeManager(null);
        return securityManager;
    }


    /**
     * 下面三个是整合Redis后 缓存到登陆信息到redis中
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        // 其他不填会默认本地无密码redis
        redisManager.setHost("127.0.0.1:" + redisProperties.getPort());
        redisManager.setPassword("AilAB$%Terfre33");
        redisManager.setDatabase(redisProperties.getDatabase());
        return redisManager;
    }



    @Bean
    RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    @Bean
    RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        // Redis要保存的用户id
        redisCacheManager.setPrincipalIdFieldName("id");
        redisCacheManager.setExpire(1000 * 60 * 24);
        return redisCacheManager;
    }


    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/questionnaire/openList", "anon");
        filterMap.put("/material/openList", "anon");
        filterMap.put("/model/openList", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/account/login", "anon");
        filterMap.put("/experimentlink", "anon");
        filterMap.put("/sysUser/getUserInfo", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/experiment", "anon");
        filterMap.put("/model/getById", "anon");
        filterMap.put("/experimentprogress/getProgress", "anon");
        filterMap.put("/experimentprogress/forwardNextProgress", "anon");
        filterMap.put("/modelhistory/getRecord", "anon");
        filterMap.put("/modelhistory/saveHistoryRecord", "anon");
        filterMap.put("/doc.html", "anon");
        filterMap.put("/experiment/experimentplan/getHomeRecord", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/websocketChatRoom/**", "anon");
        filterMap.put("/experiment/groups/exportData", "anon");
        filterMap.put("/experiment/**", "anon");
        filterMap.put("/py_workflow_process/**", "anon");
        filterMap.put("/ailab/chatroomstructure/getChatRoomById", "anon");
        filterMap.put("/socket/changeMessage", "anon");
        filterMap.put("/external/cps/push", "anon");
        filterMap.put("/external/cps/selectPush", "anon");
        filterMap.put("/external/cps/scorePush", "anon");
        filterMap.put("/external/qnStar/getStarData", "anon");
        filterMap.put("/external/qnStar/getQuestionnaireData", "anon");
        filterMap.put("/experimentprogress/getInterveneList", "anon");
        filterMap.put("/experiment/experimentplan", "anon");
        filterMap.put("/experiment/groups", "anon");
        filterMap.put("/experimentprogress/overAuthentication", "anon");
        filterMap.put("/experiment/board/wordNumber", "anon");
        filterMap.put("/**", "authc");
//        filterMap.put("/**", "anon");

        shiroFilter.setFilterChainDefinitionMap(filterMap);

        //登录
        shiroFilter.setLoginUrl("/account/unauthorized");

        return shiroFilter;
    }

    /**
     * 下面三个方法是使权限注解生效的关键
     * 1 lifecycleBeanPostProcessor
     * 2 defaultAdvisorAutoProxyCreator
     * 3 authorizationAttributeSourceAdvisor
     *
     * @return
     */

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
        hashedCredentialsMatcher.setHashIterations(20);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        redisProperties = applicationContext.getBean(RedisProperties.class);
    }
}