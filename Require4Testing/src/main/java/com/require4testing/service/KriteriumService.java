package com.require4testing.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.require4testing.repository.KriteriumRepository;
import com.require4testing.repository.AnforderungRepository;
import com.require4testing.dto.AnforderungDto;
import com.require4testing.dto.KriteriumDto;
import com.require4testing.model.Anforderung;
import com.require4testing.model.Kriterium;


@Service
public class KriteriumService {
	
	 @Autowired
	 private KriteriumRepository repository;
	
	 
	 public List<Kriterium> alleEntities() {
	        return repository.findAll();
	    }
	 
	 
	 public void speichereEntity(Kriterium entity) {
	        repository.save(entity);
	    }
	 
	 public void erstelleAkz(Anforderung anf, Kriterium entity) {	       
	        // Neue Entität erstellen
	    	if(anf!=null) {
	    		entity.setAnforderung(anf);
	    		repository.save(entity);
	    	}
	    	  
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
					repository.delete(zuLöschen);
				
				}
	    }
	    
	    
	    public Kriterium neuesKriterium(KriteriumDto dto, Anforderung anf) {
			Kriterium k = new Kriterium();
			k.setBeschreibung(dto.getBeschreibung());
			k.setAnforderung(anf);
			speichereEntity(k);
			return k;
		}
	    
}
