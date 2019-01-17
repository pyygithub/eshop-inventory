package com.wolf.eshop.inventory.controller;

import com.wolf.eshop.inventory.model.User;
import com.wolf.eshop.inventory.service.UserService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * 用户Controller控制器
 * @author Administrator
 *
 */
@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/getUserInfo")
	public User getUserInfo() {
		User user = userService.findUserInfo();
		return user;
	}
	
	@GetMapping("/getCachedUserInfo")
	public User getCachedUserInfo() {
		User user = userService.getCachedUserInfo();
		return user;
	}
	
}
