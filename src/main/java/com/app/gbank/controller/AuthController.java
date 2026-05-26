package com.app.gbank.controller;

import com.app.gbank.dto.RegisterRequest;
import com.app.gbank.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null)  model.addAttribute("error", "Invalid mobile number or password.");
        if (logout != null) model.addAttribute("message", "You have been logged out.");
        model.addAttribute("pageTitle", "Login");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest("", "", "", "", ""));
        model.addAttribute("pageTitle", "Register");
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Register");
            return "auth/register";
        }
        try {
            userService.register(registerRequest);
            redirectAttributes.addFlashAttribute("message",
                    "Account created! Please log in.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Register");
            return "auth/register";
        }
    }
}