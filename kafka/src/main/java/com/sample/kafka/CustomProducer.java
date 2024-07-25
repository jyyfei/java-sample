package com.sample.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class CustomProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        //kafka集群
        props.put("bootstrap.servers", "localhost:9092");
        //ack机制
        props.put("acks", "all");
        //重试次数
        props.put("retries", 1);

        //批次大小
        props.put("batch.size", 16384);
        //等待时间
        props.put("linger.ms", 1);

        //RecordAccumulator 缓冲区大小 props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        producer.send(new ProducerRecord<>("first_test", "1", "1"));
        producer.close();
    }
}