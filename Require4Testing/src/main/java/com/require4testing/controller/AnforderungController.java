package com.require4testing.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.require4testing.dto.AnforderungDto;
import com.require4testing.dto.KriteriumDto;
import com.require4testing.dto.TestDto;
import com.require4testing.model.AnfKategorie;
import com.require4testing.model.Anforderung;
import com.require4testing.model.Kriterium;
import com.require4testing.model.Prioritaet;
import com.require4testing.model.Test;
import com.require4testing.model.Testschritt;
import com.require4testing.model.User;
import com.require4testing.repository.KriteriumRepository;
import com.require4testing.repository.AnforderungRepository;
import com.require4testing.service.AnforderungService;
import com.require4testing.service.KriteriumService;
import com.require4testing.service.TestService;
import com.require4testing.service.UserService;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


@Controller
@RequestMapping("/anforderung")
public class AnforderungController {

	@Autowired
    private  AnforderungService service;
	@Autowired
	private UserService userService;
	@Autowired 
    private TestService testService;
	
    private  UtilController util = new UtilController();
	

	@GetMapping("/all") 
	public String alleAnforderungen(Model model) {
		model.addAttribute("anforderungen", service.alleEntities());
		
		util.setPageModelAttributes(model, "Anforderungen", "anforderungen", "","/css/uebersicht.css", "");
		return "layout";
	}
	
	//wechselt auf die Detailseite einer Anforderung 
		@GetMapping("/detail/{id}")
		public String zeigeDetails(@PathVariable Long id, Model model) {
			Anforderung anforderung = service.getAnfById(id);
			util.setPageModelAttributes(model, "Anforderungen: Detail", "anforderung_detail", "","/css/form.css", "/css/anforderung.css");

			model.addAttribute("anforderung", anforderung);
			List<Test> verknüpfteTest = new ArrayList<>();
			for(Test t: testService.alleEntities()) {
	    		if(t.getAnforderung().getId() == id) {
	    			verknüpfteTest.add(t);
	    		}
	    	}
			model.addAttribute("verknuepfteTest", verknüpfteTest);
			return "layout";
		}
	
	
	@GetMapping("/edit")
	public String zeigNeueForm(Model model, HttpSession session) {
		userService.hasPermision(session, "create_requirement");
		model.addAttribute("currentUser", userService.getCurrentUser(session));
		model.addAttribute("kategorien", AnfKategorie.values());
		model.addAttribute("prioritaeten",Prioritaet.values());
		model.addAttribute("anforderung", new AnforderungDto());
		model.addAttribute("anforderung", new Anforderung());
		
		util.setPageModelAttributes(model, "Anforderungen: Neu", "anforderung_neu", "/script.js","/css/form.css", "/css/anforderung.css");

		return "layout";
	}
	
	
	
	//zeigt Bearbeitungsseite
	@GetMapping("/edit/{id}")
	public String zeigEditForm(@PathVariable Long id, Model model, HttpSession session) {
		
		userService.hasPermision(session, "edit_requirement");
		
		Anforderung anforderung = service.getAnfById(id);
		model.addAttribute("anforderung", anforderung);
		
		
		AnforderungDto dto = service.convertToDto(anforderung);
		model.addAttribute("currentUser", userService.getCurrentUser(session));
		model.addAttribute("editdto",dto);
		model.addAttribute("kategorien", AnfKategorie.values());
		model.addAttribute("prioritaeten",Prioritaet.values());
		
		util.setPageModelAttributes(model, "Anforderungen: Bearbeiten", "anforderung_form","/script.js", "/css/form.css", "/css/anforderung.css");
	
		
		return "layout";
	}
	
	
	//Handhabt die Navigation nach Updaten und Löschen
		@PostMapping("/handleForm")
		public String handleForm( 
				@ModelAttribute @Valid AnforderungDto anfDto, 
	    		BindingResult result,
	    		@RequestParam String action,
	    		HttpSession session,
	    		Model model,
	    		@RequestParam("erstellerId") Long erstellerId) {
			
			if(result.hasErrors()) {
				System.out.println("Fehler update");

				userService.hasPermision(session, "create_requirement");
				model.addAttribute("currentUser", userService.getCurrentUser(session));
				model.addAttribute("editdto", anfDto);
				model.addAttribute("prioritaeten",Prioritaet.values());
				model.addAttribute("kategorien", AnfKategorie.values());
				util.setPageModelAttributes(model, "Anforderungen: bearbeiten", "anforderung_form", "/script.js","/css/form.css", "/css/anforderung.css");
				
				 result.getFieldErrors().forEach(error -> {
				        System.out.println("Feld: " + error.getField() + " - " + error.getDefaultMessage());
				    });
				
				return"layout";
				
			}
			return "redirect:/anforderung/all";
			
		/*	if("speichern".equals(action)) {
				//Validierung der Eingaben
				
				
				//Neue Anforderung erstellen
				if(anfDto.getId() == null) {
					
					
					//saveAnforderung(anfDto,result,erstellerId, session, model);
					 
				//Anforderung update
				} else if(anfDto.getId() != null) {
					
					
					updateAnf(anfDto.getId(), anfDto, model);
				}
				
			
			//Anforderung löschen
			} else if("loeschen".equals(action)) {
				deleteAnf(anfDto.getId(),session);
			}
			return "redirect:/anforderung/all";
			*/
		}
		
	

	@Transactional
	@PostMapping("/update/{id}")
    public String updateAnf(
    		@PathVariable Long id, 
    		@ModelAttribute AnforderungDto anfDto,
    		Model model) {
		
		service.updateAnf(id, anfDto);
		return "redirect:/anforderung/detail/"+ id;
    }
	
	


	
	
	
	@PostMapping("/save")
    public String saveAnforderung( 
    		@ModelAttribute @Valid Anforderung anf, 
    		BindingResult result,
    		@RequestParam("erstellerId") Long erstellerId,
    		HttpSession session,
    		Model model) {
    	
		
    	if(result.hasErrors()) {
			System.out.println("Fehler update");

			userService.hasPermision(session, "create_requirement");
			model.addAttribute("currentUser", userService.getCurrentUser(session));
			model.addAttribute("anforderung", anf);
			
			
			model.addAttribute("prioritaeten",Prioritaet.values());
			model.addAttribute("kategorien", AnfKategorie.values());
			util.setPageModelAttributes(model, "Anforderungen: Neu", "anforderung_neu", "/script.js","/css/form.css", "/css/anforderung.css");
			
			 result.getFieldErrors().forEach(error -> {
			        System.out.println("Feld: " + error.getField() + " - " + error.getDefaultMessage());
			    });
			
			return"layout";
			
		}
		service.saveNewAnf(anf, erstellerId);
		
		
		
        return "redirect:/anforderung/all";
     
    }
	
	@PostMapping("/delete/{id}")
	public String deleteAnf(@PathVariable Long id, HttpSession session) {
		
		service.deleteAnf(id, session);
		return "redirect:/anforderung/all";
		
	}
	
	
	
	
	
}

