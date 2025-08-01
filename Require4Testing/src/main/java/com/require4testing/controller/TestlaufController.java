package com.require4testing.controller;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.require4testing.dto.TestDto;
import com.require4testing.dto.TestlaufDto;
import com.require4testing.model.Anforderung;
import com.require4testing.model.Test;
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
	public String showEditForm(@PathVariable Long id, 
			Model model, 
			HttpSession session) {
		userService.hasPermision(session, "edit_testlauf");
		
		TestlaufDto dto = service.convertToDto(id);
		model.addAttribute("currentUser", userService.getCurrentUser(session));
		
		
		model.addAttribute("testlauf", service.getTestlaufById(id));
		model.addAttribute("testlaufDto", dto);
		
		
		model.addAttribute("status", service.alleStatus());
		model.addAttribute("testerListe", userService.getAllOfRole("Tester:in"));
		
		
		Set<Test> tests = new HashSet<>();
		for(Long testId : dto.getTests()) {
			Test test = testService.getTestById(testId);
			tests.add(test);
		}
		model.addAttribute("alleTests", testService.alleEntities());

		
		model.addAttribute("selectedTests",tests);
		
		util.setPageModelAttributes(model, "Testlauf: Edit", "testrun_edit", "/js/testlauf.js","/css/form.css", "");
		return "layout";
	}
	
	@PostMapping("/handleForm/{id}")
	public String handleForm(@PathVariable Long id, 
    		@ModelAttribute TestlaufDto testlaufDto,
    		@RequestParam String action,
    		@RequestParam("checkedInputs") String verknüpfteTestId,
    		BindingResult result,
    		
    		HttpSession session,
    		Model model) {
		try {
			if("speichern".equals(action)) {
				updateTestlauf(id,model, testlaufDto, verknüpfteTestId, session);	
			} else if("loeschen".equals(action)) {
				deleteTeslauf(id,session, model);
			}  
			  return "redirect:/testlauf/all";

		} catch(IllegalArgumentException e) {
			System.out.println("fehler in handleForm");
			model.addAttribute("errorMessage", e.getMessage());
			TestlaufDto dto = service.convertToDto(id);

			model.addAttribute("currentUser", userService.getCurrentUser(session));
			
			
			model.addAttribute("testlauf", service.getTestlaufById(id));
			model.addAttribute("testlaufDto", dto);
			
			
			model.addAttribute("status", service.alleStatus());
			model.addAttribute("testerListe", userService.getAllOfRole("Tester:in"));
			
			
			Set<Test> tests = new HashSet<>();
			for(Long testId : dto.getTests()) {
				Test test = testService.getTestById(testId);
				tests.add(test);
			}
			model.addAttribute("alleTests", testService.alleEntities());

			
			model.addAttribute("selectedTests",tests);
			util.setPageModelAttributes(model, "Testlauf: Edit", "testrun_edit", "/js/testlauf.js","/css/form.css", "");

			return "layout";
		}
		
	}
	
	
	
	@PostMapping("/save")
	public String saveTestlauf(@ModelAttribute Testlauf testlauf, 
			@RequestParam("erstellerId") Long erstellerId,
			@RequestParam("checkedInputs") String verknüpfteTestId,
			@RequestParam(name= "testerId", required = false) Long testerId) {
		System.out.println(testerId);
		service.saveNewTestlauf(testlauf, erstellerId, verknüpfteTestId, testerId);
		
		return "redirect:/testlauf/all";
	}
	
	@PostMapping("/edit/{id}")
	public String updateTestlauf(
			@PathVariable Long id, 
			Model model, 
			@ModelAttribute TestlaufDto testlaufDto,  
			String verknüpfteTestId,
			 HttpSession session
			) {
		
		
		userService.hasPermision(session, "edit_testlauf");
		Testlauf testlauf = null;
		testlauf = service.updateTestlauf(id,testlaufDto, verknüpfteTestId);
		model.addAttribute("testlauf", testlauf);
		return "redirect:/testlauf/detail/"+id;
		
	}
	
	@PostMapping("/delete/{id}") 
	public String deleteTeslauf(@PathVariable Long id, HttpSession session, Model model) {
		userService.hasPermision(session, "delete_testlauf");
		service.deleteTestlauf(id);
		
		
		
		return "redirect:/testlauf/all";
	}
}























