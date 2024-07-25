package com.sample.rocketmq.apache2aliyun.producer;

import com.sample.rocketmq.apache2aliyun.MqConfig;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class RocketMQProducer {

    private static RPCHook getAclRPCHook() {
        return new AclClientRPCHook(new SessionCredentials(MqConfig.ACCESS_KEY, MqConfig.SECRET_KEY));
    }

    public static void main(String[] args) throws MQClientException {
        /**
         * 创建Producer，并开启消息轨迹
         * 如果不想开启消息轨迹，可以按照如下方式创建：
         * DefaultMQProducer producer = new DefaultMQProducer(MqConfig.GROUP_ID, getAclRPCHook());
         */
        DefaultMQProducer producer = new DefaultMQProducer(MqConfig.GROUP_ID,
                getAclRPCHook(),
                true,
                null);
        /**
         * 设置使用接入方式为阿里云，在使用云上消息轨迹的时候，需要设置此项，如果不开启消息轨迹功能，则运行不设置此项.
         */
        producer.setAccessChannel(AccessChannel.CLOUD);
        producer.setNamesrvAddr("192.168.54.112:9876");
        producer.setSendMsgTimeout(7000);
        producer.start();
        try {

            Message msg = new Message(MqConfig.TOPIC,
                    MqConfig.TAG,
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 128; i++) {

        }
        producer.shutdown();
    }
}
