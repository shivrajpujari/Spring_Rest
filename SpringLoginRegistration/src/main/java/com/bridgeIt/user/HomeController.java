package com.bridgeIt.user;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bridgeIt.user.model.User;
import com.bridgeIt.user.service.UserService;

/**
 * Handles requests for the application home page.
 */
@RestController
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	UserService service;
	
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
	public ResponseEntity<BaseResponse> responser ( @RequestBody User user){
	
		 ResponseEntity<BaseResponse> respond = service.userReg(user);
		
		System.out.println("agfiagfiga");
		return respond;
	}
	
	@RequestMapping(value="/login",method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<BaseResponse> login(@RequestParam(value="email") String email,@RequestParam(value="password") String password ){
		
		 ResponseEntity<BaseResponse> respond =service.login(email, password);

		return respond;

	}
		
}
