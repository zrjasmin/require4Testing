package com.require4testing.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.require4testing.service.UserService;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PatchMapping;

import com.require4testing.repository.AnforderungRepository;
import com.require4testing.repository.UserRepository;
import com.require4testing.model.Anforderung;
import com.require4testing.model.User;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Service
public class AnforderungService {

    @Autowired
    private AnforderungRepository repository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired 
    private UserService userService;

    public List<Anforderung> alleEntities() {
        return repository.findAll();
    }
    
    public Anforderung getAnfById(Long id) {
    	Optional<Anforderung> optAnf = repository.findById(id);
    	return optAnf.orElse(null);
    }
    
    @Transactional
    public Anforderung speichereEntity(Anforderung entity) {
    	 System.out.println("Speichere Entity: " + entity);
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
    
    
    public void deleteAnf(Long id,HttpSession session) {
    	userService.hasPermision(session, "delete_requirement");
    	Anforderung anf = getAnfById(id);
    	repository.delete(anf);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}