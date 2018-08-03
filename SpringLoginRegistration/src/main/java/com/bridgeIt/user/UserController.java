package com.bridgeIt.user;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeIt.user.model.User;
import com.bridgeIt.user.service.UserServiceImp;
import com.bridgeIt.user.service.utility.RabbitMsgSender;

@RestController
public class UserController {

	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserServiceImp service;
	
	@Autowired
	BaseResponse response;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
	//	System.out.println(dao.insert());
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	

	@RequestMapping(value="/register", method = RequestMethod.POST ,consumes="application/json", produces="application/json" )
	public ResponseEntity<BaseResponse> responser (@Valid @RequestBody User user,BindingResult result){
	System.out.println(" in / register");
		
		ResponseEntity<BaseResponse> respond;
		if(result.hasErrors()) {
			
			List errs=result.getFieldErrors();
			List <String>allErrorMsg = new ArrayList<String>();
			for (Object object : errs) {
				ObjectError objError=(ObjectError) object;
				allErrorMsg.add(objError.getDefaultMessage());
				System.out.println(objError.getDefaultMessage());
			}
		
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessage("please enter proper inputs");
			response.setErrors(allErrorMsg);
			respond = new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
			return respond;

		}
	
		 response=service.userReg(user);
		if(response.getStatus()==HttpStatus.BAD_REQUEST) {
			respond = new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
			return respond;
		}
		else {
			respond = new ResponseEntity<BaseResponse>(response,HttpStatus.OK);
			return respond;
		}
		
		
	}
	
	@RequestMapping(value="/login" , method = RequestMethod.POST, produces="application/json")
	public ResponseEntity<BaseResponse> login(@RequestParam("email") String email,@RequestParam("password") String password ){
		System.out.println( email+" "+password);
		System.out.println("-in /login");
		if(service.login(email, password)) {
			
			return new ResponseEntity<BaseResponse>(HttpStatus.OK);
		}	
		else {
			return new ResponseEntity<BaseResponse>(HttpStatus.BAD_REQUEST);
		}
		
	}
	@Autowired
	RabbitMsgSender producer;
	
	@RequestMapping(value="rabbit", method = RequestMethod.POST , produces="application/json" )
	public void rabbit(@RequestParam("key") String key) {
		System.out.println(key);
		//producer.sendMsg(key);
	
		
	}
	
	@RequestMapping(value="verify/{key:.+}", method = RequestMethod.GET )
	public void verify(@PathVariable String key) {
		
		System.out.println(key);
		boolean result= service.verify(key);
		System.out.println("is verification got completed? "+result);
		
	}
	
	

	
	
	
}
