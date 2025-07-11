package com.require4testing.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.require4testing.repository.UserRepository;
import com.require4testing.model.Role;
import com.require4testing.model.User;

import java.util.List;

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
   
    
    public void neuerUserSpeichern() {
        // Neue Entität erstellen
    	User neuerUser = new User("Jamin", "jj", Role.TEST_FALLERSTELLR);

        // Speichern in der Datenbank
    	repository.save(neuerUser);

        System.out.println("User gespeichert mit ID: " + neuerUser.getId());
    }
}