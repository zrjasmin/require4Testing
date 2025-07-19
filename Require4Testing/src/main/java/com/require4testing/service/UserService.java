package com.require4testing.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.require4testing.repository.BerechtigungRepository;
import com.require4testing.repository.RoleRepository;
import com.require4testing.repository.UserRepository;
import com.require4testing.model.Berechtigung;
import com.require4testing.model.Role;
import com.require4testing.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private BerechtigungRepository berechtigungRepository;
    @Autowired
    private RoleRepository roleRep;

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
    	User user = repository.findById(id).get();
    	return user;
    }
    
    public User getCurrentUser(HttpSession session) {
    	User user =  (User) session.getAttribute("currentUser");
    	return user;
    }
    
    public boolean hasPermision(HttpSession session, String berechtigungName) {
    	User user =  (User) session.getAttribute("currentUser");
    	System.out.println(user);
    	
    	
    	for(Role role : user.getRoles()) {
    		for(Berechtigung berechtigung : role.getBerechtigungen()) {
    			if(berechtigung.getName().equals(berechtigungName)) {
    				System.out.println("haben Berechtigung");
    				return true;
    			}
    		}
    	}
    	System.out.println("keine Berechtigung");
    	return false;
    }
    
    
    
    
    
    
    public void initialiseUser() {
    	List<Berechtigung> initalBerechtigungen = new ArrayList<>();
    	initalBerechtigungen.add(new Berechtigung("create_requirement"));
    	initalBerechtigungen.add(new Berechtigung("edit_requirement"));
    	initalBerechtigungen.add(new Berechtigung("delete_requirement"));
    	initalBerechtigungen.add(new Berechtigung("create_test"));
    	initalBerechtigungen.add(new Berechtigung("edit_test"));
    	initalBerechtigungen.add(new Berechtigung("delete_test"));
    	initalBerechtigungen.add(new Berechtigung("assign_test"));
    	initalBerechtigungen.add(new Berechtigung("create_testlauf"));
    	initalBerechtigungen.add(new Berechtigung("delete_testlauf"));
    	initalBerechtigungen.add(new Berechtigung("edit_testlauf"));
    	initalBerechtigungen.add(new Berechtigung("assign_testlauf"));
    	initalBerechtigungen.add(new Berechtigung("run_test"));
    	initalBerechtigungen.add(new Berechtigung("run_testlauf"));
    	
    	//berechtigungRepository.saveAll(initalBerechtigungen);
    	
    	List<Role> roles = new ArrayList<>();
    	//Requirement Engineer
    	Set<Berechtigung> reBe = new HashSet<>();
    	reBe.add(berechtigungRepository.findByName("create_requirement"));
    	reBe.add(berechtigungRepository.findByName("edit_requirement"));
    	reBe.add(berechtigungRepository.findByName("delete_requirement"));
    	roles.add(new Role("Requirement Engineer", reBe));
    	
    	//Tester
    	Set<Berechtigung> testerBe = new HashSet<>();
    	testerBe.add(berechtigungRepository.findByName("run_testlauf"));
    	testerBe.add(berechtigungRepository.findByName("run_test"));
    
    	roles.add(new Role("Tester:in", testerBe));
    	
    	
    	Set<Berechtigung> testErstellerBe = new HashSet<>();
    	testErstellerBe.add(berechtigungRepository.findByName("create_test"));
    	testErstellerBe.add(berechtigungRepository.findByName("edit_test"));
    	testErstellerBe.add(berechtigungRepository.findByName("delete_test"));
    	roles.add(new Role("Testfallersteller:in", testErstellerBe));
    	
    	
    	Set<Berechtigung> managerBe = new HashSet<>();
    	
    	managerBe.add(berechtigungRepository.findByName("create_test"));
    	managerBe.add(berechtigungRepository.findByName("edit_test"));
    	managerBe.add(berechtigungRepository.findByName("delete_test"));
    	managerBe.add(berechtigungRepository.findByName("assign_test"));
    	managerBe.add(berechtigungRepository.findByName("create_testlauf"));
    	managerBe.add(berechtigungRepository.findByName("delete_testlauf"));
    	managerBe.add(berechtigungRepository.findByName("edit_testlauf"));
    	managerBe.add(berechtigungRepository.findByName("assign_testlauf"));
    	roles.add(new Role("Testmanager:in", managerBe));
    	
    	//roleRep.saveAll(roles);
    	
    	List<User> users = new ArrayList<>();
    	User user1 = new User("Jasmin", "email@gmx.net","/images/profiles/pic6.png");
    	User user2 = new User("Lukas", "email@gmx.net",  "/images/profiles/pic2.png");
    	User user3 = new User("Laura", "email@gmx.net", "/images/profiles/pic3.png");
    	User user4 = new User("Lena", "email@gmx.net",  "/images/profiles/pic4.png");
    	User user5 = new User("Thomas", "email@gmx.net",  "/images/profiles/pic5.png");
    	User user6 = new User("Niklas", "email@gmx.net","/images/profiles/pic6.png");
    	
    	user1.getRoles().add(roleRep.findByName("Requirement Engineer"));
    	user2.getRoles().add(roleRep.findByName("Testfallersteller:in"));
    	user2.getRoles().add(roleRep.findByName("Testmanager:in"));
    	user3.getRoles().add(roleRep.findByName("Tester:in"));
    	user4.getRoles().add(roleRep.findByName("Testfallersteller:in"));
    	user5.getRoles().add(roleRep.findByName("Testmanager:in"));
    	user6.getRoles().add(roleRep.findByName("Testfallersteller:in"));
    	user6.getRoles().add(roleRep.findByName("Requirement Engineer"));
    	
    	users.add(user1);
    	users.add(user2);
    	users.add(user3);
    	users.add(user4);
    	users.add(user5);
    	users.add(user6);
    	
    
    	
    	//repository.saveAll(users);
    	
    }
  
    
    
    
    
    
    
    
    
    
    
}