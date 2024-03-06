package com.example.kafkatest.test4;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController(value = "test4-Producer")
@RequestMapping("/test4")
public class Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /** 实现多播消息 */
    /** 不同的消费组订阅同一个topic,实现一个topic里面的消息被多个消费者消费 */


    /**
     * 一个消费组里面多个消费者能不能消费同一条消息???
     *      答案是不能的,只有后面上线的一个消费者才可以消费消息(在一个消费组里面只有一个消费者可以收到消息)
     *      即同一个消费组里面只有一个消费组可以收到同一个topic的消息
     *
     * 要想实现一个生产者发送的消息被多个消费者消费就必须使得多个消费者在不同的消费组里面,使用@KafkaListener里面的groupId实现消费者在不同的消费组里面
     *      @KafkaListener(topics = {"kafka-test4"}, groupId = "kafka-test4-1")
     *      @KafkaListener(topics = {"kafka-test4"}, groupId = "kafka-test4-2")
     *
     *
     */

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @GetMapping
    public void test() throws ExecutionException, InterruptedException {
        kafkaTemplate.send(
                "kafka-test4",
                "mykey",
                "hello springboot kafka").get();


    }

}
