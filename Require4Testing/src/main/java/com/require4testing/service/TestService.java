package com.require4testing.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.require4testing.repository.TestRepository;
import com.require4testing.repository.TestschrittRepository;
import com.require4testing.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.require4testing.dto.TestDto;
import com.require4testing.dto.TestschrittDto;
import com.require4testing.model.Anforderung;
import com.require4testing.model.Test;
import com.require4testing.model.Testlauf;
import com.require4testing.model.Testschritt;
import com.require4testing.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Autowired
    private TestRepository repository;
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    
    @Autowired
    private AnforderungService anfService;
    @Autowired
    private TestschrittService schrittService;
    
    @Autowired 
    private TestschrittRepository schrittRepository;

    public List<Test> alleEntities() {
        return repository.findAll();
    }
    
    public Test getTestById(Long id) {
    	return repository.findById(id).get();
    }

    public Test speichereEntity(Test entity) {
        return repository.save(entity);
    }
    
    public Set<Test> findTestByString(String list) {
    	ObjectMapper mapper = new ObjectMapper();
		Set<Test> verknüpfteTests = new HashSet<>();
		try {
			//6,5,1,7
            List<String> testIds = mapper.readValue(list, new TypeReference<List<String>>() {});
            
            for(String id : testIds) {
            	Long longId = Long.parseLong(id);
            	Test test = getTestById(longId);
            
            	verknüpfteTests.add(test);
            }
            
		} catch (Exception e) {
			
		}
    	return verknüpfteTests;
    }
   

    public Test erstelleTestfall(Long erstellerId) {
    	Optional<User> erstellerOpt = userRepository.findById(erstellerId);
        
        // Neue Entität erstellen
    	
    	if(erstellerOpt.isPresent()) {
    		Test neuerTest = new Test("name", "beschreibung");
    		neuerTest.setErsteller(erstellerOpt.get());
    		return repository.save(neuerTest);
    	}
    	else {
            throw new RuntimeException("Benutzer nicht gefunden");
    	}    	
     
    }
    
    public String generateTestNumber(Test t) {
    	Optional<Test> test = repository.findById(t.getId());
    	String testNumber = "";
    	if(test.isPresent()) {
    		testNumber = String.format("TF-%03d", t.getId());
    	}
    	
    	return testNumber;
    }
    


    public TestDto convertToDto(Long id) {
    	Test test = getTestById(id);
    	
    	TestDto dto = new TestDto();
    	dto.setId(id);
		dto.setTitle(test.getTitle());
		dto.setBeschreibung(test.getBeschreibung());
		dto.setErwartetesErgebnis(test.getErwartetesErgebnis());
		dto.setAnforderungId(test.getAnforderung().getId());
		System.out.println("speichern: "+ test.getAnforderung().getId());
		System.out.println("speichern in dto: "+ dto.getAnforderungId());
		dto.setTestdaten(test.getTestdaten());
		dto.setNotizen(test.getNotizen());
		
		List<TestschrittDto> schritteDtos = new ArrayList<>();
		List<Testschritt> sortierteSchritte = schrittRepository.findByTestOrderByStepNumberAsc(test);
		
		
		
		for(Testschritt schritt :sortierteSchritte) {
		
			TestschrittDto schrittDto = new TestschrittDto();
			schrittDto.setId(schritt.getId());
			
			schrittDto.setBeschreibung(schritt.getBeschreibung());
			schrittDto.setStepNumber(schritt.getStepNumber());
			schritteDtos.add(schrittDto);
			System.out.println("service: "+schrittDto.getStepNumber() +" "+ schrittDto.getBeschreibung());
		}
		
		dto.setTestschritte(schritteDtos);
		
		return dto;
    }
    

    public void neuenTestSpeichern(TestDto dto, String reihenfolge, Long erstellerId) {
    	Test test = new Test();
    	
    	if(reihenfolge != "") {
			List<Testschritt> sortierteSchritte = schrittService.assignStepNumber(test,dto, reihenfolge);
			if(test.getTestschritte() != null) {
				test.getTestschritte().clear();
			}
	        test.setTestschritte(sortierteSchritte);
		}
    	
    	test.setTitle(dto.getTitle());
        test.setBeschreibung(dto.getBeschreibung());
        test.setErwartetesErgebnis(dto.getErwartetesErgebnis());
		test.setTestdaten(dto.getTestdaten());
		test.setNotizen(dto.getNotizen());
    	User ersteller = userService.findById(erstellerId);
        test.setErsteller(ersteller);
    	
        Long anfID = dto.getAnforderungId();
         
         
        Anforderung anf = anfService.getAnfById(anfID);
        test.setAnforderung(anf);
 		repository.save(test);
 		saveNumber(test);
	    
    	
    }
    
    
    public Test updateTest(Long id, TestDto testDto, String reihenfolgeJSON) {
    	Test bestehenderTest = getTestById(id);
    	bestehenderTest.setTitle(testDto.getTitle());
		bestehenderTest.setBeschreibung(testDto.getBeschreibung());
		
		Long anfID = testDto.getAnforderungId();
		
		Anforderung anf = anfService.getAnfById(anfID);
		bestehenderTest.setAnforderung(anf);
		System.out.println("speichern: "+ testDto.getAnforderungId());
		bestehenderTest.setTestdaten(testDto.getTestdaten());
		bestehenderTest.setNotizen(testDto.getNotizen());
		
		schrittService.updateSchritte(bestehenderTest, testDto,reihenfolgeJSON);
		
		
		repository.save(bestehenderTest);
		return bestehenderTest;
    }
    
    
    public void deleteTest(Long id) {
    	Test test = getTestById(id);
    	for(Testlauf t : test.getTestlaeufe()) {
    		t.getTests().remove(test);
    		
    	}
    	test.setAnforderung(null);
    	repository.delete(test);
    	
    }
    
    
    public void saveNumber(Test test) {
    	String formattedNumber = null;
 	   if(test != null) {
 		   Long id = test.getId();
 		   formattedNumber = String.format("TF-%03d", id);
 		   
 	   } 
 	   test.setNr(formattedNumber);
 	   System.out.println(formattedNumber);
 	   
 	   speichereEntity(test);
    }
    
    
    
    
}