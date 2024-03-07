package com.example.kafkatest.test7;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewPartitions;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController(value = "test7-Producer")
@RequestMapping("/test7")
public class Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private AdminClient adminClient;


    /**
     * 消息的并发消费
     */
    @GetMapping
    public void test() throws ExecutionException, InterruptedException {
        Map<String, NewPartitions> newPartitions = new HashMap<>();
//       "kafka-test7"表示对应的yopic, 而totalCount表示这个topic要创建几个分区,最新的分区个数,这个数字是多少就会有多少个分区
//        注意分区编号是从0开始计数的
        newPartitions.put("kafka-test7", NewPartitions.increaseTo(2));
        adminClient.createPartitions(newPartitions);

        kafkaTemplate.send(
                "kafka-test7",
                0,
                "mykey1",
                "hello 1").get();

        kafkaTemplate.send(
                "kafka-test7",
                1,
                "mykey1",
                "hello 1").get();


    }

}
