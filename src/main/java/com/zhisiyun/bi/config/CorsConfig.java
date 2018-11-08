package com.zhisiyun.bi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 跨域资源共享配置
 */
@Configuration
public class CorsConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // todo 映射所有controller请求，正式环境应该需要针对不同的域开放不同的请求
        registry.addMapping("/*/**").allowedOrigins("*");
    }
}
