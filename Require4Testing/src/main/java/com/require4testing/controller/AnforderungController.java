package com.require4testing.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.require4testing.model.Akzeptanzkriterium;
import com.require4testing.model.Anforderung;
import com.require4testing.repository.AkzeptanzkriteriumRepository;
import com.require4testing.repository.AnforderungRepository;
import com.require4testing.service.AnforderungService;
import org.springframework.ui.Model; 


@Controller
@RequestMapping("/anforderung")
public class AnforderungController {

	@Autowired
    private  AnforderungService service;
	@Autowired
    private  AnforderungRepository repository;
	@Autowired
    private  AkzeptanzkriteriumRepository akzRepository;
	
	@GetMapping("/kk") 
	public String zeigeAnforderungen() {
		service.alleEntities();
		return "anforderungen";
	}
	
	
	
	@GetMapping("/all") 
	public String alleAnforderungen(Model model) {
		model.addAttribute("anforderungen", repository.findAll());
		return "anforderungen";
	}
	
	//wechselt auf die Detailseite einer Annn j 
	@GetMapping("/detail/{id}")
	public String zeigeDetails(@PathVariable Long id, Model model) {
		Anforderung anforderung = service.getAnfById(id);
		model.addAttribute("anforderung", anforderung);
		return "anforderung_detail";
	}
	
	
	
	
	@GetMapping("kriterienListe")
	public List<String> kriterienListe() {
		return new ArrayList<>();
    }
	
	@GetMapping("/neu")
	public String zeigeForm() {
		return "anforderungForm";
	}
	
	
	@GetMapping("/edit")
	public String zeigEditForm(@RequestParam(required = false) Long id, Model model) {
		if(id == null) {
			model.addAttribute("anforderung", new Anforderung());
		} else {
			Anforderung anforderung = service.getAnfById(id);
			model.addAttribute("anforderung", anforderung);
		}
		
		return "anforderung_bearbeiten";
	}
	

	@GetMapping("/neueAnforderung/{id}")
	public String zeigeForm(@PathVariable Long id, Model model) {
		if(id == null) {
			model.addAttribute("anforderung", new Anforderung());
		} else {
			Anforderung anforderung = service.getAnfById(id);
			model.addAttribute("anforderung", anforderung);
		}
		
		return "neueAnforderung";
	}
	
	
	@PostMapping("/verarbeiten")
    public String verarbeiteForm(@ModelAttribute("anforderung") Anforderung anforderung) {

		service.speichereEntity(anforderung);
        	
           
  
     
        return "anforderungen";
     
    }
}

