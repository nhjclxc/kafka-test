package com.example.kafkatest.test4;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component(value = "test4-Consumer")
public class Consumer {

    @KafkaListener(topics = {"kafka-test4"}, groupId = "kafka-test4-1")
    public void consumer(ConsumerRecord<String, String> record) {

        String key = record.key();
        String value = record.value();

        System.out.println("消费者4-1 = " + key + " ,, " + value);

    }

}
