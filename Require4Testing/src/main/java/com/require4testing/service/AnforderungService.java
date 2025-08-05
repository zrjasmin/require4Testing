package com.require4testing.service;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import com.require4testing.repository.AnforderungRepository;
import com.require4testing.repository.KriteriumRepository;
import com.require4testing.dto.AnforderungDto;
import com.require4testing.dto.KriteriumDto;
import com.require4testing.model.Anforderung;
import com.require4testing.model.Kriterium;
import com.require4testing.model.Test;
import com.require4testing.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Service
public class AnforderungService {

    @Autowired
    private AnforderungRepository repository;
    
    @Autowired 
    private UserService userService;
    
    
    @Autowired
    private  KriteriumRepository akzRepository;
    
    @Autowired
    private KriteriumService akzService;

    public List<Anforderung> alleEntities() {
        return repository.findAll();
    }
    
    public Anforderung getAnfById(Long id) {
    	Optional<Anforderung> optAnf = repository.findById(id);
    	return optAnf.orElse(null);
    }
    
    @Transactional
    public Anforderung speichereEntity(Anforderung entity) {
    	
        return repository.save(entity);
    }
    
    
   


    public Anforderung erstelleAnf(Long erstellerId) {
    	User user = userService.findById(erstellerId);
        
    	if(user != null) {
    		Anforderung neuerTest = new Anforderung("name", "beschreibung");
    		return repository.save(neuerTest);
    	}
    	else {
            throw new RuntimeException("Benutzer nicht gefunden");
    	} 
    }
    
    
    public void deleteAnf(Long id,HttpSession session) {
    	userService.hasPermision(session, "delete_requirement");
    	Anforderung anf = getAnfById(id);
    	repository.delete(anf);
    }
    
    public AnforderungDto  convertToDto(Anforderung anf) {
    	AnforderungDto dto = new AnforderungDto();
		dto.setId(anf.getId());
		dto.setTitle(anf.getTitle());
		dto.setBeschreibung(anf.getBeschreibung());
		dto.setPriotiät(anf.getPrioritaet());
		dto.setKategorie(anf.getKategorie());
		dto.setQuelle(anf.getQuelle());
		dto.setNotizen(anf.getNotizen());
		
		
		
		List<KriteriumDto> kriterienDto = new ArrayList<>();
		if(anf.getKriterien() !=null) {
			for(Kriterium k: anf.getKriterien()) {
				KriteriumDto kriteriumDto = new KriteriumDto();
				kriteriumDto.setId(k.getId());
				kriteriumDto.setBeschreibung(k.getBeschreibung());
				kriterienDto.add(kriteriumDto);
			}
		}
		
		dto.setKriterien(kriterienDto);
		return dto;
    }
    
    
    public void updateAnf(Long id, AnforderungDto anfDto) {
    	Anforderung bestehendeAnf = getAnfById(id);
		
		bestehendeAnf.setTitle(anfDto.getTitle());
		bestehendeAnf.setBeschreibung(anfDto.getBeschreibung());
		bestehendeAnf.setPrioritaet(anfDto.getPriotiät());
		bestehendeAnf.setKategorie(anfDto.getKategorie());
		bestehendeAnf.setQuelle(anfDto.getQuelle());
		bestehendeAnf.setNotizen(anfDto.getNotizen());
		
		updateKriterien(anfDto, bestehendeAnf);
		
		repository.save(bestehendeAnf);
    }
    
    public void updateKriterien(AnforderungDto anfDto, Anforderung bestehendeAnf) {
    	Set<Kriterium> bestehendeKriterien = new HashSet<>(bestehendeAnf.getKriterien());		
		Map<Long, Kriterium> bestehendekriterienMap = bestehendeKriterien.stream()
		        .collect(Collectors.toMap(Kriterium::getId, Function.identity()));
		
		List<Kriterium> updatedKriterien = new ArrayList<>();
		
		for(KriteriumDto kDto : anfDto.getKriterien()) {
			
			System.out.println("dto: "+kDto.getBeschreibung());
			 if (kDto.getId() != null && bestehendekriterienMap.containsKey(kDto.getId())) {
				 	Kriterium kriterium = bestehendekriterienMap.get(kDto.getId());
				 	kriterium.setBeschreibung(kDto.getBeschreibung());
				 	bestehendekriterienMap.remove(kDto.getId());
				 	updatedKriterien.add(kriterium);	

	            } else {
	            	// Neues Kriterium erstellen
	            	if(kDto.getBeschreibung() != null && kDto.getBeschreibung() != "") {
	            		Kriterium kriterium = neuesKriterium(kDto, bestehendeAnf);
	            		updatedKriterien.add(kriterium);		
	            	} 
	            }
		}
		

		for(Kriterium zuLöschen : bestehendekriterienMap.values()) {
				bestehendeAnf.removeKriterium(zuLöschen);
				akzRepository.delete(zuLöschen);
			
			}
    }
    
    
    public Kriterium neuesKriterium(KriteriumDto dto, Anforderung anf) {
		Kriterium k = new Kriterium();
		k.setBeschreibung(dto.getBeschreibung());
		k.setAnforderung(anf);
		akzService.speichereEntity(k);
		return k;
	}
    
    public void saveNewAnf(Anforderung anforderung, Long erstellerId) {
    	
    	User ersteller = userService.findById(erstellerId);
        anforderung.setErsteller(ersteller);
      
		//bidrektionale Beziehung setzen
		for(Kriterium k:anforderung.getKriterien()) {
			//Inhalt des Kriterium prüfen
			if(k.getBeschreibung() !="" || k.getBeschreibung()!= null) {
				k.setAnforderung(anforderung);
			} else {
				anforderung.getKriterien().remove(k);
			}
			
		}
		
		speichereEntity(anforderung);
		saveNumber(anforderung);
		System.out.println("nach speicherung: "+anforderung.getNr());

    }
    
   public void saveNumber(Anforderung anf) {
	   String formattedNumber = null;
	   if(anf != null) {
		   Long id = anf.getId();
		   formattedNumber = String.format("AR-%03d", id);
		   
	   } 
	   anf.setNr(formattedNumber);
	   System.out.println(formattedNumber);
	   
	   speichereEntity(anf);
   }
    
    public List<Test> getTestforAnf(Long id) {
    	List<Test> verknüpfteTest = new ArrayList<>();
    	
    	
    	
    	return verknüpfteTest;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}