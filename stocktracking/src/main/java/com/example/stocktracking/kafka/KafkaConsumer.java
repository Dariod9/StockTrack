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

    public List<String> getLogs(){
        return this.logs;
    }

    @KafkaListener(topics = "test_topic", groupId = "group_id")
    private void consume(String message){
        logger.info("Message received : " + message);
        logs.add(message);
    }
}
