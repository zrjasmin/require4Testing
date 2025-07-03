package com.require4testing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.require4testing.model.Test;
import com.require4testing.repository.AnforderungRepository;
import com.require4testing.repository.TestRepository;
import com.require4testing.service.AnforderungService;
import com.require4testing.service.TestService;

@Controller
@RequestMapping("/test")
public class TestController {
	@Autowired
    private  TestService service;
	@Autowired
    private  TestRepository repository;
	
	

	@GetMapping("/all") 
	public String alleTests(Model model) {
		model.addAttribute("test", repository.findAll());
		return "test_uebersicht";
	}
	
	@GetMapping("/edit")
	public String zeigeNeueTestForm(Model model) {
		model.addAttribute("test", new Test());
		return "test_neu";
	}
	
	@GetMapping("/detail/{id}")
	public String zeigDetailForm(@PathVariable Long id, Model model) {
		Test test = service.getTestById(id);
		model.addAttribute("test", test);
		return "test_detail";
	}
	
	@GetMapping("/edit/{id}")
	public String zeigEditForm(@PathVariable Long id, Model model) {
		Test test = service.getTestById(id);
		model.addAttribute("test", test);
		return "test_bearbeiten";
	}
	
	@PostMapping("/save") 
	public String neuenTestSpeichern(@ModelAttribute Test test) {
		service.speichereEntity(test);
		
		return "redirect:/test/all";
	}
	
	@PostMapping("/update/{id}")
	public String updateTest(@PathVariable Long id,@ModelAttribute Test test) {
		Test bestehenderTest = service.getTestById(id);
		
		bestehenderTest.setTitle(test.getTitle());
		bestehenderTest.setBeschreibung(test.getBeschreibung());
		repository.save(bestehenderTest);
		
		return "redirect:/test/detail/"+id;
	}
	
}
