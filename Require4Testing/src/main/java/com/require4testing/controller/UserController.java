package com.require4testing.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.require4testing.model.Role;
import com.require4testing.model.User;
import com.require4testing.repository.UserRepository;
import com.require4testing.service.UserService;



@Controller
@RequestMapping("/")
public class UserController {
	@Autowired
	private UserService service;
	@Autowired
	private UserRepository repository;
	
	
	@GetMapping("")
	public String loadSelectForm(Model model) {
		service.initialiseUser();
		
		//User aus DB laden
		List<User> users = service.alleEntities();
		model.addAttribute("users", users);
	
		
		return "welcome";
		
	}

	
	
	@PostMapping("/user/selectRole")
	public String handleSelectRole(@RequestParam("selectedUserId") Long userId, HttpSession session) {
		User user = service.findById(userId);
	
		session.setAttribute("currentUser", user);
		
		return "redirect:/anforderung/all";
		
	    // Hier kannst du den User anhand der ID laden und weiterverarbeiten
	}
	
	
	
	
	
	
	
	
	
	
	
}