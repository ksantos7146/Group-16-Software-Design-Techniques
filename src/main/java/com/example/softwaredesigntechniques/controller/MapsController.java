package com.example.softwaredesigntechniques.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class MapsController {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @GetMapping("/maps-api-key")
    public Map<String, String> getGoogleMapsApiKey() {
        return Map.of("apiKey", googleMapsApiKey);
    }
} 