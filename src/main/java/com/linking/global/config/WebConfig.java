package com.linking.global.config;

import com.linking.global.auth.interceptor.LoginCheckInterceptor;
import com.linking.global.auth.resolver.LoginUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Bean
//    public InterceptorRegistry addInterceptors() {
//        InterceptorRegistry registry = new InterceptorRegistry();
//        registry.addInterceptor(new LoginCheckInterceptor());
//
////        registry.e("/", "/auth/login");
//        return registry;
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error", "/users/sign-in", "/users/sign-up", "/users/verify/email");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver());
    }
}
