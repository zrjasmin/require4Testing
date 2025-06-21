package com.require4testing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
   public class HomeController {

       @GetMapping("/")
       public String home(Model model) {
           model.addAttribute("message", "Willkommen bei Thymeleaf!");
           return "index"; // verweist auf src/main/resources/templates/index.html
       }
   }