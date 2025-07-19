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
	private TestDto testDto;
	

	@GetMapping("/all") 
	public String alleTests(Model model, HttpSession session) {
		model.addAttribute("test", repository.findAll());
		
		User user = (User) session.getAttribute("currentUser");		
		model.addAttribute("aktuellerUser", user);
		
		return "test_uebersicht";
	}
	
	@GetMapping("/edit")
	public String zeigeNeueTestForm(Model model, HttpSession session) {
		if(userService.hasPermision(session, "create_test"))
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
			
			schrittDto.setBeschreibung(schritt.getBeschreibung());
			schrittDto.setStepNumber(schritt.getStepNumber());
			schritteDtos.add(schrittDto);
		}
		dto.setTestschritte(schritteDtos);
		
	
		
		model.addAttribute("testDto", dto);


		return "test_bearbeiten";
	}
	
	
	
	
	
	@PostMapping("/save") 
	public String neuenTestSpeichern(@ModelAttribute Test test, @RequestParam("reihenfolge") String reihenfolgeJSON) {
		
		List<Testschritt> sortierteSchritte = assignStepNumber(test, reihenfolgeJSON);
		test.getTestschritte().clear();
        test.setTestschritte(sortierteSchritte);
        
		Long anfID = test.getAnforderung().getId();
		
		Anforderung anf = anfService.getAnfById(anfID);
		
		test.setAnforderung(anf);
		
		repository.save(test);
		
		return "redirect:/test/all";
	}
	
	
	public List<Testschritt> assignStepNumber(Test test, String schrittReihenfolge) {
		ObjectMapper mapper = new ObjectMapper();
		List<Testschritt> sortierteSchritte = new ArrayList<>();
		try {
			//6,5,1,7
            List<String> reihenfolgeListe = mapper.readValue(schrittReihenfolge, new TypeReference<List<String>>() {});
            
            int i = 1;
            System.out.println(test.getTestschritte().size());
            for(String s : reihenfolgeListe) {
            	System.out.println("Sortierung durch JS:  " +s);
            	System.out.println("Schritt: " +i);
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
            
            System.out.println(sortierteSchritte);
		} catch(Exception e) {
			 e.printStackTrace();
		}
		
		return sortierteSchritte;
	}
	
	
	@Transactional
	@PostMapping("/update/{id}")
	public String updateTest(@PathVariable Long id, Model model, @ModelAttribute TestDto testDto, @RequestParam("reihenfolge") String reihenfolgeJSON) {
		ObjectMapper mapper = new ObjectMapper();
		
		//Test aus Datenbank laden
		Test bestehenderTest = service.getTestById(id);
		
		bestehenderTest.setTitle(testDto.getTitle());
		bestehenderTest.setBeschreibung(testDto.getBeschreibung());
		bestehenderTest.setAnforderung(testDto.getAnforderung());
		model.addAttribute("test", bestehenderTest);
		
		List<Testschritt> bestehendeSchritte = bestehenderTest.getTestschritte();
		Map<Long, Testschritt> bestehendeSchritteMap = bestehendeSchritte.stream()
			        .collect(Collectors.toMap(Testschritt::getId, Function.identity()));
		
		//speichert ID und tempräre Schrittnummer
		 Map<Integer, Long> idZuSchrittNummer = new HashMap<>();
		
		 
		 // Aktualisieren / Neue hinzufügen
		 for(TestschrittDto schrittDTO : testDto.getTestschritte()) {
		
			 if (schrittDTO.getId() != null && bestehendeSchritteMap.containsKey(schrittDTO.getId())) {
		            // Bestehender Schritt: aktualisieren
				 	Testschritt schritt = bestehendeSchritteMap.get(schrittDTO.getId());
		            schritt.setBeschreibung(schrittDTO.getBeschreibung());
		            System.out.println("Kriterium ist bereits gespeichert: "+schrittDTO.getId());
		            bestehendeSchritteMap.remove(schrittDTO.getId());
		            idZuSchrittNummer.put(schritt.getStepNumber(), schritt.getId());
		           
		        } else {
		            // Neuer Schritt: erstellen
		        	if(schrittDTO.getBeschreibung() != null) {
		        		Testschritt neuerSchritt = createNeuerSchritt(schrittDTO, bestehenderTest);
		        		idZuSchrittNummer.put(neuerSchritt.getStepNumber(), neuerSchritt.getId());
		        	}
		            
		        }
		 }
		 
		 //löschte Schritte
		 for(Testschritt zuLöschen : bestehendeSchritteMap.values()) {
			 bestehenderTest.removeSchritt(zuLöschen);
			 schrittRepository.delete(zuLöschen);
		 }
		 
		 
		 //sortiert Schritte neu
		 try {
			 List<String> stepValues = mapper.readValue(reihenfolgeJSON, new TypeReference<List<String>>() {});;
					 
			 int i = 1;
			 for(String s : stepValues) {
				Integer intS = Integer.parseInt(s);
             	System.out.println(intS);

             	//
				 if(idZuSchrittNummer.containsKey(intS)) {
					 Long schrittId = idZuSchrittNummer.get(intS);
					 Optional<Testschritt> optSchritt = schrittRepository.findById(schrittId);
					 if(optSchritt.isPresent()) {
						 Testschritt schritt  = optSchritt.get();
						
						 schritt.setStepNumber(i);
				
						 i++; 
					 }
					
				 }
	            	
			 }
			 
		 } catch (Exception e) {
			    e.printStackTrace();
		 }
		
		
		repository.save(bestehenderTest);
		

		
		return "redirect:/test/detail/"+id;
	}
	
	
	@PostMapping("/delete/{id}")
	public String deleteTest(@PathVariable Long id) {
		Test test = service.getTestById(id);
		
		service.deleteTest(test);
		return "redirect:/test/all";
	}
	
	public Testschritt createNeuerSchritt(TestschrittDto schrittDto, Test test) {
		Testschritt neuerSchritt = new Testschritt();
        neuerSchritt.setBeschreibung(schrittDto.getBeschreibung());
        neuerSchritt.setTest(test);
        neuerSchritt.setStepNumber(schrittDto.getStepNumber());
        schrittRepository.save(neuerSchritt);
        return neuerSchritt;
	}
	
}
