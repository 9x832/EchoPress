package com.ruoyiblog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.ruoyiblog.interceptor.JwtInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/blog/admin/**", "/blog/user/**", "/blog/comment/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:upload/");
    }
}
