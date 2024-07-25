package com.sample.rocketmq.ons2apache.consumer;

import com.aliyun.openservices.ons.api.*;
import com.sample.rocketmq.ons2apache.MqConfig;

import java.util.Properties;

/**
 * 使用ons-client通过老的方式连接开源mq发送消息，使用onsAddr的方式
 */
public class OldOnsClientSubApacheConsumerTest {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, MqConfig.ACCESS_KEY);
        properties.put(PropertyKeyConst.SecretKey, MqConfig.SECRET_KEY);
        properties.put(PropertyKeyConst.GROUP_ID, MqConfig.GROUP_ID);
        properties.put(PropertyKeyConst.ONSAddr, "http://localhost:9010/rocketmq/nsaddr4client-internal");
        Consumer consumer = ONSFactory.createConsumer(properties);

        //订阅主题
        consumer.subscribe(MqConfig.TOPIC, MqConfig.TAG, new MessageListener() {
            public Action consume(Message message, ConsumeContext consumeContext) {
                System.out.println(message.getTopic() + "@" + message.getTag() + "@" + message.getMsgID());
                System.out.println(new String(message.getBody()));
                return Action.CommitMessage;
            }
        });
        //启动消费者
        consumer.start();
    }

}
