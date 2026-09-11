package com.hmall.common.config;

import com.hmall.common.UserInfoInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration//配置类
@ConditionalOnClass(DispatcherServlet.class)//只有DispatcherServlet类存在时，才会加载MvcConfig类
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //1.添加登录拦截器
        registry.addInterceptor(new UserInfoInterceptor());
    }
}
