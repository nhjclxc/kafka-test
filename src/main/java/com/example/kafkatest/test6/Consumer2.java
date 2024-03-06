//package com.example.kafkatest.test6;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.annotation.TopicPartition;
//import org.springframework.stereotype.Component;
//
//
//@Component(value = "test6-Consumer2")
//public class Consumer2 {
//
//    @KafkaListener(topics = {"kafka-test6"})
//    public void consumer(ConsumerRecord<String, String> record) {
//        String key = record.key();
//        String value = record.value();
//
//        System.out.println("test6-1 = " + key + " ,, " + value);
//
//        System.out.println("test6-1 offset = " + record.offset());
//        System.out.println("test6-1 partition = " + record.partition());
//
//    }
//
//}
