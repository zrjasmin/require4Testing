package com.require4testing.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.require4testing.repository.AnforderungRepository;
import com.require4testing.repository.UserRepository;
import com.require4testing.model.Anforderung;
import com.require4testing.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class AnforderungService {

    @Autowired
    private AnforderungRepository repository;
    
    @Autowired
    private UserRepository userRepository;

    public List<Anforderung> alleEntities() {
        return repository.findAll();
    }

    public Anforderung speichereEntity(Anforderung entity) {
        return repository.save(entity);
    }
   


    public Anforderung erstelleAnf(Long erstellerId) {
    	Optional<User> erstellerOpt = userRepository.findById(erstellerId);
        
        // Neue Entität erstellen
    	
    	if(erstellerOpt.isPresent()) {
    		Anforderung neuerTest = new Anforderung("name", "beschreibung");
    		return repository.save(neuerTest);
    	}
    	else {
            throw new RuntimeException("Benutzer nicht gefunden");
    	}    	
     
    }
}