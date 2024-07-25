package com.sample.rocketmq.ons2apache.producer;

import cn.seenew.future.his.service.mq.core.common.MQMessage;
import cn.seenew.future.his.service.mq.core.common.MqCommonUtils;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.sample.rocketmq.ons2apache.MqConfig;

import java.util.Properties;
import java.util.UUID;

/**
 * 使用ons-client通过老的方式连接开源mq发送消息，使用onsAddr的方式
 */
public class OldOnsClientSubApacheProducerTest {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("AccessKey", MqConfig.ACCESS_KEY);
        properties.put("SecretKey", MqConfig.SECRET_KEY);
        properties.put(PropertyKeyConst.ONSAddr, "http://localhost:8080/rocketmq/nsaddr4client-internal");
        OrderProducer producer = ONSFactory.createOrderProducer(properties);

        String key = UUID.randomUUID().toString();
        Message message = MqCommonUtils.getMessage(MqConfig.TOPIC, MqConfig.TAG,
                "msgBody",
                key);
        MQMessage mqMessage = new MQMessage(message, key);
        SendResult sendResult = MqCommonUtils.sendMessage(producer, MqCommonUtils.MessageType.GlobalOrder, mqMessage);
        System.out.printf("%s%n", sendResult);
    }

}
