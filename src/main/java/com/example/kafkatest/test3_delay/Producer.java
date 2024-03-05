package com.example.kafkatest.test3_delay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController("Producer-delay")
@RequestMapping("/delay")
public class Producer {


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**

     Kafka 并没有内置的延迟消费功能，所以延迟消费的实现通常是在消息生成端进行的，而不是在消费端。这意味着你需要在发送消息时设置延迟，以便消息在指定的延迟时间后才被消费者接收到。

     一种常见的方法是在发送消息时使用 Kafka 提供的延迟发送功能，通过设置消息的时间戳或者延迟时间来实现延迟。例如，你可以在生产者端设置消息的时间戳，然后在消费者端通过配置来使得消费者只消费那些时间戳符合一定条件（例如，在当前时间之后）的消息。

     另一种方法是在生产者端实现自定义的延迟逻辑，例如，将消息发送到一个延迟主题中，然后在指定的时间后再将消息转移到实际消费的主题中。

     在任何情况下，延迟消费的实现都是在消息生成端进行的。消费端只需按照正常流程接收和处理消息即可。


     */
    @GetMapping
    public void test() throws ExecutionException, InterruptedException {
        long now = System.currentTimeMillis();
        System.out.println("生产者 当前: " + now);
        long timer = now + 5 * 1000;
        System.out.println("生产者 加5s后: " + timer);
        SendResult<String, String> sendResult = kafkaTemplate.send(
                "delay_topic",
                0,
                timer,
                "mykey",
                "hello springboot kafka-delay").get();

        System.out.println("发送结果: " + sendResult.toString());


    }

}
