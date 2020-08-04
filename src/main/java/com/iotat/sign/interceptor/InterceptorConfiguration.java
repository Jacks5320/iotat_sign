package com.iotat.sign.interceptor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *@file 配置拦截器
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

	@Autowired
	private AppInterceptor appInterceptor;

	/**
	 * 配置拦截器
	 */
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                  .allowedOrigins("*")
                  .allowCredentials(true)
                  .allowedMethods("GET", "POST", "DELETE", "PUT","PATCH")
                  .maxAge(3600);
    }

	@Override
	public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(appInterceptor).addPathPatterns("/api/**");
    }

} 
