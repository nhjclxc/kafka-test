package com.example.kafkatest.test7;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;


@Component(value = "test7-Consumer")
public class Consumer {


    /**
     以下注解的意思是这个消费者属于"kafka-test7-group"这个消费组,
        消费了两个主题,分别是"kafka-test7-0"和"kafka-test7-1"
            消费了"kafka-test7-0"主题的0,2分区,
            消费了"kafka-test7-1"主题的0分区,
     concurrency就是同组下的消费者个数,就是并发消费的消费数,一般设置值小于分区个数

     @KafkaListener(groupId = "kafka-test7-group",
         topicPartitions = {
             @TopicPartition(topic = "kafka-test7-0", partitions = {"0", "2"}),
             @TopicPartition(topic = "kafka-test7-1", partitions = {"0"}),
         },concurrency = 3)
     */

    @KafkaListener(topics = {"kafka-test7"}, groupId = "kafka-test7-0",
            topicPartitions = {@TopicPartition(topic = "kafka-test7", partitions = {"0"})})
    public void consumer(ConsumerRecord<String, String> record) {

        String key = record.key();
        String value = record.value();

        System.out.println("消费者7-0 = " + key + " ,, " + value);

        System.out.println("消费者7-0 offset = " + record.offset());
        System.out.println("消费者7-0 partition = " + record.partition());
    }

    @KafkaListener(topics = {"kafka-test7"}, groupId = "kafka-test7-1",
            topicPartitions = {@TopicPartition(topic = "kafka-test7", partitions = {"1"})})
    public void consumer1(ConsumerRecord<String, String> record) {

        String key = record.key();
        String value = record.value();

        System.out.println("消费者7-1 = " + key + " ,, " + value);

        System.out.println("消费者7-1 offset = " + record.offset());
        System.out.println("消费者7-1 partition = " + record.partition());
    }

}
