package com.example.softwaredesigntechniques.controller.web;

import com.example.softwaredesigntechniques.domain.event.Event;
import com.example.softwaredesigntechniques.domain.category.Category;
import com.example.softwaredesigntechniques.domain.registration.Registration;
import com.example.softwaredesigntechniques.domain.user.User;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import com.example.softwaredesigntechniques.service.event.EventService;
import com.example.softwaredesigntechniques.service.registration.RegistrationService;
import com.example.softwaredesigntechniques.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class EventWebController {

    private final EventService eventService;
    private final RegistrationService registrationService;
    private final UserService userService;

    @Autowired
    public EventWebController(EventService eventService, RegistrationService registrationService, UserService userService) {
        this.eventService = eventService;
        this.registrationService = registrationService;
        this.userService = userService;
    }

    @GetMapping("/vibe-events")
    public String index(Model model) {
        try {
            // Add empty events list to prevent errors if the JavaScript fetch fails
            model.addAttribute("featuredEvents", List.of());
            
            // You can add more model attributes here if needed by the index template
            return "index";
        } catch (Exception e) {
            // Log the error
            System.err.println("Error rendering index page: " + e.getMessage());
            e.printStackTrace();
            
            // Add error message to the model
            model.addAttribute("error", "An error occurred while loading the home page. Please try again later.");
            return "index";
        }
    }

    @GetMapping("/events/all")
    public String allEvents() {
        return "events/all";
    }

    @GetMapping("/events/create-new")
    public String createEvent() {
        return "events/create";
    }

    @GetMapping("/events/details/{id}")
    public String eventDetails(@PathVariable Long id, Model model) {
        model.addAttribute("eventId", id);
        return "events/detail";
    }

    @GetMapping("/events/my-created-events")
    public String myEvents(Authentication authentication, Model model) {
        try {
            Optional<User> userOpt = userService.findByUsername(authentication.getName());
            if (userOpt.isEmpty()) {
                throw new NotFoundException("User not found");
            }
            User user = userOpt.get();
            
            // Since we don't have a direct method to find events by creator,
            // we'll use findAll and filter (not efficient but works for now)
            List<Event> allEvents = eventService.findAll();
            List<Event> userEvents = allEvents.stream()
                .filter(event -> event.getCreatedBy() != null && user.equals(event.getCreatedBy()))
                .collect(Collectors.toList());
            
            model.addAttribute("events", userEvents);
            return "events/my-events";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load your events: " + e.getMessage());
            model.addAttribute("events", List.of());
            return "events/my-events";
        }
    }

    @GetMapping("/events/my-registered-events")
    public String myRegistrations(Authentication authentication, Model model) {
        try {
            Optional<User> userOpt = userService.findByUsername(authentication.getName());
            if (userOpt.isEmpty()) {
                throw new NotFoundException("User not found");
            }
            User user = userOpt.get();
            
            // Get all registrations for this user
            List<Registration> registrations = registrationService.findByUserId(user.getId());
            
            // Convert registrations to events
            List<Event> events = new ArrayList<>();
            for (Registration reg : registrations) {
                Event event = reg.getEvent();
                if (event != null) {
                    events.add(event);
                }
            }
            
            model.addAttribute("events", events);
            return "events/my-registrations";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load your registrations: " + e.getMessage());
            model.addAttribute("events", List.of());
            return "events/my-registrations";
        }
    }

    @GetMapping("/events/categories-view")
    public String categories(Model model) {
        try {
            model.addAttribute("categories", Category.values());
            return "events/categories";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load categories: " + e.getMessage());
            return "events/categories";
        }
    }

    @GetMapping("/events/upcoming-view")
    public String upcomingEvents(Model model) {
        try {
            LocalDateTime now = LocalDateTime.now();
            List<Event> events = eventService.findByStartTimeBetween(now, now.plusMonths(3));
            model.addAttribute("events", events);
            model.addAttribute("pageTitle", "Upcoming Events");
            return "events/list";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load upcoming events: " + e.getMessage());
            model.addAttribute("events", List.of());
            model.addAttribute("pageTitle", "Upcoming Events");
            return "events/list";
        }
    }

    @GetMapping("/events/popular-view")
    public String popularEvents(Model model) {
        try {
            // For now, we'll just use the first 10 events as a placeholder
            List<Event> events = eventService.findAll().subList(0, Math.min(10, eventService.findAll().size()));
            model.addAttribute("events", events);
            model.addAttribute("pageTitle", "Popular Events");
            return "events/list";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load popular events: " + e.getMessage());
            model.addAttribute("events", List.of());
            model.addAttribute("pageTitle", "Popular Events");
            return "events/list";
        }
    }
} 