package com.example.kafkatest.test9;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


@Component(value = "test9-Consumer")
public class Consumer {

    @KafkaListener(topics = {"kafka-test9"}, groupId = "kafka-test9")
    public void consumer(ConsumerRecord<String, String> record) {

        String key = record.key();
        String value = record.value();

        System.out.println("消费者 test9 = " + key + " ,, " + value);

        System.out.println("消费者 test9 offset = " + record.offset());
        System.out.println("消费者 test9 partition = " + record.partition());


    }

}
