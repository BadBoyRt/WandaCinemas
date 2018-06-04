package com.thtf.cinemas.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thtf.cinemas.dao.UserDao;
import com.thtf.cinemas.domain.User;
import com.thtf.cinemas.service.UserService;
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	@Override
	public List<User> selectAllUsers() {
		List<User> list = userDao.selectAllUser();
		if(list != null && list.size() > 0) {
			return list;
		}
		return null;
	}
}
