package com.require4testing.controller;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
		util.setPageModelAttributes(model, "Testläufe", "testrun_uebersicht", "","/css/uebersicht.css", "/css/testlauf.css");
		return "layout";
	}
	
	@GetMapping("/edit")
	public String showNeuForm(Model model, HttpSession session) {
		userService.hasPermision(session, "create_testlauf");
		model.addAttribute("currentUser", userService.getCurrentUser(session));
		model.addAttribute("testlaufDto", new TestlaufDto());
		model.addAttribute("status", service.alleStatus());
		model.addAttribute("testerListe", userService.getAllOfRole("Tester:in"));
		model.addAttribute("alleTests", testService.alleEntities());
		util.setPageModelAttributes(model, "Testlauf: Neu", "testrun_form", "/js/testlauf.js","/css/form.css", "/css/testlauf.css");
		
		return "layout";
	}
	
	
	
	@GetMapping("/detail/{id}")
	public String showDetailForm(@PathVariable Long id, Model model) {
		Testlauf testlauf = service.getTestlaufById(id);
		model.addAttribute("testlauf", testlauf);
		util.setPageModelAttributes(model, "Testlauf: Detail", "testrun_detail", "","/css/form.css", "/css/testlauf.css");
		return "layout";
	}
	
	
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, 
			Model model, 
			HttpSession session) {
		userService.hasPermision(session, "edit_testlauf");
		
		TestlaufDto dto = service.convertToDto(id);
		model.addAttribute("currentUser", userService.getCurrentUser(session));
		
		Testlauf testlauf = service.getTestlaufById(id);
		model.addAttribute("testlauf", testlauf);
		model.addAttribute("testlaufDto", dto);
		
		
		model.addAttribute("status", service.alleStatus());
		model.addAttribute("testerListe", userService.getAllOfRole("Tester:in"));
		
		System.out.println(dto.getId());
		if(testlauf.getTests() != null) {
			Set<Long> verküpfteIds = new HashSet<>();
			for(Test test : testlauf.getTests()) {
				verküpfteIds.add(test.getId());
				
			}
			dto.setTestIds(verküpfteIds);
			model.addAttribute("selectedTests",testlauf.getTests());
		}
		
		
		
		model.addAttribute("alleTests", testService.alleEntities());

		
		
		
		util.setPageModelAttributes(model, "Testlauf: Edit", "testrun_form", "/js/testlauf.js","/css/form.css", "/css/testlauf.css");
		return "layout";
	}
	
	@PostMapping("/handleForm")
	public String handleForm(
    		@ModelAttribute("testlaufDto") @Valid TestlaufDto testlaufDto,
    		BindingResult result,
    		@RequestParam String action,
    		@RequestParam("erstellerId") Long erstellerId,
    		HttpSession session,
    		Model model) {
		
		
		System.out.println("testlaufDto.getTester(): "+ testlaufDto.getTester().getId());
		if (service.checkTesterAuswahl(testlaufDto)) {
	        // Verbotenes Verhalten erkannt -> Fehler hinzufügen
	        result.rejectValue("tester", "error.tester", "Der Tester kann nicht entfernt werden, wenn er bereits gesetzt ist.");
	    }
		
		if(result.hasErrors()) {
			System.out.println("fehler");
			System.out.println(testlaufDto.getCheckedInputs());
			System.out.println(testlaufDto.getCheckedInputs());
			for (FieldError error : result.getFieldErrors()) {
	            System.err.println("Fehler im Feld '" + error.getField() + "': " + error.getDefaultMessage());
	        }
			userService.hasPermision(session, "edit_testlauf");
			model.addAttribute("currentUser", userService.getCurrentUser(session));
			
			model.addAttribute("testlaufDto", testlaufDto);
			model.addAttribute("status", service.alleStatus());
			model.addAttribute("alleTests", testService.alleEntities());
			model.addAttribute("testerListe", userService.getAllOfRole("Tester:in"));
			
			
			Set<Test> tests = testService.findTestByString(testlaufDto.getCheckedInputs());
			model.addAttribute("selectedTests",tests);
			
			if(testlaufDto.getId() != null) {
				model.addAttribute("testlauf", service.getTestlaufById(testlaufDto.getId()));
			}
			
			
			util.setPageModelAttributes(model, "Testlauf: Edit", "testrun_form", "/js/testlauf.js","/css/form.css", "/css/testlauf.css");
			return "layout";
		}
		
			if("speichern".equals(action)) {
				if(testlaufDto.getId() == null) {
					saveTestlauf(testlaufDto, erstellerId);
				} else {
					updateTestlauf(model, testlaufDto,  session);
				}
					
			} else if("loeschen".equals(action)) {
				deleteTeslauf(testlaufDto.getId(),session, model);
			}  
			  return "redirect:/testlauf/all";

		} 
		
	
	

	
	
	
	@PostMapping("/save")
	public String saveTestlauf(
			TestlaufDto testlaufdto, 
			Long erstellerId) {
		
		service.saveNewTestlauf(testlaufdto, erstellerId);
		
		return "redirect:/testlauf/all";
	}
	
	@PostMapping("/edit/{id}")
	public String updateTestlauf(
			Model model, 
			TestlaufDto testlaufDto,  
			HttpSession session
			) {
		
		
		userService.hasPermision(session, "edit_testlauf");
		Testlauf testlauf = null;
		testlauf = service.updateTestlauf(testlaufDto);
		model.addAttribute("testlauf", testlauf);
		return "redirect:/testlauf/detail/"+testlaufDto.getId();
		
	}
	
	@PostMapping("/delete/{id}") 
	public String deleteTeslauf(@PathVariable Long id, HttpSession session, Model model) {
		userService.hasPermision(session, "delete_testlauf");
		service.deleteTestlauf(id);
		
		
		
		return "redirect:/testlauf/all";
	}
}























