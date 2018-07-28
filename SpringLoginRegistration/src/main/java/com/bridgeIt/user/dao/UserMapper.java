package com.bridgeIt.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.bridgeIt.user.model.User;

public class UserMapper implements RowMapper<User>{

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setName(rs.getString("name"));
		user.setCity(rs.getString("city"));
		user.setEmail(rs.getString("email"));
		user.setMobileNo(rs.getString("mobileNo"));
		user.setRole(rs.getString("role"));
		user.setPassword(rs.getString("password"));
		
		return user;
	}

}
