package com.cinemas.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cinemas.dao.UserDao;
import com.cinemas.domain.User;
import com.cinemas.service.UserService;


@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;

	@Override
	public User getById(int userId) {
		return userDao.getUserById(userId);
	}

}
