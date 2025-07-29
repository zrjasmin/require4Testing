package com.require4testing.service;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.require4testing.dto.TestDto;
import com.require4testing.dto.TestlaufDto;
import com.require4testing.model.Anforderung;
import com.require4testing.model.Status;
import com.require4testing.model.Test;
import com.require4testing.model.Testlauf;
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
	
	public void saveNewTestlauf(Testlauf testlauf, Long erstellerId, String verknüpfteTestId) {
		User ersteller = userService.findById(erstellerId);
		testlauf.setErsteller(ersteller);
		
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
		dto.setTester(testlauf.getTester());
		
		dto.setTestumgebung(testlauf.getTestumgebung());
		dto.setTitle(testlauf.getTitle());
		
		Set<TestDto> testsDto = new HashSet<>();
		for(Test test : testlauf.getTests()) {
			TestDto tDto = new TestDto();
			tDto.setId(test.getId());
			testsDto.add(tDto);
		}
		dto.setTests(testsDto);;
		
		return dto;
		
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