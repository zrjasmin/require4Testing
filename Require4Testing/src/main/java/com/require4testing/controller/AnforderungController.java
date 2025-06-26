package com.require4testing.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@GetMapping("/edit")
	public String zeigNeueForm(Model model) {
		model.addAttribute("anforderung", new Anforderung());
		
		return "anforderung_neu";
	}
	
	

	
	@GetMapping("/edit/{id}")
	public String zeigEditForm(@PathVariable Long id, Model model) {
		Anforderung anforderung = service.getAnfById(id);
		model.addAttribute("anforderung", anforderung);
		return "anforderung_bearbeiten";
	}
	
	

	@PostMapping("/update/{id}")
    public String updateAnf(@PathVariable Long id, @ModelAttribute Anforderung anforderung) {
		Optional<Anforderung> optAnf = repository.findById(id);
		
		if(optAnf.isPresent()) {
			Anforderung gespeicherteAnf = optAnf.get();
			gespeicherteAnf.setTitle(anforderung.getTitle());
			gespeicherteAnf.setBeschreibung(anforderung.getBeschreibung());
			service.speichereEntity(gespeicherteAnf);
			return "redirect:/anforderung/detail/" + id;
		} else {
			return "error";
		}
		
    	

    	
    }
	
	
	
	@PostMapping("/save")
    public String verarbeiteForm(@ModelAttribute Anforderung anforderung) {
		repository.save(anforderung);
		//service.speichereEntity(anforderung);
        return "redirect:/anforderung/all";
     
    }
}

