package com.example.stocktracking.controllers;

import com.example.stocktracking.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {
    private final KafkaProducer producer;

    @Autowired
    public KafkaController(KafkaProducer producer){
        this.producer = producer;
    }

    @PostMapping()
    public void messageToTopic(@RequestParam("message") String message){
        this.producer.send("test_topic" , message);
    }
}