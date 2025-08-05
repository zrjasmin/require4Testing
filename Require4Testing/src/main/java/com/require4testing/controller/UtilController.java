package com.require4testing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


public class UtilController {
	

	public void setPageModelAttributes(
				Model model,
				String pageTitel,
				String pageFile,
				String js,
				String css,
				String css2) {
			model.addAttribute("pageTitle", pageTitel);
			model.addAttribute("additionalCss", css);
			model.addAttribute("additionalCss2", css2);
			model.addAttribute("mainContent", pageFile);
			model.addAttribute("additionalJs", js);
	
			
		}
	

}