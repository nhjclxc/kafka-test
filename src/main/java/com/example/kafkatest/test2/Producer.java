package com.example.kafkatest.test2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/test2")
public class Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping
    public void test() throws ExecutionException, InterruptedException {
        Object o = kafkaTemplate.send("kafka-test",
                "mykey",
                "hello springboot kafka").get();
    }

}
