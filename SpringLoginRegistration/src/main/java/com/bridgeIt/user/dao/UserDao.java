package com.bridgeIt.user.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.bridgeIt.user.model.User;
@Component
public class UserDao {

	@Autowired
	private DataSource dataSource;
	
	private JdbcTemplate template;
	
	public DataSource getDataSource() {
		return dataSource;
	}
	
	public boolean insert(User user) {
		
		Object [] args = {user.getEmail(),user.getName(),user.getMobileNo(),user.getCity(),user.getRole()};
		template = new JdbcTemplate(dataSource);
		System.out.println(dataSource);
		int out=0;
		try {
			out = template.update("insert into UserLogin(email,name,mobileNo,city,role) values (?,?,?,?,?)", args);
			System.out.println("number rows affected "+out);
			return true;
		} catch (DataAccessException e) {

			System.out.println("template not excuted..");
			return false;
		}

	}
	
	public boolean existence (User user) {
		
		template = new JdbcTemplate(dataSource);
		System.out.println(user);
		Object [] args = {user.getEmail()};
		
		try {
			template.queryForObject("select * from UserLogin where email=?", User.class, args);
			System.out.println("template not excuted..");
			
			return false;
		} catch (DataAccessException e) {

			
			return true;
		}
		
	}

	public boolean checkUser(String email, String password ) {
		
		Object [] args = {email,password};
		try {
		template.queryForObject("select * from UserLogin where email=? and password=?", User.class,args );
			
			return true;
		} catch (DataAccessException e) {
			
			return false;
		}
		
	}
	
	
}
