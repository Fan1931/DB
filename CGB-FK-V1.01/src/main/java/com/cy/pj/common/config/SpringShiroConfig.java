package com.cy.pj.common.config;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.cy.pj.sys.service.realm.ShiroUserRealm;

/** Shiro的配置文件 */
@Configuration
public class SpringShiroConfig {
    /**单机环境，session交给shiro管理*/
    @Bean
    public DefaultWebSessionManager newSessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionValidationInterval(3600 * 1000);
        sessionManager.setGlobalSessionTimeout(3600 * 1000);
        return sessionManager;
    }
    @Bean("securityManager")
    public SecurityManager newSecurityManager(ShiroUserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(newSessionManager());
        securityManager.setRememberMeManager(newRememberMeManager());
        securityManager.setCacheManager(newCacheManager());
        return securityManager;
    }
    public RememberMeManager newRememberMeManager() {
    	CookieRememberMeManager cManager=new CookieRememberMeManager();
    	cManager.setCookie(newCookie());
    	return cManager;
    }
   
    public SimpleCookie newCookie() {
    	SimpleCookie sc=new SimpleCookie("simpleCookie");
    	sc.setMaxAge(7*24*60*60);
    	return sc;
    }

    public CacheManager newCacheManager() {
    	MemoryConstrainedCacheManager cacheManager=new MemoryConstrainedCacheManager();
    	return cacheManager;
    }
    
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl("/doLoginUI");
        shiroFilter.setUnauthorizedUrl("/");

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/bower_components/**", "anon");
        filterMap.put("/build/**", "anon");
        filterMap.put("/dist/**", "anon");
        filterMap.put("/plugins/**", "anon");
        filterMap.put("/user/doLogin","anon");
        filterMap.put("/doLogout", "logout");
        filterMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator newProxyCreator() {
    	return new DefaultAdvisorAutoProxyCreator();
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
