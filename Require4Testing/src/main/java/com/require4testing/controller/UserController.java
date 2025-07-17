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
		/*User user1 = new User("Jasmin", "email@gmx.net", Role.RE, "/images/profiles/pic1.png");
		User user2 = new User("Lukas", "email@gmx.net", Role.TEST_FALLERSTELLR, "/images/profiles/pic2.png");
		User user3 = new User("Laura", "email@gmx.net", Role.TEST_MANAGER, "/images/profiles/pic3.png");
		User user4 = new User("Lena", "email@gmx.net", Role.TESTER, "/images/profiles/pic4.png");
		User user5 = new User("Thomas", "email@gmx.net", Role.TESTER, "/images/profiles/pic5.png");
		User user6 = new User("Niklas", "email@gmx.net", Role.RE, "/images/profiles/pic6.png");
		
		repository.save(user1);
		repository.save(user2);
		repository.save(user3);
		repository.save(user4);
		repository.save(user5);
		repository.save(user6);*/
		
		//User aus DB laden
		List<User> users = service.alleEntities();
		model.addAttribute("users", users);
	
		
		return "welcome";
		
	}
	
	
	@PostMapping("/user/selectRole")
	public String handleSelectRole(@RequestParam("selectedUserId") Long userId, HttpSession session) {
		session.setAttribute("currentUser", service.findById(userId));
		
		return "redirect:/test/all";
		
	    // Hier kannst du den User anhand der ID laden und weiterverarbeiten
	}
	
	
	
	
	
	
	
	
	
	
	
}