//package com.example.kafkatest.test11;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.admin.AdminClient;
//import org.apache.kafka.clients.admin.NewPartitions;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.common.TopicPartition;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//
//@Slf4j
//@RestController(value = "test11-Producer")
//@RequestMapping("/test11")
//public class Producer {
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    /**
//     * 延迟队列
//     */
//    @GetMapping
//    public void test() throws ExecutionException, InterruptedException {
//
//        for (int i = 0; i < 10; i++) {
//            kafkaTemplate.send(
//                    "kafka-test11",
//                    "mykey test11",
//                    "hello test11   " + System.currentTimeMillis()).get();
//            Thread.sleep(1000);
//        }
//
//    }
//
//
//
//     long offset = 0L;
//
//     TopicPartition topicPartition = new TopicPartition("kafka-test11", 1);
//
//     @Autowired
//     ConsumerFactory<String, String> consumerFactory;
//
//
//    @Autowired
//    private AdminClient adminClient;
//    @GetMapping("2")
//    public  void ss() {
//        Map<String, NewPartitions> newPartitions = new HashMap<>();
////       "kafka-test11"表示对应的topic, 而totalCount表示这个topic要创建几个分区,最新的分区个数,这个数字是多少就会有多少个分区
////        注意分区编号是从0开始计数的
//        newPartitions.put("kafka-test11", NewPartitions.increaseTo(1));
//        adminClient.createPartitions(newPartitions);
//
//        org.apache.kafka.clients.consumer.Consumer<String, String> consumer = consumerFactory.createConsumer();
//        while (true){
////            consumer.seek(topicPartition, offset);
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
//
//}
