package com.require4testing.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.require4testing.model.User;
import com.require4testing.service.UserService;



@Controller
@RequestMapping("/")
public class UserController {
	@Autowired
	private UserService service;
	
	@GetMapping("")
	public String loadSelectForm(Model model) {
		List<User> users = service.alleEntities();
		model.addAttribute("users", users);
		return "welcome";
		
	}
}