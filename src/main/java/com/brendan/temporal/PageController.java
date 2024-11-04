package com.brendan.temporal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("message", "Welcome");
        return "list";
    }

    @GetMapping("/onboarding")
    public String onboard(Model model) {
        model.addAttribute("message", "Welcome");
        return "onboard";
    }
}
