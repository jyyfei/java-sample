package com.sample.rocketmq.apache.project;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class SqlFilterProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("GID_jyf_test_producer");
        //local
        producer.setNamesrvAddr("localhost:9876");
        //hbos-dev
//        producer.setNamesrvAddr("192.168.54.201:9876");
        producer.start();

        int a = 1;
        Message msg = new Message("SqlFilterTest",
                "test",
                ("Hello RocketMQ " + a).getBytes(RemotingHelper.DEFAULT_CHARSET)
        );
        msg.putUserProperty("a", String.valueOf(a));

        SendResult sendResult = producer.send(msg);
        System.out.printf("%s%n", sendResult);

        producer.shutdown();
    }
}
