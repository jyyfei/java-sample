package com.sample.rocketmq.ons;

import com.aliyun.openservices.ons.api.*;

import java.util.Properties;

public class OnsClientSubAliYunMqConsumerTest {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                Properties properties = new Properties();
                properties.put(PropertyKeyConst.AccessKey, "xxx");
                properties.put(PropertyKeyConst.SecretKey, "xxx");
                properties.put(PropertyKeyConst.GROUP_ID, "GID_Service_B");
                properties.put(PropertyKeyConst.NAMESRV_ADDR, "http://xxx:8080");
                Consumer consumer = ONSFactory.createConsumer(properties);

                //订阅主题
                consumer.subscribe("Topic_A_Order", "*", new MessageListener() {
                    public Action consume(Message message, ConsumeContext consumeContext) {
                        System.out.println(message.getTopic() + "@" + message.getTag() + "@" + message.getMsgID());
                        System.out.println(new String(message.getBody()));
                        throw new RuntimeException("test");
                    }
                });
                //启动消费者
                consumer.start();
            }
        }).start();

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, "xxx");
        properties.put(PropertyKeyConst.SecretKey, "xxx");
        properties.put(PropertyKeyConst.GROUP_ID, "GID_Service_A");
        properties.put(PropertyKeyConst.NAMESRV_ADDR, "http://xxx:8080");
        Consumer consumer = ONSFactory.createConsumer(properties);

        //订阅主题
        consumer.subscribe("Topic_B_Order", "*", new MessageListener() {
            public Action consume(Message message, ConsumeContext consumeContext) {
                System.out.println(message.getTopic() + "@" + message.getTag() + "@" + message.getMsgID());
                System.out.println(new String(message.getBody()));
                throw new RuntimeException("test");
            }
        });
        //启动消费者
        consumer.start();
    }

}
