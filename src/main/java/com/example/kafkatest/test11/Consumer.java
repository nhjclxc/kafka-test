//package com.example.kafkatest.test11;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.TopicPartition;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.annotation.PartitionOffset;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.stereotype.Component;
//
//import java.time.Duration;
//
//
//@Component(value = "test11-Consumer")
//public class Consumer {
//
////    @KafkaListener(topics = {"kafka-test11"}, groupId = "kafka-test11"
////            ,@TopicPartition(topic = "topic",
////            partitionOffsets = {
////                    @PartitionOffset(partition = "0", initialOffset = offset),
////                    @PartitionOffset(partition = "1", initialOffset = "818000"),
////                    @PartitionOffset(partition = "2", initialOffset = "819500")
////            }
////    ))
//
//    static long offset = 0L;
//
//    static TopicPartition topicPartition = new TopicPartition("kafka-test11", 0);
//
//    static org.apache.kafka.clients.consumer.Consumer<String, String> consumer = null;
//    public Consumer(ConsumerFactory<String, String> consumerFactory){
//        Consumer.consumer = consumerFactory.createConsumer();
//    }
//
//    public static void main(String[] args) {
//
//        while (true){
//            consumer.seek(topicPartition, offset);
//            ConsumerRecords<String, String> poll = consumer.poll(Duration.ofSeconds(1));
//            for (ConsumerRecord<String, String> record : poll) {
//
//                String key = record.key();
//                String value = record.value();
//
//                System.out.println("消费者 test11 = " + key + " ,, " + value);
//
//                System.out.println("消费者 test11 offset = " + record.offset());
//                System.out.println("消费者 test11 partition = " + record.partition());
//
//
//                offset = record.offset();
//                System.out.println();
//            }
//
//        }
//
//    }
//    public void consumer(ConsumerRecord<String, String> record) {
//
//
//
//
//    }
//
//}
