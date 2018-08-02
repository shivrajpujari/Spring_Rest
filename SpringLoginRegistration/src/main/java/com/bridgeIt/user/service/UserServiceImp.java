package com.bridgeIt.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bridgeIt.user.BaseResponse;
import com.bridgeIt.user.dao.UserDao;
import com.bridgeIt.user.model.User;
import com.bridgeIt.user.service.utility.JwtToken;
import com.bridgeIt.user.service.utility.RabbitMsgSender;


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
	
	
	@Override
	public BaseResponse userReg(User user) {
	
		boolean outcome=dao.presence(user);
		if(outcome!=true) {
			System.out.println("after presence");
			dao.insert(user);
			long expTime=72000;
			int id=user.getId();
			String tokenId=Integer.toString(id);
			String jwtToken = token.createJwt(tokenId, user.getEmail(),expTime);
			String url="http://192.168.0.68:8080/user/verify/";
			String verificationUrl=url+jwtToken;
			sender.sendMsg(verificationUrl);
			response.setCode(HttpStatus.OK);
			response.setStatus("your are registered");
			response.setErrors(null);
			return response;
			
		}
		
		response.setCode(HttpStatus.BAD_REQUEST);
		response.setStatus("user already exist");
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
	public void verify(String token) {
	
		
		
	}

}
