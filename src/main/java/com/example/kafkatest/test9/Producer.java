package com.example.kafkatest.test9;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestController(value = "test9-Producer")
@RequestMapping("/test9")
public class Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 防止消息重复消费(保证消息的幂等性)
     *  使用mysql插入业务id作为主键,使用主键作为唯一标识
     *  在消费者端解决消息的幂等性问题,使用redis或者zookeeper的分布式锁【主流方案】
     */
    @GetMapping
    public void test() throws ExecutionException, InterruptedException {
        kafkaTemplate.send(
                "kafka-test9",
                "mykey test9",
                "hello test9").get();

    }

}
