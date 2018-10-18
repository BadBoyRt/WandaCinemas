package com.cinemas.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cinemas.domain.User;


public interface UserDao {

	List<User> selectAllUser();
	
	User getUserById(@Param("userId")int userId);
}
