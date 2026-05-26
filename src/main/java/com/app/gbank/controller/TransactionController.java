package com.app.gbank.controller;

import com.app.gbank.dto.SendMoneyRequest;
import com.app.gbank.entity.User;
import com.app.gbank.security.UserPrincipal;
import com.app.gbank.service.TransactionService;
import com.app.gbank.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    @GetMapping
    public String history(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        User user = userService.findByMobileNumber(principal.getUsername());
        model.addAttribute("transactions", transactionService.getTransactionHistory(user));
        model.addAttribute("currentUser", user);
        model.addAttribute("pageTitle", "Transaction History");
        return "transactions/history";
    }

    @GetMapping("/send")
    public String sendPage(Model model) {
        model.addAttribute("sendMoneyRequest",
                new SendMoneyRequest("", null, ""));
        model.addAttribute("pageTitle", "Send Money");
        return "transactions/send";
    }

    @PostMapping("/send")
    public String sendMoney(@AuthenticationPrincipal UserPrincipal principal,
                            @Valid @ModelAttribute SendMoneyRequest sendMoneyRequest,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Send Money");
            return "transactions/send";
        }
        User sender = userService.findByMobileNumber(principal.getUsername());
        transactionService.sendMoney(sender, sendMoneyRequest);
        redirectAttributes.addFlashAttribute("success",
                "₱" + sendMoneyRequest.amount() + " sent to " + sendMoneyRequest.recipientMobileNumber());
        return "redirect:/dashboard";
    }
}