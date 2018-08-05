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
import com.bridgeIt.user.service.UserService;
import com.bridgeIt.user.service.utility.RabbitMsgSender;



@RestController
public class UserController {

	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService  service;
	
	
	
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
		BaseResponse response = new BaseResponse();
		ResponseEntity<BaseResponse> respond;
		if(result.hasErrors()) {
			
			List<?> errs=result.getFieldErrors();
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
		BaseResponse response = new BaseResponse();
		System.out.println( email+" "+password);
		System.out.println("-in /login");
		if(service.login(email, password)) {
			response.setStatus(HttpStatus.OK);
			response.setMessage("you are logged in sucessfully");
			User user =service.getUser(email);
			String token = service.getToken(user);
			response.setToken(token);
			response.setUser(user);
			
			return new ResponseEntity<BaseResponse>(response,HttpStatus.OK);
		}	
		else {
			response.setMessage("Login failed...you are not a valid user");
			response.setStatus(HttpStatus.BAD_REQUEST);
			return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@Autowired
	RabbitMsgSender producer;
	
/*	@RequestMapping(value="rabbit", method = RequestMethod.POST , produces="application/json" )
	public void rabbit(@RequestParam("key") String key) {
		System.out.println(key);
		//producer.sendMsg(key);
	
		
	}*/
	
	@RequestMapping(value="verification/{flag}/{key}", method = RequestMethod.GET )
	public ResponseEntity<BaseResponse> verify(@PathVariable("key") String key ,  @PathVariable("flag") String flag) {
		BaseResponse response = new BaseResponse();
		if(flag.equals("getVerified")) {
			System.out.println(key);
			boolean result= service.verify(key);
			System.out.println("is verification got completed? "+result);
			
			if(result==true) {
				response.setStatus(HttpStatus.ACCEPTED);
				response.setMessage("you are verified");
				return new ResponseEntity<BaseResponse>(response,HttpStatus.ACCEPTED);
			}else {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessage("some thing went wrong!..");
				return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
				
			}
			
			
			
		}
		else if(flag.equals("reset_password")) {
			
			System.out.println(key+" from resetPassword");
			boolean result = service.checkSessionPassword(key);
			
			if(result==true) {
				response.setStatus(HttpStatus.ACCEPTED);
				response.setMessage("session is alive");
				return new ResponseEntity<BaseResponse>(response,HttpStatus.ACCEPTED);
			}else if (result==false) {
				response.setStatus(HttpStatus.NOT_ACCEPTABLE);
				response.setMessage("your session is no longer alive..");
				return new ResponseEntity<BaseResponse>(response,HttpStatus.NOT_ACCEPTABLE);
			}
		
			return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);

		}
		return null;

		
	}
	
	@RequestMapping(value="resetPassword", method = RequestMethod.POST )
	public ResponseEntity<BaseResponse> resetPassword(@RequestParam("password")String password,@RequestParam("uuid")String uuid) {
		BaseResponse response = new BaseResponse();
	
			boolean result = service.changePassword(uuid, password);
			if(result) {
				response.setMessage("your password is saved sucessfully");
				response.setStatus(HttpStatus.OK);
				return new ResponseEntity<BaseResponse>(response,HttpStatus.OK);
			}else {
				response.setMessage("some thing went wrong");
				response.setStatus(HttpStatus.BAD_REQUEST);
				return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
				
			}
		
	}
		
		
	@RequestMapping(value="forgotPassword", method = RequestMethod.POST ,produces="application/json" )
	public ResponseEntity<BaseResponse> conformationMail(@RequestParam("email")String email) {
		System.out.println("in conformation");
		boolean result =service.sendConformationMail(email);
		BaseResponse response = new BaseResponse();
		if(result==true) {
			response.setMessage("message sent");
			response.setStatus(HttpStatus.OK);
			return new ResponseEntity<BaseResponse>(response,HttpStatus.OK);
			
		}else {
			
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessage("please check your mail id properly");
			return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
		}

	}
	
	/*@RequestMapping(value="reset-password/{key}", method = RequestMethod.GET ,produces="application/json" )
	public ResponseEntity<BaseResponse> resetPassword(@PathVariable String key) {
		
		System.out.println(key+" from resetPassword");
		boolean result = service.checkSessionPassword(key);
		
		if(result==true) {
			response.setStatus(HttpStatus.ACCEPTED);
			response.setMessage("session is alive");
			return new ResponseEntity<BaseResponse>(response,HttpStatus.ACCEPTED);
		}else if (result==false) {
			response.setStatus(HttpStatus.NOT_ACCEPTABLE);
			response.setMessage("your session is no longer alive..");
			return new ResponseEntity<BaseResponse>(response,HttpStatus.NOT_ACCEPTABLE);
		}
	
		return new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);

	}
	*/

	
}
