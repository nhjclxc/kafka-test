package com.example.kafkatest.test3_delay;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component(value = "Consumer-delay")
public class Consumer {

    @KafkaListener(topics = {"delay_topic"})
    public void consumer(ConsumerRecord<String, String> record) {

        Optional<ConsumerRecord<String, String>> record1 = Optional.ofNullable(record);
        if (record1.isPresent()) {
            String key = record.key();
            String value = record.value();

            long now = System.currentTimeMillis();
            System.out.println("消费者 当前: " + now);
            System.out.println("延迟消费  " + key + " ,, " + value);

        }
    }
}
