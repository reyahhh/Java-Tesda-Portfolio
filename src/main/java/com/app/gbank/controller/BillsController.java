package com.app.gbank.controller;

import com.app.gbank.dto.BillPaymentRequest;
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

import java.util.List;

@Controller
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillsController {

    private final TransactionService transactionService;
    private final UserService userService;

    private static final List<String> BILLERS = List.of(
            "Meralco", "Manila Water", "PLDT", "Globe", "Smart",
            "PhilHealth", "SSS", "Pag-IBIG", "BIR", "Converge"
    );

    @GetMapping
    public String billsPage(Model model) {
        model.addAttribute("billPaymentRequest",
                new BillPaymentRequest("", "", null));
        model.addAttribute("billers", BILLERS);
        model.addAttribute("pageTitle", "Pay Bills");
        return "bills/pay";
    }

    @PostMapping
    public String payBill(@AuthenticationPrincipal UserPrincipal principal,
                          @Valid @ModelAttribute BillPaymentRequest billPaymentRequest,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("billers", BILLERS);
            model.addAttribute("pageTitle", "Pay Bills");
            return "bills/pay";
        }
        User user = userService.findByMobileNumber(principal.getUsername());
        transactionService.payBill(user, billPaymentRequest);
        redirectAttributes.addFlashAttribute("success",
                "Bill payment to " + billPaymentRequest.billerName() + " successful!");
        return "redirect:/dashboard";
    }
}