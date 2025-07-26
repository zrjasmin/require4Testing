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
import com.require4testing.model.Testschritt;
import com.require4testing.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
		dto.setTitle(test.getTitle());
		dto.setBeschreibung(test.getBeschreibung());
		dto.setErwartetesErgebnis(test.getErwartetesErgebnis());
		dto.setAnforderung(test.getAnforderung());
		dto.setTestdaten(test.getTestdaten());
		
		List<TestschrittDto> schritteDtos = new ArrayList<>();
		for(Testschritt schritt : test.getTestschritte()) {
			TestschrittDto schrittDto = new TestschrittDto();
			schrittDto.setId(schritt.getId());
			
			schrittDto.setBeschreibung(schritt.getBeschreibung());
			schrittDto.setStepNumber(schritt.getStepNumber());
			schritteDtos.add(schrittDto);
		}
		dto.setTestschritte(schritteDtos);
		
		return dto;
    }
    
    
    public void neuenTestSpeichern(Test test, String reihenfolge, Long erstellerId) {
    	if(reihenfolge != "") {
			List<Testschritt> sortierteSchritte = assignStepNumber(test, reihenfolge);
			if(test.getTestschritte() != null) {
				test.getTestschritte().clear();
			}
	        test.setTestschritte(sortierteSchritte);
		}
    	
    	  User ersteller = userService.findById(erstellerId);
          test.setErsteller(ersteller);
          
    	
    	Long anfID = test.getAnforderung().getId();
		
		Anforderung anf = anfService.getAnfById(anfID);
		
		test.setAnforderung(anf);
		repository.save(test);
    	
    }
    
    public List<Testschritt> assignStepNumber(Test test, String schrittReihenfolge) {
		ObjectMapper mapper = new ObjectMapper();
		List<Testschritt> sortierteSchritte = new ArrayList<>();
		try {
			//6,5,1,7
            List<String> reihenfolgeListe = mapper.readValue(schrittReihenfolge, new TypeReference<List<String>>() {});
            
            int i = 1;
          
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
    
    
    
    public Test updateTest(Long id, TestDto testDto, String reihenfolgeJSON) {
    	Test bestehenderTest = getTestById(id);
    	bestehenderTest.setTitle(testDto.getTitle());
		bestehenderTest.setBeschreibung(testDto.getBeschreibung());
		bestehenderTest.setAnforderung(testDto.getAnforderung());
		bestehenderTest.setTestdaten(testDto.getTestdaten());
		
		updateSchritte(bestehenderTest, testDto,reihenfolgeJSON);
		
		
		repository.save(bestehenderTest);
		return bestehenderTest;
    }
    
    public void updateSchritte(Test test, TestDto testDto, String reihenfolgeJSON) {
    	Test bestehenderTest = test;
    	List<Testschritt> bestehendeSchritte = bestehenderTest.getTestschritte();
		Map<Long, Testschritt> bestehendeSchritteMap = bestehendeSchritte.stream()
			        .collect(Collectors.toMap(Testschritt::getId, Function.identity()));
		
		//speichert ID und tempräre Schrittnummer
		 Map<Integer, Long> idZuSchrittNummer = new HashMap<>();
		
		 
		 // Aktualisieren / Neue hinzufügen
		 if(testDto.getTestschritte() != null) {
			 
		
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
		 }
		 for(Testschritt zuLöschen : bestehendeSchritteMap.values()) {
			 bestehenderTest.removeSchritt(zuLöschen);
			 schrittRepository.delete(zuLöschen);
		 }
		 
		 sortSchritte(reihenfolgeJSON, idZuSchrittNummer);
		 
		 
    }
    
    public void sortSchritte(String reihenfolgeJSON, Map<Integer, Long> idZuSchrittNummer ) {
    	
    	ObjectMapper mapper = new ObjectMapper();
    	 try {
			 if(reihenfolgeJSON != "") {
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
			 }
			 
			 
		 } catch (Exception e) {
			    e.printStackTrace();
		 }
    }
    
    public Testschritt createNeuerSchritt(TestschrittDto schrittDto, Test test) {
		Testschritt neuerSchritt = new Testschritt();
        neuerSchritt.setBeschreibung(schrittDto.getBeschreibung());
        neuerSchritt.setTest(test);
        neuerSchritt.setStepNumber(schrittDto.getStepNumber());
        schrittRepository.save(neuerSchritt);
        return neuerSchritt;
	}
    
    public void deleteTest(Long id) {
    	Test test = getTestById(id);
    	test.setAnforderung(null);
    	repository.delete(test);
    }
    
    
    
    
    
    
    
}