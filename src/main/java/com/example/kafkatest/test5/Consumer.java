package com.example.kafkatest.test5;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;


@Component(value = "test5-Consumer")
public class Consumer {

    @KafkaListener(topics = {"kafka-test5"}, groupId = "kafka-test5-0",
            topicPartitions = {@TopicPartition(topic = "kafka-test5", partitions = {"0"})})
    public void consumer(ConsumerRecord<String, String> record) {

        String key = record.key();
        String value = record.value();

        System.out.println("消费者5-0 = " + key + " ,, " + value);


        System.out.println("消费者5-0 offset = " + record.offset());
        System.out.println("消费者5-0 partition = " + record.partition());
    }

}
