package com.example.softwaredesigntechniques.controller.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get error details
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        String errorMessage = exception != null ? exception.getMessage() : "Unknown error";
        
        // Add error details to model
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);
        
        // Log the error
        System.err.println("Error occurred: " + statusCode + " - " + errorMessage);
        if (exception != null) {
            exception.printStackTrace();
        }
        
        // Return custom error page
        return "error";
    }
} 