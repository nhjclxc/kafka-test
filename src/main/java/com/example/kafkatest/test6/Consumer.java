package com.example.kafkatest.test6;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


@Component(value = "test6-Consumer")
public class Consumer {

    @KafkaListener(topics = {"kafka-test6"})
    public void consumer(ConsumerRecord<String, String> record) {

        String key = record.key();
        String value = record.value();

        System.out.println("test6-0 = " + key + " ,, " + value);


        System.out.println("test6-0 offset = " + record.offset());
        System.out.println("test6-0 partition = " + record.partition());

        // 手动提交消费完成
//        ack.acknowledge();
    }

}
