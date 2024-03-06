package com.example.kafkatest.test5;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreatePartitionsResult;
import org.apache.kafka.clients.admin.NewPartitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController(value = "test5-Producer")
@RequestMapping("/test5")
public class Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaAdmin kafkaAdmin;
    @Autowired
    private AdminClient adminClient;

    /**
     * topic和分区的概念
     *
     * 要想往一个topic里面的不同分区发送数据,必须先创建分区,之后在对应的分区上发送数据
     *
     *
     * 注意消费同一个topic的不同分区的消费者必须要在不同的消费组中,即消费者注解必须加上, groupId = "kafka-test5-0",.[https://blog.csdn.net/lijiewen2017/article/details/128322053]
     *
     *
     * 消费者注解上面通过topicPartitions = {@TopicPartition(topic = "kafka-test5", partitions = {"0"})}来区分不同的分区
     */
    @GetMapping
    public void test() throws ExecutionException, InterruptedException {
        Map<String, NewPartitions> newPartitions = new HashMap<>();
//       "kafka-test5"表示对应的yopic, 而totalCount表示这个topic要创建几个分区,最新的分区个数,这个数字是多少就会有多少个分区
//        注意分区编号是从0开始计数的
        newPartitions.put("kafka-test5", NewPartitions.increaseTo(2));
        adminClient.createPartitions(newPartitions);

        kafkaTemplate.send(
                "kafka-test5",
                0,
                "mykey0",
                "hello 0").get();

        kafkaTemplate.send(
                "kafka-test5",
                1,
                "mykey1",
                "hello 1").get();

    }
//
//    @GetMapping("/send/{input}")
//    public void sendFoo(@PathVariable String input) {
//        kafkaTemplate.send("topic-kl", input);
//    }
//
//    @KafkaListener(id = "webGroup", topics = "topic-kl")
//    public String listen(String input) {
//        log.info("input value: {}", input);
//        throw new RuntimeException("dlt");
//    }
//
//    @KafkaListener(id = "dltGroup", topics = "topic-kl.DLT")
//    public void dltListen(String input) {
//        log.info("Received from DLT: " + input);
//    }

}
