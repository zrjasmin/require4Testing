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
    
    
    public void reloadTestschritte(String reihenfolge, TestDto dto) {
    	
    	ObjectMapper mapper = new ObjectMapper();
		Set<TestDto> verknüpfteTests = new HashSet<>();
		try {
			//6,5,1,7
            List<Integer> testIds = mapper.readValue(reihenfolge, new TypeReference<List<Integer>>() {});
            int i = 0;
            for(TestschrittDto schrittDto : dto.getTestschritte()) {
            	if(schrittDto.getBeschreibung() == null) {
            		dto.getTestschritte().remove(schrittDto);
            	} else if(schrittDto.getBeschreibung() != null) {
            		System.out.println("reload: "+ schrittDto.getStepNumber() + " " + schrittDto.getBeschreibung());
            		schrittDto.setStepNumber(testIds.get(i));
                	i++;
            	
            	} 
            	
            }
            
		} catch (Exception e) {
			
		}
    	
    	
    }
   
    
    public void neuenTestSpeichern(TestDto dto, String reihenfolge, Long erstellerId) {
    	Test test = new Test();
    	
    	if(reihenfolge != "") {
			List<Testschritt> sortierteSchritte = assignStepNumber(test,dto, reihenfolge);
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
    
    public List<Testschritt> assignStepNumber(Test test, TestDto testDto, String schrittReihenfolge) {
		ObjectMapper mapper = new ObjectMapper();
		List<Testschritt> sortierteSchritte = new ArrayList<>();
		try {
			//6,5,1,7
            List<String> reihenfolgeListe = mapper.readValue(schrittReihenfolge, new TypeReference<List<String>>() {});
            
            int i = 1;
          
            for(String s : reihenfolgeListe) {
            	int stellenIndex = Integer.parseInt(s);
            	// sucht Testschritt anhand der Reihenfolge
            	TestschrittDto schrittDto = testDto.getTestschritte().get(stellenIndex);
            	
            	//prüft auf Inhalt des Schrittes
            	if(schrittDto.getBeschreibung() != "") {
            		Testschritt schritt = new Testschritt();
            		schritt.setBeschreibung(schrittDto.getBeschreibung());
            		schritt.setStepNumber(i);
            		schritt.setTest(test);
            		
            		sortierteSchritte.add(schritt);
  
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
		
		Long anfID = testDto.getAnforderungId();
		
		Anforderung anf = anfService.getAnfById(anfID);
		bestehenderTest.setAnforderung(anf);
		System.out.println("speichern: "+ testDto.getAnforderungId());
		bestehenderTest.setTestdaten(testDto.getTestdaten());
		bestehenderTest.setNotizen(testDto.getNotizen());
		
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
			 System.out.println(schrittDTO.getBeschreibung());
			 if (schrittDTO.getId() != null && bestehendeSchritteMap.containsKey(schrittDTO.getId())) {
		            // Bestehender Schritt: aktualisieren
				 	Testschritt schritt = bestehendeSchritteMap.get(schrittDTO.getId());
		            schritt.setBeschreibung(schrittDTO.getBeschreibung());
		            System.out.println("Kriterium ist bereits gespeichert: "+schrittDTO.getId());
		            bestehendeSchritteMap.remove(schrittDTO.getId());
		            idZuSchrittNummer.put(schritt.getStepNumber(), schritt.getId());
		           
		        } else {
		            // Neuer Schritt: erstellen
		        	if(schrittDTO.getBeschreibung() != null && schrittDTO.getBeschreibung() != "") {
		        		  System.out.println("neues Kriterium: "+schrittDTO.getBeschreibung());
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
			 if(reihenfolgeJSON != "" && reihenfolgeJSON!=null) {
				 List<String> stepValues = mapper.readValue(reihenfolgeJSON, new TypeReference<List<String>>() {});;
				 System.out.println("reihenfolgeJSon " + reihenfolgeJSON);
				 int i = 1;
				 for(String s : stepValues) {
					Integer intS = Integer.parseInt(s);
	            

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
        if(schrittDto.getStepNumber() == null) {
        	Integer step = test.getTestschritte().size();
        	 System.out.println(step);
        	 neuerSchritt.setStepNumber(step +1);
        } else {
        	neuerSchritt.setStepNumber(schrittDto.getStepNumber());
        }
        System.out.println(schrittDto.getStepNumber());
      
        schrittRepository.save(neuerSchritt);
        return neuerSchritt;
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