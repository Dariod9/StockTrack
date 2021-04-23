package com.example.stocktracking.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private static final String TOPIC = "test_topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String message){
        logger.info("Sending message:'{}'", message);
        kafkaTemplate.send(TOPIC, message);
    }

}
