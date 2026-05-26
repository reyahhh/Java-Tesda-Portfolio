package com.app.gbank.controller;

import com.app.gbank.entity.User;
import com.app.gbank.security.UserPrincipal;
import com.app.gbank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping
    public String profile(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        User user = userService.findByMobileNumber(principal.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "My Profile");
        return "profile/view";
    }
}