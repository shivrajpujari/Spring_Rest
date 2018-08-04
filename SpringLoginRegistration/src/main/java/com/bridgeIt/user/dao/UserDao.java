package com.bridgeIt.user.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import com.bridgeIt.user.model.User;
@Repository
public class UserDao {

	@Autowired
	private DataSource dataSource;
	
	private JdbcTemplate template;
	
	public DataSource getDataSource() {
		return dataSource;
	}
	
	public boolean insert(User user) {
		
		
		template = new JdbcTemplate(dataSource);
		
		String hashPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		
		Object [] args = {user.getEmail(),user.getName(),hashPassword,user.getMobileNo(),user.getCity(),user.getRole(),user.isVerified(),user.getAuthenticatedUserKey()};
		System.out.println(dataSource);
		System.out.println(hashPassword);
		int out=0;
		try {
			System.out.println(user);
			out = template.update("insert into UserLogin(email,name,password,mobileNo,city,role,verified,authenticated_user_key) values (?,?,?,?,?,?,?,?)", args);
			System.out.println("number rows affected "+out);
			return true;
		} catch (DataAccessException e) {
			e.printStackTrace();
			System.out.println("insert template not excuted..");
			return false;
		}

	}
	
	public boolean existence (User user) {
		

		template = new JdbcTemplate(dataSource);
		System.out.println(user);
		Object [] args = {user.getEmail()};
		String sql = "select name from UserLogin where email = ?";
		
		try {
			
			String name = (String)template.queryForObject(
					sql, new Object[] { user.getEmail()}, String.class);
			
			System.out.println(name);
			System.out.println("abcd");
			return false;
		} catch (DataAccessException e) {
			e.printStackTrace();
			System.out.println(user);
			return true;
		}
		
	}
	
	
	public boolean presence (User user) {

		template = new JdbcTemplate(dataSource);
		System.out.println(user);
		Object [] args = {user.getEmail()};
		String sql="select * from UserLogin where email=?";
		
		List<User> users=template.query(sql, args, new UserMapper());
		if(users.isEmpty()!=true) {
			System.out.println(" presence returning true");
			return true;
		}
		return false;
		
	}
	

	public boolean checkUser(String email, String password ) {
	System.out.println(email+"---"+password);
	Object [] args = {email};
	template = new JdbcTemplate(dataSource);
	String sql="select * from UserLogin where email=?";
	
	List<User> user=template.query(sql, args, new UserMapper());
	
	if(user.isEmpty()!=true ) {
		User user1=user.get(0);
		if(BCrypt.checkpw(password, user1.getPassword())) {
			System.out.println(true);
			return true;
		}
		return false;
	}else {
		return false;
	} 
	
	}
	
	
/*	public boolean getVerified(String email ) {
		
		template =new JdbcTemplate(dataSource);
		boolean verified=true;
		Object [] args = {verified,email};
		System.out.println(email+"--------id");
		String sql="update UserLogin set verified = ? where email = ?";
		try {
		int res=template.update(sql, args);
		System.out.println(res);
		} catch (Exception e) {		
			e.printStackTrace();
			return false;
		}
		
		return true;
	}*/
	
	public boolean getVerified(String uniqueId) {
		
		template = new JdbcTemplate(dataSource);
		boolean verified = true;
		Object [] args= {verified,uniqueId};
		
		String sql = "update UserLogin set verified = ? where authenticated_user_key = ?";
		try {
			int res=template.update(sql, args);
			System.out.println(res);
			
			if(res==1) {
				return true;
			}else {
				return false;
			}
			} catch (Exception e) {		
				e.printStackTrace();
				return false;
			}
	}
	
	
	public User fetchUserByEmail(String email) {
	
		Object [] args = {email};
		template = new JdbcTemplate(dataSource);
		String sql="select * from UserLogin where email=?";
		List<User> user=template.query(sql, args, new UserMapper());
		User user1=user.get(0);
		return user1;
	}
	
	public boolean resetPassword(String uuid , String newPassword) {
		Object[] args = {uuid,newPassword};
		
		String sql="update UserLogin set password = ? where uuid=?";
		template= new JdbcTemplate(dataSource);
		try {
			int res=template.update(sql, args);
			System.out.println(res);
			
			if(res==1) {
				return true;
			}else {
				return false;
			}
			} catch (Exception e) {		
				e.printStackTrace();
				return false;
			}
	}
	
	
	public String getUUid(String email) {
		
		Object[] args = {email};
		
		String sql = "select authenticated_user_key from UserLogin where email = ?";
		template= new JdbcTemplate(dataSource);
		
		List<String> userIds = null;
		userIds = template.queryForList(sql, String.class, args);
		System.out.println(userIds);
		
		if(userIds.isEmpty()) {
			System.out.println(true);
			return null;
		}
		String uuid=userIds.get(0);
		System.out.println(uuid);
		return uuid;
	}
	
	public void insertForgotPassword(String uuid,String email) {
		
		java.util.Date date = new java.util.Date();
        long t = date.getTime();
        long t1 = t+720000;
        template= new JdbcTemplate(dataSource);
        System.out.println(t);
        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(t);
        java.sql.Timestamp sqlTimestamp1 = new java.sql.Timestamp(t1);
		
		Object[] args = {uuid,email,sqlTimestamp,sqlTimestamp1};
		
		String sql ="insert into resetPassword (authenticated_user_key,email,starting_interval,ending_interval) values (?,?,?,?) ";
		
		int rows=template.update(sql, args);
		System.out.println(rows);
		
		
	}
	
	
}
