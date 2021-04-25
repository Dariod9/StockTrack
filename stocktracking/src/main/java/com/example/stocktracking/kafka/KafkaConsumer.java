package com.example.stocktracking.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private List<String> logs = new LinkedList<>();
    private List<String> events = new LinkedList<>();

    private String event_notification;

    public List<String> getLogs(){
        return this.logs;
    }

    public List<String> getEvents(){
        return this.events;
    }


    @KafkaListener(topics = "test_topic", groupId = "group_id")
    private void consume(String message){
        logger.info("Message received : " + message);
        logs.add(message);
    }

    @KafkaListener(topics = "event", groupId = "group_id")
    private void consume_event(String message){
        this.event_notification = message;
        logger.info("Message received : " + message);
        events.add(message);
    }
}
