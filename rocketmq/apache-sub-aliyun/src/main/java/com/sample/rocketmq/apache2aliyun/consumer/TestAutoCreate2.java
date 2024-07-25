package com.sample.rocketmq.apache2aliyun.consumer;

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

public class TestAutoCreate2 {

    private static RPCHook getAclRPCHook() {
        return new AclClientRPCHook(new SessionCredentials("", ""));
    }

    public static void main(String[] args) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("GID-Test-Demo-2",
                getAclRPCHook(),
                true,
                null);
        producer.setAccessChannel(AccessChannel.CLOUD);
        producer.setNamesrvAddr("10.100.124.163:9876");
        producer.setSendMsgTimeout(7000);
        producer.start();
        try {

            Message msg = new Message("Topic-Test-Demo",
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
