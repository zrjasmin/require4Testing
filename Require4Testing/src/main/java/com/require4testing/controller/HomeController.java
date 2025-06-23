package com.require4testing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.require4testing.model.Anforderung;
import com.require4testing.service.AnforderungService;

@Controller
   public class HomeController {

	@Autowired
    private AnforderungService anforderungService;
	
       @GetMapping("/")
       public String home(Model model) {
           model.addAttribute("message", "Willkommen bei Thymeleaf!");
           return "index"; // verweist auf src/main/resources/templates/index.html
       }
       
       @GetMapping("/neueAnforderung")
       public String zeigeFormular(Model model) {
           model.addAttribute("anforderung", new Anforderung());
           return "neueAnforderung"; // Name des HTML-Templates
       }

       @PostMapping("/neueAnforderung")
       public String speichereTestfall(@ModelAttribute Anforderung anforderung) {
    	   anforderungService.speichereEntity(anforderung);
    	   
           // Hier kannst du den Testfall speichern (z.B. in einer Datenbank)
           // Für jetzt nur eine Bestätigung oder Weiterleitung
    	   
    	   
    	   
    	   
           return "redirect:/anforderungen"; // Beispiel: Zurück zur Übersicht
       }
   }