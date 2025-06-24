package com.require4testing.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.require4testing.repository.AkzeptanzkriteriumRepository;



@Controller
@RequestMapping("/kriterien")
public class AkzeptanzkriteriumController {
	@Autowired
    private AkzeptanzkriteriumRepository repository;
	
	
	 
	 
	
}