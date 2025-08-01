package com.require4testing.service;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.require4testing.dto.TestDto;
import com.require4testing.dto.TestlaufDto;
import com.require4testing.model.Anforderung;
import com.require4testing.model.Status;
import com.require4testing.model.Test;
import com.require4testing.model.Testlauf;
import com.require4testing.model.Testschritt;
import com.require4testing.model.User;
import com.require4testing.repository.StatusRepository;
import com.require4testing.repository.TestlaufRepository;

@Service
public class TestlaufService {
	
	@Autowired 
	private StatusRepository statusRep;
	
	@Autowired 
	private TestlaufRepository repository;
	@Autowired 
	private UserService userService;
	@Autowired 
	private TestService testService;
	
	private TestlaufDto dto;
	
	
	public List<Testlauf> alleEntities() {
		 return repository.findAll();
	}
	
	public List<Status> alleStatus() {
		//saveStatus();
		return statusRep.findAll();
	}
	
	@Transactional
	public void speichereEntity(Testlauf testlauf) {
		repository.save(testlauf);
	}
	
	public Testlauf getTestlaufById(Long id) {
    	return repository.findById(id).get();
    }
	
	
	
	public void saveNewTestlauf(Testlauf testlauf, Long erstellerId, String verknüpfteTestId, Long testerId) {
		User ersteller = userService.findById(erstellerId);
		testlauf.setErsteller(ersteller);
		System.out.println(testerId);
		if(testerId != null) {
			testlauf.setTester(userService.findById(testerId));
		}
		
		testlauf.setTests(testService.findTestByString(verknüpfteTestId));
		speichereEntity(testlauf);
		saveNumber(testlauf);

		
	}
	
	public TestlaufDto convertToDto(Long id) {
		TestlaufDto dto = new TestlaufDto();
		Testlauf testlauf = getTestlaufById(id);
		
		dto.setBeschreibung(testlauf.getBeschreibung());
		dto.setId(id);
		dto.setKommentar(testlauf.getKommentar());
		dto.setStatus(testlauf.getStatus());
		System.out.println(testlauf.getTester());
		if(testlauf.getTester() != null) {
			dto.setTester(testlauf.getTester());
		} else {
			dto.setTester(null);
		}
		
		dto.setTestumgebung(testlauf.getTestumgebung());
		dto.setTitle(testlauf.getTitle());
		
		//Test Id speichern
		Set<Long> testsDto = new HashSet<>();
		for(Test test : testlauf.getTests()) {
			testsDto.add(test.getId());
		}
		dto.setTests(testsDto);;
		
		return dto;
		
	}
	
	public Testlauf updateTestlauf(Long id, TestlaufDto testlaufDto, String testIds) {
		Testlauf bestehenderTestlauf = getTestlaufById(id);
		
		
		System.out.println("dto "+testlaufDto.getTester().getId());
		System.out.println("dto "+testlaufDto.getTester());
		System.out.println("db "+ bestehenderTestlauf.getTester().getId());
		
		
		if (bestehenderTestlauf != null && bestehenderTestlauf.getTester() != null && testlaufDto.getTester().getId() == null) {
	    	System.out.println("Fehler im Service");

            throw new IllegalArgumentException("Das Tester-Feld darf nicht entfernt werden, wenn bereits ein Tester zugeordnet war.");
        }
		if(testlaufDto.getTester() != null) {
			bestehenderTestlauf.setTester(testlaufDto.getTester());
		} else {
			bestehenderTestlauf.setTester(null);
		}
	
		
		bestehenderTestlauf.setBeschreibung(testlaufDto.getBeschreibung());
		bestehenderTestlauf.setKommentar(testlaufDto.getKommentar());
		bestehenderTestlauf.setStatus(testlaufDto.getStatus());
		
		bestehenderTestlauf.setTestumgebung(testlaufDto.getTestumgebung());
		bestehenderTestlauf.setTitle(testlaufDto.getTitle());
		
		updateVerknüpfteTests(bestehenderTestlauf, testlaufDto, testIds);
		repository.save(bestehenderTestlauf);

		return bestehenderTestlauf;
	}
	
	
	
	
	public void updateVerknüpfteTests(Testlauf testlauf,TestlaufDto dto, String testIds) {
		Set<Test> aktuelleTests = testlauf.getTests();
	
		
		//Ids aus der DTO
		Set<Test> neueTestsListe = testService.findTestByString(testIds);

		 Set<Test> neueTestSet = new HashSet<>(neueTestsListe);

		 aktuelleTests.removeIf(test -> !neueTestSet.contains(test));
		 aktuelleTests.addAll(neueTestSet.stream()
			        .filter(test -> !aktuelleTests.contains(test))
			        .collect(Collectors.toSet()));		 
	}
	
	
	public void deleteTestlauf(Long id) {
		Testlauf testlauf = getTestlaufById(id);
		
		for(Test t : testlauf.getTests()) {
    		t.getTestlaeufe().remove(testlauf);
    		
    	}
    	testlauf.setTests(null);
    	repository.delete(testlauf);
	}
	
	public void saveNumber(Testlauf testlauf) {
		   String formattedNumber = null;
		   if(testlauf != null) {
			   Long id = testlauf.getId();
			   formattedNumber = String.format("TL-%03d", id);
			   
		   } 
		   testlauf.setNr(formattedNumber);
		   System.out.println(formattedNumber);
		   
		   speichereEntity(testlauf);
	   }
	
	
	
	
	
	public void saveStatus() {
		Status s1 = new Status("planned", "Geplant");
		Status s2 = new Status("in-progress", "In Durchführung");
		Status s3 = new Status("completed", "Abgeschlossen");
		Status s4 = new Status("passed", "Erfolgreich bestanden");
		Status s5 = new Status("failed", "Nicht bestanden");
		Status s6 = new Status("pending", "Blockiert");
		
		statusRep.save(s1);
		statusRep.save(s2);
		statusRep.save(s3);
		statusRep.save(s4);
		statusRep.save(s5);
		statusRep.save(s6);
		
	}
}