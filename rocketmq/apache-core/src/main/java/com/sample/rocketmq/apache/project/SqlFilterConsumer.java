package com.sample.rocketmq.apache.project;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;

public class SqlFilterConsumer {

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("GID_jyf_test");
        //local
//        consumer.setNamesrvAddr("localhost:9876");
        //hbos-dev
        consumer.setNamesrvAddr("192.168.54.201:9876");

        // Don't forget to set enablePropertyFilter=true in broker
        consumer.subscribe("SqlFilterTest",
                MessageSelector.bySql("a is not null and a between 5 and 6"));

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            System.out.printf("Receive New Messages: %s %n", new String(msgs.get(0).getBody()));
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
        System.out.printf("com.sample.rocketmq.apache.project.Consumer Started.%n");
//        consumer.subscribe("SqlFilterTest",
//                MessageSelector.bySql("a is not null and a between 0 and 3"));
    }
}
