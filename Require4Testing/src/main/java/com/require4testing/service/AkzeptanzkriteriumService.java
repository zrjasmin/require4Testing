package com.require4testing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.require4testing.repository.AkzeptanzkriteriumRepository;
import com.require4testing.repository.AnforderungRepository;
import com.require4testing.model.Akzeptanzkriterium;
import com.require4testing.model.Anforderung;


@Service
public class AkzeptanzkriteriumService {
	
	 @Autowired
	    private AkzeptanzkriteriumRepository repository;
	 @Autowired
	    private AnforderungRepository anfRepository;
	 
	 public List<Akzeptanzkriterium> alleEntities() {
	        return repository.findAll();
	    }
	 
	 
	 public Akzeptanzkriterium speichereEntity(Akzeptanzkriterium entity) {
	        return repository.save(entity);
	    }
	 
	 public Akzeptanzkriterium erstelleAkz(Anforderung anf, Akzeptanzkriterium entity) {
	    	Optional<Anforderung> anforderung = anfRepository.findById(anf.getId());
	       
	        // Neue Entität erstellen
	    	
	    	if(anforderung.isPresent()) {
	    		entity.setAnforderung(anf);
	    		return repository.save(entity);
	    	}
	    	else {
	            throw new RuntimeException("Benutzer nicht gefunden");
	    	}    	
	     
	    }
}
