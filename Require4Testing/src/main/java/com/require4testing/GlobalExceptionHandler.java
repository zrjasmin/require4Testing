package com.require4testing;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.require4testing.exception.UnauthorizedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public String handleAccessDeniedException(UnauthorizedException ex, Model model) {
        // Hier kannst du eine individuelle Fehlermeldung setzen
        model.addAttribute("errorMessage", "Du hast keine Berechtigung, diese Seite zu sehen.");
        return "access-denied"; // Name des Thymeleaf-Templates
    }
}