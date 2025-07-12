package com.require4testing.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.require4testing.repository.AnforderungRepository;
import com.require4testing.repository.TestRepository;
import com.require4testing.repository.TestschrittRepository;
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
	private TestschrittRepository schrittRepository;
	@Autowired
	private AnforderungRepository anforderungRepository;
	@Autowired
	private AnforderungService anfService;
	private TestDto testDto;
	

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
		
		
		if(test.getTestschritte() != null) {
			List<Testschritt> sortierteSchritte = schrittRepository.findByTestOrderByStepNumberAsc(test);
			model.addAttribute("schritte", sortierteSchritte);
		}
		
		
		
		model.addAttribute("testNummer", service.generateTestNumber(test));
		return "test_detail";
	}
	
	@GetMapping("/edit/{id}")
	public String zeigEditForm(@PathVariable Long id, Model model) {

		Test test = service.getTestById(id);
		model.addAttribute("test", test);
		
		List<Anforderung> anforderungen = anforderungRepository.findAll();
		model.addAttribute("anforderungen", anforderungen);
		
		
		TestDto dto = new TestDto();
		dto.setTitle(test.getTitle());
		dto.setBeschreibung(test.getBeschreibung());
		dto.setErwartetesErgebnis(test.getErwartetesErgebnis());
		dto.setAnforderung(test.getAnforderung());
		
		List<TestschrittDto> schritteDtos = new ArrayList<>();
		for(Testschritt schritt : test.getTestschritte()) {
			TestschrittDto schrittDto = new TestschrittDto();
			schrittDto.setId(schritt.getId());
			System.out.println("edit id: "+ schrittDto.getId());
			schrittDto.setBeschreibung(schritt.getBeschreibung());
			schritteDtos.add(schrittDto);
		}
		dto.setTestschritte(schritteDtos);
		
	
		
		model.addAttribute("testDto", dto);


		return "test_bearbeiten";
	}
	
	
	
	
	
	@PostMapping("/save") 
	public String neuenTestSpeichern(@ModelAttribute Test test, @RequestParam("reihenfolge") String reihenfolgeJSON) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
            List<String> reihenfolgeListe = mapper.readValue(reihenfolgeJSON, new TypeReference<List<String>>() {});
            List<Testschritt> sortierteSchritte = new ArrayList<>();
            
            int i = 1;
    
            for(String s : reihenfolgeListe) {
            	System.out.println("maybe empty " +s);
            	int stellenIndex = Integer.parseInt(s);
            	// sucht Testschritt anhand der Reihenfolge
            	Testschritt currentSchritt = test.getTestschritte().get(stellenIndex);
            	
            	//prüft auf Inhalt des Schrittes
            	if(currentSchritt.getBeschreibung() != "") {
            		sortierteSchritte.add(currentSchritt);
                	System.out.println(currentSchritt.getBeschreibung());
                	//setzt die SchrittNumme richtig
                	currentSchritt.setStepNumber(i);
                	currentSchritt.setTest(test);
                	i++;
            	}
            	
            	
            }
            
            test.getTestschritte().clear();
            test.setTestschritte(sortierteSchritte);
            
            
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
	public String updateTest(@PathVariable Long id, Model model, @ModelAttribute TestDto testDto) {
		//Test aus Datenbank laden
		Test bestehenderTest = service.getTestById(id);
		
		bestehenderTest.setTitle(testDto.getTitle());
		bestehenderTest.setBeschreibung(testDto.getBeschreibung());
		bestehenderTest.setAnforderung(testDto.getAnforderung());
		model.addAttribute("test", bestehenderTest);
		
		List<Testschritt> bestehendeSchritte = bestehenderTest.getTestschritte();
		Map<Long, Testschritt> schritteMap = bestehendeSchritte.stream()
			        .collect(Collectors.toMap(Testschritt::getId, Function.identity()));
		
		
		
		 List<Testschritt> updatedSchritte = new ArrayList<>();
		 
		 
		 for(TestschrittDto schrittDTO : testDto.getTestschritte()) {
			 System.out.println("DTO ID (update): "+schrittDTO.getId());
			 if (schrittDTO.getId() != null && schritteMap.containsKey(schrittDTO.getId())) {
		            // Bestehender Schritt: aktualisieren
				 	Testschritt schritt = schritteMap.get(schrittDTO.getId());
		            schritt.setBeschreibung(schrittDTO.getBeschreibung());
		            updatedSchritte.add(schritt);
		        } else {
		            // Neuer Schritt: erstellen
		        	Testschritt neuerSchritt = new Testschritt();
		            neuerSchritt.setBeschreibung(schrittDTO.getBeschreibung());
		           
		            // neuerSchritt.setStepNumber(...);
		            neuerSchritt.setTest(bestehenderTest);
		            updatedSchritte.add(neuerSchritt);
		        }
		 }
		 
		 bestehenderTest.setTestschritte(updatedSchritte);
		 
		
		repository.save(bestehenderTest);
		
		
		
		
		
		
		return "redirect:/test/detail/"+id;
	}
	
}
