package com.sample.rocketmq.apache.project;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class Producer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("GID_jyf_test_producer");
        //local
//        producer.setNamesrvAddr("localhost:9876");
        //hbos-dev
        producer.setNamesrvAddr("192.168.54.201:9876");
        producer.start();

        int a = 5;
        Message msg = new Message("SqlFilterTest",
                "test",
                ("Hello RocketMQ " + a).getBytes(RemotingHelper.DEFAULT_CHARSET)
        );
        msg.putUserProperty("a", String.valueOf(a));
        msg.putUserProperty("PROJECT_ENV", "test_env");

        SendResult sendResult = producer.send(msg);
        System.out.printf("%s%n", sendResult);

        producer.shutdown();
    }
}
