/*package com.bridgeIt.user;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bridgeIt.user.model.User;
import com.bridgeIt.user.service.UserServiceClass;

*//**
 * Handles requests for the application home page.
 *//*
@RestController
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	UserServiceClass service;
	
	*//**
	 * Simply selects the home view to render by returning its name.
	 *//*
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
	
		 ResponseEntity<BaseResponse> respond = service.userReg(user,result);
		
		System.out.println("agfiagfiga");
		return respond;
	}
	
	@RequestMapping(value="/login",method = RequestMethod.POST, produces="application/json")
	public ResponseEntity<BaseResponse> login(@RequestParam("email") String email,@RequestParam("password") String password ){
		System.out.println( email+" "+password);
		 ResponseEntity<BaseResponse> respond =service.login(email, password);

		return respond;

	}
		
}
*/