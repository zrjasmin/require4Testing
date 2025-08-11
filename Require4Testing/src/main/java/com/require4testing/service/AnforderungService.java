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
    private KriteriumService akzService;
   
    
    

    public List<Anforderung> alleEntities() {
        return repository.findAll();
    }
    
    public Anforderung getAnfById(Long id) {
    	Optional<Anforderung> optAnf = repository.findById(id);
    	return optAnf.orElse(null);
    }
    
    @Transactional
    public void speichereEntity(Anforderung entity) {
    	
        repository.save(entity);
    }
    
  
    
    public void saveNewAnf(AnforderungDto anf, Long erstellerId) {
    	Anforderung anforderung = new Anforderung();
    	User ersteller = userService.findById(erstellerId);
        anforderung.setErsteller(ersteller);
        
        
        anforderung.setTitle(anf.getTitle());
        anforderung.setBeschreibung(anf.getBeschreibung());
        anforderung.setPrioritaet(anf.getPrioritaet());
        anforderung.setKategorie(anf.getKategorie());
        anforderung.setQuelle(anf.getQuelle());
		anforderung.setNotizen(anf.getNotizen());
      
		
		//bidrektionale Beziehung setzen
		for(KriteriumDto k:anf.getKriterien()) {
			//Inhalt des Kriterium prüfen
			if(k.getBeschreibung() !="" || k.getBeschreibung()!= null) {
				Kriterium kriterium = new Kriterium();
				kriterium.setBeschreibung(k.getBeschreibung());
				kriterium.setAnforderung(anforderung);
				anforderung.getKriterien().add(kriterium);
			} else {
				anforderung.getKriterien().remove(k);
			}
			
		}
		
		speichereEntity(anforderung);
		saveNumber(anforderung);
		

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
		dto.setPrioritaet(anf.getPrioritaet());
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
		bestehendeAnf.setPrioritaet(anfDto.getPrioritaet());
		bestehendeAnf.setKategorie(anfDto.getKategorie());
		bestehendeAnf.setQuelle(anfDto.getQuelle());
		bestehendeAnf.setNotizen(anfDto.getNotizen());
		
		akzService.updateKriterien(anfDto, bestehendeAnf);
		
		repository.save(bestehendeAnf);
    }
    
    
  
    
   public void saveNumber(Anforderung anf) {
	   String formattedNumber = null;
	   if(anf != null) {
		   Long id = anf.getId();
		   formattedNumber = String.format("AR-%03d", id);
		   
	   } 
	   anf.setNr(formattedNumber);
	   
	   speichereEntity(anf);
   }
    
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}