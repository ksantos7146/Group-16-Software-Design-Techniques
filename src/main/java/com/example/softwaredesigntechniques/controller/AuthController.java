package com.example.softwaredesigntechniques.controller;

import com.example.softwaredesigntechniques.endpoint.user.UserEndpoint;
import com.example.softwaredesigntechniques.dto.user.UserDto;
import com.example.softwaredesigntechniques.dto.user.UserRequest;
import com.example.softwaredesigntechniques.dto.user.LoginRequest;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserEndpoint userEndpoint;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userRequest", new UserRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userRequest") UserRequest userRequest,
                          BindingResult bindingResult,
                          Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        if (!userRequest.isPasswordMatching()) {
            model.addAttribute("error", "Passwords do not match");
            return "auth/register";
        }

        try {
            userEndpoint.add(userRequest);
            return "redirect:/auth/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<UserDto> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(userEndpoint.login(loginRequest));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 