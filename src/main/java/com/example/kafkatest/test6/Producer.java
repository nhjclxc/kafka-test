package com.example.kafkatest.test6;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewPartitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController(value = "test6-Producer")
@RequestMapping("/test6")
public class Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 生产者同步发送和异步发送
     */
    @GetMapping
    public void test() throws ExecutionException, InterruptedException {

        // 同步发送
        kafkaTemplate.send(
                "kafka-test6",
                "mykey1",
                "hello 1").get();

        // 异步发送
        kafkaTemplate.send(
                "kafka-test6",
                "mykey",
                "生产者同步发送和异步发送").addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("消息发送失败");
            }

            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                System.out.println("消息发送成功");
            }


        });

    }

}
