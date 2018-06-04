package com.thtf.cinemas.controllor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thtf.cinemas.domain.User;
import com.thtf.cinemas.service.UserService;

@Controller
public class UserControllor {
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/users")
	public String selectAllUsers(HttpServletRequest request) {
		List<User> list = userService.selectAllUsers();
		request.setAttribute("list", list);
		return "user/userList";
	}

}
