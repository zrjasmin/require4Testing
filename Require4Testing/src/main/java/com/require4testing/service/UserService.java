package com.require4testing.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.require4testing.repository.UserRepository;
import com.require4testing.model.Role;
import com.require4testing.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> alleEntities() {
        return repository.findAll();
    }

    public User speichereEntity(User entity) {
        return repository.save(entity);
    }
   
    
    public void neuerUserSpeichern(User user) {
        // Speichern in der Datenbank
    	repository.save(user);

       
    }
    
    public User findById(Long id) {
    	Optional<User> optUser = repository.findById(id);
    	User user = null;
    	if(optUser.isPresent()) {
    		user = optUser.get();
    	}
    	
    	return user;
    }
}