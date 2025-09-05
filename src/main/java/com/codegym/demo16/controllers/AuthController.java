package com.codegym.demo16.controllers;

import com.codegym.demo16.services.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final HttpSession httpSession;

    public AuthController(AuthService authService,
                          HttpSession httpSession) {
        this.authService = authService;
        this.httpSession = httpSession;
    }


    @GetMapping("/login")
    public String showFormLogin(@RequestParam(name = "error", required = false) String error,
                                Model model) {
        if (error != null){
            model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
        }
        // Return the name of the view for the login page
        return "auth/login"; // This will resolve to /WEB-INF/views/login.html
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xoá toàn bộ session
        return "redirect:/auth/login"; // Chuyển về trang login
    }

}