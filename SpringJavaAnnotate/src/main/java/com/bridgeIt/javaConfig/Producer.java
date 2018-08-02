package com.bridgeIt.javaConfig;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class Producer {

	@Autowired
	AmqpTemplate template;
	
	void sendMsg(String message ) {
		
		template.convertAndSend("myTopic", "myBindingKey", message);;
		
	}
	
	
}
