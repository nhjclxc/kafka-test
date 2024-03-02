package com.example.kafkatest.test2;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class Consumer {

    @KafkaListener(topics = "kafka-test")
    public void consumer(ConsumerRecord<String, String> record) {

        Optional<ConsumerRecord<String, String>> record1 = Optional.ofNullable(record);
        if (record1.isPresent()) {
            String key = record.key();
            String value = record.value();

            System.out.println(key + " ,, " + value);

            if ("user".equals(key)){
                User user = JSON.parseObject(value, User.class);
                System.out.println("name = " + user.getName());
                System.out.println("age = " + user.getAge());
            }

        }
    }
}
