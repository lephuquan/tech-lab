package com.techlab.kafkaretrydlq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaRetryDlqApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaRetryDlqApplication.class, args);
    }
}
