package com.example.kafkatest.test10;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component(value = "test10-Consumer")
public class Consumer {

    @KafkaListener(topics = {"kafka-test10"}, groupId = "kafka-test10")
    public void consumer(ConsumerRecord<String, String> record) {

        String key = record.key();
        String value = record.value();

        System.out.println("消费者 test10 = " + key + " ,, " + value);

        System.out.println("消费者 test10 offset = " + record.offset());
        System.out.println("消费者 test10 partition = " + record.partition());


    }

}
