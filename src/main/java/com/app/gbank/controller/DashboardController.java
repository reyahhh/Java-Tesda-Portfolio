package com.app.gbank.controller;

import com.app.gbank.entity.User;
import com.app.gbank.security.UserPrincipal;
import com.app.gbank.service.TransactionService;
import com.app.gbank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final TransactionService transactionService;

    @GetMapping
    public String dashboard(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        User user = userService.findByMobileNumber(principal.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("recentTransactions",
                transactionService.getRecentTransactions(user));
        model.addAttribute("pageTitle", "Dashboard");
        return "dashboard/index";
    }
}