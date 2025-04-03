package com.example.softwaredesigntechniques.controller.web;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public Object handleError(HttpServletRequest request, Model model) {
        // Get error details
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        String errorMessage = exception != null ? exception.getMessage() : "Unknown error";
        
        // Check if this is an API request
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
            // Return JSON response for API requests
            return ResponseEntity.status(statusCode != null ? statusCode : 500)
                    .body(Map.of(
                            "error", errorMessage,
                            "status", statusCode != null ? statusCode : 500,
                            "path", request.getRequestURI()
                    ));
        }
        
        // For web requests, return the error page
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);
        
        // Log the error
        System.err.println("Error occurred: " + statusCode + " - " + errorMessage);
        if (exception != null) {
            exception.printStackTrace();
        }
        
        return "error";
    }
} 