package com.sample.rocketmq.apache2aliyun.consumer;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.RPCHook;

import java.util.List;

/**
 * 测试自动创建
 *
 * 启用autoCreateTopicEnable创建主题时，在Broker端创建主题的时机为，消息生产者往Broker端发送消息时才会创建。
 */
public class TestAutoCreate {
    private static RPCHook getAclRPCHook() {
        return new AclClientRPCHook(new SessionCredentials("", ""));
    }

    public static void main(String[] args) throws MQClientException {
        //GID-Test-Demo不存在
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("GID-Test-Demo",
                getAclRPCHook(), new AllocateMessageQueueAveragely(),
                true, null);
        consumer.setAccessChannel(AccessChannel.CLOUD);
        //Topic-Test-Demo不存在
        consumer.subscribe("Topic-Test-Demo", "*");
        consumer.setNamesrvAddr("10.100.124.163:9876");

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.registerMessageListener(new MessageListenerOrderly() {

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                context.setAutoCommit(true);
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();
        System.out.printf("Consumer Started.%n");
    }
}
