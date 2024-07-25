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

public class RocketMQProducerMyAliYun {

    private static RPCHook getAclRPCHook() {
        return new AclClientRPCHook(new SessionCredentials("xx", "xx"));
    }

    public static void main(String[] args) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("GID-test-producer",
                getAclRPCHook(),
                true,
                null);
        producer.setAccessChannel(AccessChannel.CLOUD);
        producer.setNamesrvAddr("http://xx:8080");
        producer.setSendMsgTimeout(100000);
        producer.start();
        try {
            Message msg = new Message("Topic-test-2",
                    MqConfig.TAG,
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.shutdown();
    }
}
