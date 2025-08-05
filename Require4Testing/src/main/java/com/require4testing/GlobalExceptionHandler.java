package com.require4testing;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.require4testing.controller.UtilController;
import com.require4testing.exception.UnauthorizedException;

@ControllerAdvice
public class GlobalExceptionHandler {
	private  UtilController util = new UtilController();
    @ExceptionHandler(UnauthorizedException.class)
    public String handleAccessDeniedException(UnauthorizedException ex, Model model) {
        // Hier kannst du eine individuelle Fehlermeldung setzen
      
        util.setPageModelAttributes(model, "Kein Zutritt", "access-denied", "", "/css/uebersicht.css", "");
        return "layout"; // Name des Thymeleaf-Templates
    }
}