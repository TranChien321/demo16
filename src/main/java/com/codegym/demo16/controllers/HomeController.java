package com.codegym.demo16.controllers;

import com.codegym.demo16.dto.response.OpenWeatherResponse;
import com.codegym.demo16.services.OpenWeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final OpenWeatherService openWeatherService;

    public HomeController(OpenWeatherService openWeatherService, RestTemplate restTemplate) {
        this.openWeatherService = openWeatherService;
    }

    @GetMapping
    public String homePage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // Lấy email đăng nhập
            String userEmail = authentication.getName();
            model.addAttribute("userEmail", userEmail);
        }
        return "index"; // This will resolve to /WEB-INF/views/home.jsp
    }
}