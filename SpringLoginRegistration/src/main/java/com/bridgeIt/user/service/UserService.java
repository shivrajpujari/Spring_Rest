package com.bridgeIt.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.bridgeIt.user.BaseResponse;
import com.bridgeIt.user.dao.UserDao;
import com.bridgeIt.user.model.User;

@Component
public class UserService {

	@Autowired
	UserDao dao;

	@Autowired
	BaseResponse response;
	public ResponseEntity<BaseResponse> userReg (User user) {
		
		
		boolean outcome=dao.existence(user);
		ResponseEntity<BaseResponse> respond;
		if(outcome) {
			dao.insert(user);
			response.setCode(HttpStatus.OK);
			response.setStatus("your are registered");
			respond = new ResponseEntity<BaseResponse>(response,HttpStatus.OK);
			return respond;

		}else {
			response.setCode(HttpStatus.BAD_REQUEST);
			response.setStatus("user already exist");
			respond = new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
			return respond;
		}
		
	}
	
	public ResponseEntity<BaseResponse> login ( String email , String password ){
		
		boolean outcome=dao.checkUser(email, password);
		ResponseEntity<BaseResponse> respond;
		if(outcome) {
			
			response.setCode(HttpStatus.OK);
			response.setStatus("your are logged in");
			respond = new ResponseEntity<BaseResponse>(response,HttpStatus.OK);
			return respond;

		}else {
			
			response.setCode(HttpStatus.BAD_REQUEST);
			response.setStatus("Invalid email or password");
			respond = new ResponseEntity<BaseResponse>(response,HttpStatus.BAD_REQUEST);
			return respond;
		}
		
		
	}
}
