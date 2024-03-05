package com.example.kafkatest.test2;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/test2")
public class Producer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping
    public void test() throws ExecutionException, InterruptedException {
        Object o = kafkaTemplate.send("kafka-test",
                "mykey",
                "hello springboot kafka").get();

        User user = User.builder().name("张三").age(18).build();
        String s = JSONObject.toJSONString(user);
        System.out.println("user = " + s);
        SendResult<String, String> user1 = kafkaTemplate.send("kafka-test", "user", s).get();
        System.out.println(user1.toString());


        SendResult<String, String> aDefault = kafkaTemplate.send("topic-default", "s").get();
        System.out.println("aDefault = " + aDefault);

    }

}
