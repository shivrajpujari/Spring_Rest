package com.bridgeIt.restDemo.config;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.sql.DataSource;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.LocalizedQueueConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.bridgeIt.restDemo.model.Receiver;
import com.mchange.v2.c3p0.ComboPooledDataSource;


@EnableWebMvc
@ComponentScan("com.bridgeIt")
@Configuration
public class SpringConfig extends WebMvcConfigurerAdapter {

	
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/**").allowedMethods("GET","POST").allowedOrigins("*").allowedHeaders("*");
		
	}
	
	@Bean
	public InternalResourceViewResolver internalViewResolver () {
		
		  InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		  resolver.setViewClass(JstlView.class);
		  resolver.setPrefix("/WEB-INF/views/");
		  resolver.setSuffix(".jsp");
		
		  return resolver;
	}
	
	
	static final String queueName="myQueue";
	static final String topicName="myTopic";
	
	
	@Bean
	ConnectionFactory factory(){
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		connectionFactory.setPort(5672);
		
		return connectionFactory;
		
	 }
	
	
	
	@Bean
	Queue queue() {
		
		
	 return new Queue(queueName, true);
	}
	
	@Bean
	TopicExchange topicExchange() {
		
		return new TopicExchange(topicName);
	}
	
	@Bean
	Binding binding () {
		
		
		return BindingBuilder.bind(queue()).to(topicExchange()).with("myBindingKey");
	}
	
	
//	@Bean
//    MessageListenerAdapter listenerAdapter(Receiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
	
	@Bean 
	RabbitTemplate getTemplate () {
		
		
		
		 RabbitTemplate r = new RabbitTemplate();
		 r.setConnectionFactory(factory());
		return r;
	}
	
	@Bean
	RabbitAdmin getRabbitAdmin() {
		RabbitAdmin admin = new RabbitAdmin(factory());
		admin.declareExchange(topicExchange());
		admin.declareQueue(queue());
		admin.declareBinding(binding());
		return admin;
	}
	
	@Bean
	SimpleRabbitListenerContainerFactory rabbitListenerFactory() {
		
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(factory());
		factory.setConcurrentConsumers(3);
		factory.setMaxConcurrentConsumers(10);
		return factory;	
	}
	
	@Bean
	SimpleMessageListenerContainer getSimpleMessageListenerContainer(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
		simpleMessageListenerContainer.setQueueNames(queueName);
		simpleMessageListenerContainer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				System.out.println("Message consumed : "+message);
			}
		});
		return simpleMessageListenerContainer;
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
	
	@Bean
	public JdbcTemplate template() {
		
		return new JdbcTemplate(dataSource());
	}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		
		configurer.enable();
	}
	
	
}
