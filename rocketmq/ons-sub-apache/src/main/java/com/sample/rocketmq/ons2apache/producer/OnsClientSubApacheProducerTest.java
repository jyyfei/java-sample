package com.sample.rocketmq.ons2apache.producer;

import cn.seenew.future.his.service.mq.core.common.MQMessage;
import cn.seenew.future.his.service.mq.core.common.MqCommonUtils;
import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.sample.rocketmq.ons2apache.MqConfig;

import java.util.Properties;
import java.util.UUID;

/**
 * 使用ons-client连接开源mq发送消息
 */
public class OnsClientSubApacheProducerTest {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("AccessKey", MqConfig.ACCESS_KEY);
        properties.put("SecretKey", MqConfig.SECRET_KEY);
//        properties.put(PropertyKeyConst.ONSAddr, onsAddr);
        properties.put(PropertyKeyConst.NAMESRV_ADDR, MqConfig.NAMESRV_ADDR);
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
