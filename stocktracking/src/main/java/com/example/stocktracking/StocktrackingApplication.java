package com.example.stocktracking;

import com.example.stocktracking.controllers.KafkaController;
import com.example.stocktracking.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication()
@EnableScheduling
@EnableJpaRepositories
public class StocktrackingApplication {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaController kafkaController = new KafkaController((kafkaProducer));

    public static void main(String[] args) {
        SpringApplication.run(StocktrackingApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
