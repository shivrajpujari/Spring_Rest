package com.bridgeIt.user;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@EnableWebMvc
@ComponentScan("com.bridgeIt.user")
@Configuration
public class SpringConfig extends WebMvcConfigurerAdapter {

	@Bean
	public InternalResourceViewResolver internalViewResolver () {		
		  InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		  resolver.setViewClass(JstlView.class);
		  resolver.setPrefix("/WEB-INF/views/");
		  resolver.setSuffix(".jsp");		
		  return resolver;
	}
	
	
	@Bean
	public DataSource dataSource() {
		
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setUser("root");
		cpds.setPassword("shiv");
		try {
			cpds.setDriverClass("com.mysql.jdbc.Driver");
			cpds.setJdbcUrl("jdbc:mysql://localhost:3306/Shiv?useSSL=false");
			cpds.setMinPoolSize(5);                                     
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(20);
		
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		
		return cpds;
	}
	
}
