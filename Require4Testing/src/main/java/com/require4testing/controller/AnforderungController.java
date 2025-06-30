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
import org.springframework.validation.BindingResult; 


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
		Anforderung anforderung = repository.findById(id).get();
		
		if (anforderung.getAkzeptanzkriterien() == null) {
		    anforderung.setAkzeptanzkriterien(new ArrayList<>());
		}
		
		model.addAttribute("anforderung", anforderung);
		return "anforderung_bearbeiten";
	}
	
	

	
	
	

	@PostMapping("/update/{id}")
    public String updateAnf(@PathVariable Long id, @ModelAttribute(value= "anforderung") Anforderung anforderung) {
		Optional<Anforderung> optAnf = repository.findById(id);
		List<Akzeptanzkriterium> gespeichterKriterien = optAnf.get().getAkzeptanzkriterien();
		
		for (Akzeptanzkriterium k : gespeichterKriterien) {
            System.out.println("bestehendes Kriterium ID: " + k.getId() + ", Beschreibung: " + k.getBeschreibung());
        }
		
		
		List<Akzeptanzkriterium> neueListe = anforderung.getAkzeptanzkriterien();
		
		for (Akzeptanzkriterium k : anforderung.getAkzeptanzkriterien()) {
            System.out.println("Kriterium ID: " + k.getId() + ", Beschreibung: " + k.getBeschreibung());
        }
		
		if(optAnf.isPresent()) {
			for(int i = 0; i<neueListe.size(); i++) {
				System.out.println(neueListe.get(i));
			}
			
		}
		
		
		/*if(optAnf.isPresent()) {
			Anforderung gespeicherteAnf = optAnf.get();
			gespeicherteAnf.setTitle(anforderung.getTitle());
			gespeicherteAnf.setBeschreibung(anforderung.getBeschreibung());
			
			List<Akzeptanzkriterium> editedKriterien = anforderung.getAkzeptanzkriterien();
			
			
			
			for(Akzeptanzkriterium k : editedKriterien) {
				
				
				if(k.getId() != null) {
					Optional<Akzeptanzkriterium> bestehendesK = gespeicherteAnf.getAkzeptanzkriterien().stream().filter(e -> e.getId().equals(k.getId())).findFirst();
					if(bestehendesK.isPresent()) {
						bestehendesK.get().setBeschreibung(k.getBeschreibung());
						
					} 
				} 
			}
						
			service.speichereEntity(gespeicherteAnf);
			return "redirect:/anforderung/detail/"+ id;
		} else {
			return "error";
		}*/
		
		return "redirect:/anforderung/detail/"+ id;
    }
	
	
	
	@PostMapping("/save")
    public String verarbeiteForm(@ModelAttribute Anforderung anforderung) {
		repository.save(anforderung);
		//service.speichereEntity(anforderung);
        return "redirect:/anforderung/all";
     
    }
}

