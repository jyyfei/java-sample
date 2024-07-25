package com.sample.rocketmq.apache.project;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.hook.FilterMessageContext;
import org.apache.rocketmq.client.hook.FilterMessageHook;
import org.apache.rocketmq.common.message.MessageExt;

public class SqlFilterConsumer2 {

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("GID_jyf_test_gray");
        //local
        consumer.setNamesrvAddr("localhost:9876");
        //hbos-dev
//        consumer.setNamesrvAddr("192.168.54.201:9876");

        // Don't forget to set enablePropertyFilter=true in broker
        consumer.subscribe("SqlFilterTest", "*");

        consumer.getDefaultMQPushConsumerImpl().registerFilterMessageHook(new FilterMessageHook() {
            @Override
            public String hookName() {
                return "testHook";
            }

            @Override
            public void filterMessage(FilterMessageContext context) {
                MessageExt messageExt = context.getMsgList().get(0);
                if (messageExt.getUserProperty("a").equals("5")) {
                    context.getMsgList().clear();
                }
            }
        });

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            System.out.printf("Receive New Messages: %s userProperties: %s %n",
                    new String(msgs.get(0).getBody()),
                    msgs.get(0).getUserProperty("a"));
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
        System.out.printf("com.sample.rocketmq.apache.project.Consumer Started.%n");
    }
}
