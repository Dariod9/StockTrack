package com.example.stocktracking.controllers;

import com.example.stocktracking.kafka.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoggerController {

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @GetMapping("/logs")
    public String logging(Model model){
        System.out.println("---------Logs -> " + kafkaConsumer.getLogs());
        model.addAttribute("logs", kafkaConsumer.getLogs());

        return "logs";
    }
}
