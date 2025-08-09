package com.require4testing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.require4testing.dto.TestDto;
import com.require4testing.dto.TestschrittDto;
import com.require4testing.model.Test;
import com.require4testing.model.Testschritt;
import com.require4testing.repository.TestschrittRepository;

@Service
public class TestschrittService {
	
	@Autowired
	private TestschrittRepository schrittRepository;
	
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
	

	
}
