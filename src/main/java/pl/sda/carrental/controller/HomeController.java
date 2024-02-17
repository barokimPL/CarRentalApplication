package pl.sda.carrental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class HomeController {

    @GetMapping("/")
    public String greeting() {
        return "home";
    }

    @GetMapping("/oops")
    public String notImplementedYet(Model model, @RequestParam(required = false) String message) {
        model.addAttribute("message", Objects.requireNonNullElse(message, "Oops! Something went wrong!"));

        return "/errorPages/oops"; }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}
