package com.bridgeIt.restDemo.model;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class Runner {

	static final String topicName="myTopic";
	@Autowired
	private RabbitTemplate getTemplate;
	
	@Autowired
	private Receiver receiver;
	
	
	
	public void run() {
		System.out.println("sending message;");
		getTemplate.convertAndSend(topicName, "myBindingKey", "Hello from rabbitMq");
		 try {
			receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		
			e.printStackTrace();
		}
	}
	
	
	
}
