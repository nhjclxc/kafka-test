package com.example.kafkatest.test8;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


@Component(value = "test8-Consumer")
public class Consumer {

    @KafkaListener(topics = {"kafka-test8"}, groupId = "kafka-test8")
    public void consumer(ConsumerRecord<String, String> record, Acknowledgment ack) {

        String key = record.key();
        String value = record.value();

        System.out.println("消费者 test8 = " + key + " ,, " + value);

        System.out.println("消费者 test8 offset = " + record.offset());
        System.out.println("消费者 test8 partition = " + record.partition());



        System.out.println("消费者消息消费完成");

        // 手动提交消息消费完成
        ack.acknowledge();

    }

}
