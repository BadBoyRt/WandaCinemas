package com.thtf.cinemas.dao;

import java.util.List;

import com.thtf.cinemas.domain.User;

public interface UserDao {

	List<User> selectAllUser();
}
