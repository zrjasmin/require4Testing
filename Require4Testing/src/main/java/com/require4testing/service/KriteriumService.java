package com.require4testing.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.require4testing.repository.KriteriumRepository;
import com.require4testing.repository.AnforderungRepository;
import com.require4testing.model.Anforderung;
import com.require4testing.model.Kriterium;


@Service
public class KriteriumService {
	
	 @Autowired
	    private KriteriumRepository repository;
	 @Autowired
	    private AnforderungRepository anfRepository;
	 
	 public List<Kriterium> alleEntities() {
	        return repository.findAll();
	    }
	 
	 
	 public Kriterium speichereEntity(Kriterium entity) {
	        return repository.save(entity);
	    }
	 
	 public Kriterium erstelleAkz(Anforderung anf, Kriterium entity) {
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
