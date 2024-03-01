//package com.example.kafkatest.test1;
//
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Properties;
//import java.util.concurrent.ExecutionException;
//
//@RestController
//@RequestMapping("/test1")
//public class Producer {
//
//    @GetMapping
//    public void test() throws ExecutionException, InterruptedException {
//
//        Properties props = new Properties();
//        //kafka的服务器
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "106.14.142.184:9092");
//        //key的序列化StringSerializer序列化
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//        //value的序列化
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//        //确认方式
//        props.put(ProducerConfig.ACKS_CONFIG, "all");
//        //失败重试次数
//        props.put(ProducerConfig.RETRIES_CONFIG, 3);
//        //批量发送的最大消息数
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        //多久发一次消息
//        props.put("linger.ms", 1);
//        //创建kafka客户端
//        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(props);
//
//        //发送消息
//        String topic = "kafka-demo";
//        String message = "hello kafka";
//        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
//        RecordMetadata recordMetadata = kafkaProducer.send(record).get();
//        int partition = recordMetadata.partition();//分区
//        long offset = recordMetadata.offset();//发送消息的偏移量
//        kafkaProducer.close();
//        System.out.println("partition = " + partition + ", offset = " + offset);
//
//    }
//}
