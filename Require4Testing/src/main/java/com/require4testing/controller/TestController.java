package com.require4testing.controller;

import java.util.Arrays;
import java.util.List;

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
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;
import com.require4testing.model.Anforderung;
import com.require4testing.model.Test;
import com.require4testing.model.Testschritt;
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
	@Autowired
	private AnforderungRepository anforderungRepository;
	@Autowired
	private AnforderungService anfService;
	
	

	@GetMapping("/all") 
	public String alleTests(Model model) {
		model.addAttribute("test", repository.findAll());
		return "test_uebersicht";
	}
	
	@GetMapping("/edit")
	public String zeigeNeueTestForm(Model model) {
		model.addAttribute("test", new Test());
		List<Anforderung> anforderungen = anforderungRepository.findAll();
		model.addAttribute("anforderungen", anforderungen);
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
		List<Anforderung> anforderungen = anforderungRepository.findAll();
		model.addAttribute("anforderungen", anforderungen);
		return "test_bearbeiten";
	}
	
	@PostMapping("/save") 
	public String neuenTestSpeichern(@ModelAttribute Test test, @RequestParam("reihenfolge") String reihenfolgeJSON) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
            List<String> reihenfolgeListe = mapper.readValue(reihenfolgeJSON, new TypeReference<List<String>>() {});
            
            int i = 1;
    
            for(String s : reihenfolgeListe) {
            	int stellenIndex = Integer.parseInt(s);
            	// sucht Testschritt anhand der Reihenfolge
            	Testschritt currentSchritt = test.getTestschritte().get(stellenIndex);
            	//setzt die SchrittNumme richtig
            	currentSchritt.setStepNumber(i);
            	currentSchritt.setTest(test);
            	i++;
           
            }
            
            
		} catch(Exception e) {
			 e.printStackTrace();
		}
		
		
		Long anfID = test.getAnforderung().getId();
		
		Anforderung anf = anfService.getAnfById(anfID);
		
		test.setAnforderung(anf);
		
		repository.save(test);
		
		return "redirect:/test/all";
	}
	
	@PostMapping("/update/{id}")
	public String updateTest(@PathVariable Long id,@ModelAttribute Test test) {
		Test bestehenderTest = service.getTestById(id);
		
		bestehenderTest.setTitle(test.getTitle());
		bestehenderTest.setBeschreibung(test.getBeschreibung());
		bestehenderTest.setAnforderung(test.getAnforderung());
		repository.save(bestehenderTest);
		
		return "redirect:/test/detail/"+id;
	}
	
}
