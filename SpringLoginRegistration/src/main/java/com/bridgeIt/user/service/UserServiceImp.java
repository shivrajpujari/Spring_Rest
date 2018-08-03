package com.bridgeIt.user.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bridgeIt.user.BaseResponse;
import com.bridgeIt.user.dao.UserDao;
import com.bridgeIt.user.model.User;
import com.bridgeIt.user.service.utility.JwtToken;
import com.bridgeIt.user.service.utility.RabbitMsgSender;
import com.bridgeIt.user.service.utility.UserMail;

@Service
public class UserServiceImp implements UserService {

	
	@Autowired
	UserDao dao;
	
	@Autowired
	BaseResponse response;
	
	
	@Autowired
	JwtToken token;
	
	
	@Autowired
	RabbitMsgSender sender;
	
	@Autowired
	UserMail mail;
	
	@Override
	public BaseResponse userReg(User user) {
	
		boolean outcome=dao.presence(user);
		if(outcome!=true) {
			System.out.println("after presence");
			String uuid=UUID.randomUUID().toString();
			user.setAuthenticatedUserKey(uuid);
			dao.insert(user);
			
			String url="http://localhost:8080/user/verify/";
			String verificationUrl=url+uuid;
			mail.setTo(user.getEmail());
			mail.setMessage(verificationUrl);
			sender.sendMsg(mail);
			
			response.setStatus(HttpStatus.OK);
			response.setMessage("Your are registered");
			response.setErrors(null);
			return response;
			
		}
		
		response.setStatus(HttpStatus.BAD_REQUEST);
		response.setMessage("User already exist");
		response.setErrors(null);
		
		
		return response;
	}

	@Override
	public boolean login(String email, String password) {

		boolean outcome=dao.checkUser(email, password);
		if (outcome) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean verify(String uniqueId) {
		
		boolean result = dao.getVerified(uniqueId);
		
		
		return result;
		
	}

	
	
	
}
