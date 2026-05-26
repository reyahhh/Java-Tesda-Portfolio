package com.app.gbank.controller;

import com.app.gbank.entity.User;
import com.app.gbank.enums.Role;
import com.app.gbank.service.*;
import com.app.gbank.dto.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("totalUsers", adminService.countUsers());
        model.addAttribute("totalTransactions", adminService.countTransactions());
        model.addAttribute("totalBalance", adminService.totalBalanceAllUsers());
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("auditLogs", adminService.getRecentLogs());
        model.addAttribute("pageTitle", "Admin Dashboard");
        return "admin/dashboard";
    }

    // View specific user account with all info and logs
    @GetMapping("/users/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("transactions", adminService.getUserTransactions(user));
        model.addAttribute("auditLogs", adminService.getUserAuditLogs(user));
        model.addAttribute("pageTitle", "User Details - " + user.getFullName());
        return "admin/user-detail";
    }

    // Add funds page
    @GetMapping("/users/{id}/add-funds")
    public String addFundsPage(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("addFundsRequest", new AddFundsRequest(null, ""));
        model.addAttribute("pageTitle", "Add Funds - " + user.getFullName());
        return "admin/add-funds";
    }

    @PostMapping("/users/{id}/add-funds")
    public String addFunds(@PathVariable Long id,
                        @Valid @ModelAttribute AddFundsRequest request,
                        BindingResult result,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addFundsRequest", result);
            redirectAttributes.addFlashAttribute("addFundsRequest", request);
            return "redirect:/admin/users/" + id + "/add-funds";
        }

        userService.addFunds(id, request);
        redirectAttributes.addFlashAttribute("success", "Funds added successfully.");
        return "redirect:/admin/users/" + id;
    }

    @GetMapping("/users/{id}/deduct-funds")
    public String deductFundsPage(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        if (!model.containsAttribute("deductFundsRequest")) {
            model.addAttribute("deductFundsRequest", new DeductFundsRequest(null, ""));
        }
        return "admin/deduct-funds";
    }

    // Deduct funds page
    @PostMapping("/users/{id}/deduct-funds")
    public String deductFunds(@PathVariable Long id,
                            @Valid @ModelAttribute DeductFundsRequest request,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.deductFundsRequest", result);
            redirectAttributes.addFlashAttribute("deductFundsRequest", request);
            return "redirect:/admin/users/" + id + "/deduct-funds";
        }

        userService.deductFunds(id, request);
        redirectAttributes.addFlashAttribute("success", "Funds deducted successfully.");
        return "redirect:/admin/users/" + id;
    }

    // Create new user page
    @GetMapping("/users/new")
    public String createUserPage(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest("", "", "", "", null));
        model.addAttribute("roles", List.of(Role.ROLE_USER, Role.ROLE_ADMIN));
        model.addAttribute("pageTitle", "Create New Account");
        return "admin/create-user";
    }

    @PostMapping("/users/new")
public String createUser(@Valid @ModelAttribute CreateUserRequest request,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
        redirectAttributes.addFlashAttribute(
                "org.springframework.validation.BindingResult.createUserRequest",
                result
        );
        redirectAttributes.addFlashAttribute("createUserRequest", request);
        return "redirect:/admin/users/new";
    }

    User user = userService.addUser(request);
    adminService.log("ADMIN", "USER_CREATED", "Created new user: " + user.getFullName());
    redirectAttributes.addFlashAttribute("success", "Account created successfully.");
    return "redirect:/admin";
}

    @PostMapping("/users/{id}/toggle")
    public String toggleUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.toggleEnabled(id);
        redirectAttributes.addFlashAttribute("success", "User status updated.");
        return "redirect:/admin";
    }
}