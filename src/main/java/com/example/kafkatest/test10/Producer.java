package com.example.kafkatest.test10;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestController(value = "test10-Producer")
@RequestMapping("/test10")
public class Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 消息的顺序消费(先消费消息a,接着消费消息b,最后消费消息c)
     *
     *  生产者:ack不能设置为1,并且关闭消息重试
     *  消费方:将要顺序消费的消息发往一个topic的同一个Partition里面,并且保证只有一个消费者来消费
     *
     *
     */
    /**
     * 解决消息积压:
     *      1.使用多线程来实现消费者(在消费者方法中使用多线程来对消息进行消费)
     *      2.使用多个消费组的多个消费者的方式来实现消费速度的提升
     *      3.使用一个消费者将消息转发到其他topic上面让其他消费组进行消费
     *
     */
    @GetMapping
    public void test() throws ExecutionException, InterruptedException {
        kafkaTemplate.send(
                "kafka-test10",
                "mykey test10",
                "hello test10").get();

    }

}
