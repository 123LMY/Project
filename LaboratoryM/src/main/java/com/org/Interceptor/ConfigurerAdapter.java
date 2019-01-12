package com.org.Interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class ConfigurerAdapter extends WebMvcConfigurerAdapter {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns 用于添加拦截规则
        //excludePathPatterns 用于排除拦截
		InterceptorRegistration li = registry.addInterceptor(new Interceptor());
		li.addPathPatterns("/**");
		li.excludePathPatterns("/");
		li.excludePathPatterns("/userLogin");
		li.excludePathPatterns("/adminlogin");
		li.excludePathPatterns("/adminlogout");
		li.excludePathPatterns("/logincheck");
		li.excludePathPatterns("/js/**");
		li.excludePathPatterns("/css/**");
		li.excludePathPatterns("/fonts/**");
		li.excludePathPatterns("/img/**");
		li.excludePathPatterns("/templates/**");
		

    }
}