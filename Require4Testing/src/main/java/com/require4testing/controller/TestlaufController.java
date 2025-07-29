package com.require4testing.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.require4testing.dto.TestDto;
import com.require4testing.dto.TestlaufDto;
import com.require4testing.model.Anforderung;
import com.require4testing.model.Testlauf;
import com.require4testing.service.TestService;
import com.require4testing.service.TestlaufService;
import com.require4testing.service.UserService;

@Controller
@RequestMapping("/testlauf")
public class TestlaufController {
	
	private  UtilController util = new UtilController();
	
	@Autowired 
	private TestlaufService service;
	@Autowired
	private UserService userService;
	@Autowired
	private TestService testService;
	
	
	@GetMapping("/all")
	public String alleTestlaeufe(Model model) {
		model.addAttribute("testlaeufe", service.alleEntities());
		util.setPageModelAttributes(model, "Testläufe", "testrun_uebersicht", "","/css/uebersicht.css", "");
		return "layout";
	}
	
	@GetMapping("/edit")
	public String showNeuForm(Model model, HttpSession session) {
		userService.hasPermision(session, "create_testlauf");
		model.addAttribute("currentUser", userService.getCurrentUser(session));
		model.addAttribute("testlauf", new Testlauf());
		model.addAttribute("status", service.alleStatus());
		model.addAttribute("testerListe", userService.getAllOfRole("Tester:in"));
		model.addAttribute("tests", testService.alleEntities());
		util.setPageModelAttributes(model, "Testlauf: Neu", "testrun_neu", "/js/testlauf.js","/css/form.css", "");
		
		return "layout";
	}
	
	
	
	@GetMapping("/detail/{id}")
	public String showDetailForm(@PathVariable Long id, Model model) {
		Testlauf testlauf = service.getTestlaufById(id);
		model.addAttribute("testlauf", testlauf);
		util.setPageModelAttributes(model, "Testlauf: Detail", "testrun_detail", "","/css/form.css", "");
		return "layout";
	}
	
	
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
		userService.hasPermision(session, "edit_testlauf");
		model.addAttribute("currentUser", userService.getCurrentUser(session));
		
		Testlauf testlauf = service.getTestlaufById(id);
		model.addAttribute("testlauf", testlauf);
		
		TestlaufDto dto = service.convertToDto(id);
		model.addAttribute("testlaufDto", dto);
		
		util.setPageModelAttributes(model, "Testlauf: Edit", "testrun_edit", "","/css/form.css", "");
		return "layout";
	}
	
	
	
	@PostMapping("/save")
	public String saveTestlauf(@ModelAttribute Testlauf testlauf, 
			@RequestParam("erstellerId") Long erstellerId,
			@RequestParam("checkedInputs") String verknüpfteTestId) {
		service.saveNewTestlauf(testlauf, erstellerId, verknüpfteTestId);
		System.out.println(testlauf.getStatus());
		return "redirect:/testlauf/all";
	}
}























