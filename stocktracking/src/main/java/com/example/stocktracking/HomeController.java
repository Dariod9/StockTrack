package com.example.stocktracking;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {


    @GetMapping("/blog")
    public String hello(@RequestParam(name="name2", required=false) int[] name2, Model model) {
        model.addAttribute("name2", name2);
        String[] continents = {
                "Africa", "Antarctica", "Asia", "Australia",
                "Europe", "North America", "Sourth America"
        };

        model.addAttribute("continents", continents);
        return "blog";
    }

    @GetMapping("/")
    public String index(Model model) {
        String[] continents = {
                "Africa", "Antarctica", "Asia", "Australia",
                "Europe", "North America", "Sourth America"
        };

        model.addAttribute("continents", continents);
        return "index";
    }
}
