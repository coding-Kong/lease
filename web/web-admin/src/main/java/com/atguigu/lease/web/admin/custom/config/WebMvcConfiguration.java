package com.atguigu.lease.web.admin.custom.config;

import com.atguigu.lease.web.admin.custom.converter.StringToBaseEnumConverterFactory;
import com.atguigu.lease.web.admin.custom.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author kdz
 * @create 2025-06-25-15:02
 */

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

//一个枚举类实现方式
//    @Autowired
//    private StringToItemTermConverter stringToItemTermConverter;


    //枚举工厂类转换实现方式
    @Autowired
    private StringToBaseEnumConverterFactory stringToBaseEnumConverterFactory;

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        //mvc注册枚举转换器
//        registry.addConverter(this.stringToItemTermConverter);
        registry.addConverterFactory(this.stringToBaseEnumConverterFactory);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(this.authenticationInterceptor)
                .addPathPatterns("/admin/**").excludePathPatterns("/admin/login/**");
    }
}
