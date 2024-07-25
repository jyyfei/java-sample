package com.sample.rocketmq.ons2apache.consumer;

import com.aliyun.openservices.ons.api.*;
import com.sample.rocketmq.ons2apache.MqConfig;

import java.util.Properties;

/**
 * 使用ons-client连接开源mq消费消息
 */
public class OnsClientSubApacheConsumerTest {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, MqConfig.ACCESS_KEY);
        properties.put(PropertyKeyConst.SecretKey, MqConfig.SECRET_KEY);
        properties.put(PropertyKeyConst.GROUP_ID, MqConfig.GROUP_ID);
        properties.put(PropertyKeyConst.NAMESRV_ADDR, MqConfig.NAMESRV_ADDR);
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
