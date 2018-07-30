package com.bridgeIt.user;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Scope(value="prototype")
public class BaseResponse {

	private HttpStatus code;
	private String status;
	private List errors;
	
	public List getErrors() {
		return errors;
	}
	public void setErrors(List errors) {
		this.errors = errors;
	}
	public HttpStatus getCode() {
		return code;
	}
	public void setCode(HttpStatus ok) {
		this.code = ok;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
