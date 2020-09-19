package com.dcms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/21 0021 11:22
 * Description: 配置拦截器的拦截路径
 **/
@Configuration
@EnableCaching
public class UserLoginAdapter implements WebMvcConfigurer {
    @Autowired
    private UserLoginInterceptor userLoginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInterceptor).addPathPatterns("/doctor/**","/admin/**")
                .excludePathPatterns("/css/**","/img/**","/layui/**")
                .excludePathPatterns("/user/**")
                .excludePathPatterns("/login/**");
    }
}
