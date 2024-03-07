package com.example.kafkatest.test8;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewPartitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController(value = "test8-Producer")
@RequestMapping("/test8")
public class Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 防止消息丢失
     *  生产者:ack设置为1或-1/all可以防止消息丢失  . 当ack设置为all时,把min.insync,replicas配置成分区备份数
     *  消费者:把自动提交改为手动提交
     */
    @GetMapping
    public void test() throws ExecutionException, InterruptedException {
        kafkaTemplate.send(
                "kafka-test8",
                "mykey test8",
                "hello test8").get();

    }

}
