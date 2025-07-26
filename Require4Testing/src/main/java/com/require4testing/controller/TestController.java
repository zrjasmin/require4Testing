package com.require4testing.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.require4testing.dto.*;
import com.require4testing.model.Anforderung;
import com.require4testing.model.Test;
import com.require4testing.model.Testschritt;
import com.require4testing.model.User;
import com.require4testing.repository.AnforderungRepository;
import com.require4testing.repository.TestRepository;
import com.require4testing.repository.TestschrittRepository;
import com.require4testing.service.AnforderungService;
import com.require4testing.service.TestService;
import com.require4testing.service.UserService;

@Controller
@RequestMapping("/test")
public class TestController {
	@Autowired
    private  TestService service;
	@Autowired
    private  TestRepository repository;
	@Autowired
	private TestschrittRepository schrittRepository;
	@Autowired
	private AnforderungRepository anforderungRepository;
	@Autowired
	private AnforderungService anfService;
	@Autowired
	private UserService userService;

	

	@GetMapping("/all") 
	public String alleTests(Model model, HttpSession session) {
		model.addAttribute("test", repository.findAll());
		
		User user = (User) session.getAttribute("currentUser");		
		model.addAttribute("aktuellerUser", user);
		
		return "test_uebersicht";
	}
	
	@GetMapping("/edit")
	public String zeigeNeueTestForm(Model model, HttpSession session) {
		userService.hasPermision(session, "create_test");
		model.addAttribute("currentUser", userService.getCurrentUser(session));
		
		model.addAttribute("test", new Test());
		List<Anforderung> anforderungen = anfService.alleEntities();
		model.addAttribute("anforderungen", anforderungen);
		return "test_neu";
	}
	
	@GetMapping("/detail/{id}")
	public String zeigDetailForm(@PathVariable Long id, Model model) {
		Test test = service.getTestById(id);
		model.addAttribute("test", test);
		
		
		if(test.getTestschritte() != null) {
			List<Testschritt> sortierteSchritte = schrittRepository.findByTestOrderByStepNumberAsc(test);
			model.addAttribute("schritte", sortierteSchritte);
		}
		
		
		
		model.addAttribute("testNummer", service.generateTestNumber(test));
		return "test_detail";
	}
	
	@GetMapping("/edit/{id}")
	public String zeigEditForm(@PathVariable Long id, Model model, HttpSession session) {

		userService.hasPermision(session, "edit_test");
		model.addAttribute("currentUser", userService.getCurrentUser(session));
	

		Test test = service.getTestById(id);
		model.addAttribute("test", test);
		
		List<Anforderung> anforderungen = anforderungRepository.findAll();
		model.addAttribute("anforderungen", anforderungen);
		

		TestDto dto = service.convertToDto(id);
		model.addAttribute("testDto", dto);


		return "test_bearbeiten";
	}
	
	
	
	
	
	@PostMapping("/save") 
	public String neuenTestSpeichern(@ModelAttribute Test test, 
			@RequestParam("reihenfolge") String reihenfolgeJSON, 
			@RequestParam("erstellerId") Long erstellerId) {
				
		service.neuenTestSpeichern(test, reihenfolgeJSON, erstellerId);
      
		return "redirect:/test/all";
	}
	
	
	
	
	
	@Transactional
	@PostMapping("/update/{id}")
	public String updateTest(@PathVariable Long id, Model model, @ModelAttribute TestDto testDto, @RequestParam("reihenfolge") String reihenfolgeJSON) {
		
		Test updatedTest = service.updateTest(id, testDto, reihenfolgeJSON);
		model.addAttribute("test", updatedTest);

		return "redirect:/test/detail/"+id;
	}
	
	@PostMapping("/delete/{id}")
	public String deleteTest(@PathVariable Long id, HttpSession session, Model model) {
		userService.hasPermision(session, "delete_test");
		model.addAttribute("currentUser", userService.getCurrentUser(session));
		
		service.deleteTest(id);
		
		return "redirect:/test/all";
	}
	
	
	
}
