package com.require4testing.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.require4testing.repository.TestRepository;
import com.require4testing.repository.UserRepository;
import com.require4testing.model.Test;
import com.require4testing.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private TestRepository repository;
    
    @Autowired
    private UserRepository userRepository;

    public List<Test> alleEntities() {
        return repository.findAll();
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
}