package com.bridgeIt.restDemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@ComponentScan("com.bridgeIt.restDemo")
@Configuration
public class SpringConfig extends WebMvcConfigurerAdapter {

/*	@Bean
	public InternalResourceViewResolver internalViewResolver () {
		
		  InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		  resolver.setViewClass(JstlView.class);
		  resolver.setPrefix("/WEB-INF/views/");
		  resolver.setSuffix(".jsp");
		
		  return resolver;
	}*/
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		
		configurer.enable();
	}
	
	
}
