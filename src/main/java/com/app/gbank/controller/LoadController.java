package com.app.gbank.controller;

import com.app.gbank.dto.LoadPurchaseRequest;
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
@RequestMapping("/load")
@RequiredArgsConstructor
public class LoadController {

    private final TransactionService transactionService;
    private final UserService userService;

    private static final List<String> NETWORKS = List.of("Globe", "Smart", "TNT", "DITO", "SUN");

    @GetMapping
    public String loadPage(Model model) {
        model.addAttribute("loadPurchaseRequest",
                new LoadPurchaseRequest("", "", null));
        model.addAttribute("networks", NETWORKS);
        model.addAttribute("pageTitle", "Buy Load");
        return "load/buy";
    }

    @PostMapping
    public String buyLoad(@AuthenticationPrincipal UserPrincipal principal,
                          @Valid @ModelAttribute LoadPurchaseRequest loadPurchaseRequest,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("networks", NETWORKS);
            model.addAttribute("pageTitle", "Buy Load");
            return "load/buy";
        }
        User user = userService.findByMobileNumber(principal.getUsername());
        transactionService.buyLoad(user, loadPurchaseRequest);
        redirectAttributes.addFlashAttribute("success",
                "₱" + loadPurchaseRequest.amount() + " " + loadPurchaseRequest.network()
                        + " load sent to " + loadPurchaseRequest.targetMobileNumber());
        return "redirect:/dashboard";
    }
}