package com.sample.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

public class CustomComsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        //Kafka集群
        props.put("bootstrap.servers", "localhost:9092");
        //消费者组，只要 group.id 相同，就属于同一个消费者组
        props.put("group.id", "test_cg");
        //关闭自动提交 offset
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //消费者订阅主题
        consumer.subscribe(Collections.singletonList("first_test"));
        //消费者拉取数据
        ConsumerRecords<String, String> records = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : records) {
            System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
        //同步提交，当前线程会阻塞直到 offset 提交成功
        consumer.commitSync();
    }
}