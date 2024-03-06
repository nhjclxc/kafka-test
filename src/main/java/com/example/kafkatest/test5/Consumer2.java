package com.example.kafkatest.test5;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;


@Component(value = "test5-Consumer2")
public class Consumer2 {

    @KafkaListener(topics = {"kafka-test5"}, groupId = "kafka-test5-1",
            topicPartitions = {@TopicPartition(topic = "kafka-test5", partitions = {"1"})})
    public void consumer(ConsumerRecord<String, String> record) {
        String key = record.key();
        String value = record.value();

        System.out.println("消费者5-1 = " + key + " ,, " + value);

        System.out.println("消费者5-1 offset = " + record.offset());
        System.out.println("消费者5-1 partition = " + record.partition());

    }

}
