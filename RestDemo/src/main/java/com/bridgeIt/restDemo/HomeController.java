package com.bridgeIt.restDemo;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeIt.restDemo.model.BaseResponse;
import com.bridgeIt.restDemo.model.PaymentRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Handles requests for the application home page.
 */
@JsonFormat
@RestController
public class HomeController {
	

	 private final String sharedKey = "SHARED_KEY";
	 private static final String SUCCESS_STATUS = "success";
	 private static final String ERROR_STATUS = "error";
	 private static final int CODE_SUCCESS = 100;
	 private static final int AUTH_FAILURE = 102;
	
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping(value="/pay", method = RequestMethod.POST , produces="application/json" )
	public BaseResponse responser (@RequestParam(value = "key") String key, @RequestBody PaymentRequest request){
		BaseResponse response = new BaseResponse();
		if(key.equalsIgnoreCase(sharedKey)) {
			
			response.setCode(CODE_SUCCESS);
			response.setStatus(SUCCESS_STATUS);
			System.out.println(request);
			return response;
		}else {
			
			response.setCode(AUTH_FAILURE);
			response.setStatus(ERROR_STATUS);
			return response;
		}
		
	}
	
	@RequestMapping(value="rabbit/{key}", method = RequestMethod.POST , produces="application/json" )
	public void rabbit(@PathVariable("key") String key) {
		rabbitTemplate.convertAndSend(key);
		
	}
	
	
	
	
	
}
