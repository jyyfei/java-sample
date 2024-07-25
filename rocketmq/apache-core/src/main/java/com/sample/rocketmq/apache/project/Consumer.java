package com.sample.rocketmq.apache.project;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.impl.MQClientAPIImpl;
import org.apache.rocketmq.common.protocol.body.ConsumerConnection;
import org.apache.rocketmq.common.protocol.body.GroupList;
import org.apache.rocketmq.common.protocol.route.BrokerData;
import org.apache.rocketmq.common.protocol.route.TopicRouteData;

public class Consumer {

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("GID_jyf_test_gray");
        //local
        consumer.setNamesrvAddr("localhost:9876");
        //hbos-dev
//        consumer.setNamesrvAddr("192.168.54.201:9876");

        // Don't forget to set enablePropertyFilter=true in broker
//        consumer.subscribe("SqlFilterTest", MessageSelector.bySql("a is not null and a between 4 and 6"));
        consumer.subscribe("test-topic", "*");

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            System.out.printf("Receive New Messages: %s userProperties: %s %n",
                    new String(msgs.get(0).getBody()),
                    msgs.get(0).getUserProperty("a"));
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
        System.out.printf("com.sample.rocketmq.apache.project.Consumer Started.%n");

//        ScheduledFuture<?> future = new ScheduledThreadPoolExecutor(1).scheduleAtFixedRate((Runnable) () -> {
//            try {
//                queryTopicConsumeByWho("SqlFilterTest", consumer);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }, 2, 2, TimeUnit.SECONDS);

    }

    public static GroupList queryTopicConsumeByWho(String topic, DefaultMQPushConsumer consumer) throws Exception {
        MQClientAPIImpl mqClientAPIImpl = consumer.getDefaultMQPushConsumerImpl().getmQClientFactory().getMQClientAPIImpl();

        TopicRouteData topicRouteData = mqClientAPIImpl.getTopicRouteInfoFromNameServer(topic, 1000);

        for (BrokerData bd : topicRouteData.getBrokerDatas()) {
            String addr = bd.selectBrokerAddr();
            if (addr != null) {
                GroupList groupList = mqClientAPIImpl.queryTopicConsumeByWho(addr, topic, 1000);
                if (!groupList.getGroupList().isEmpty()) {
                    groupList.getGroupList().forEach(s -> {
                        try {
                            try {
                                ConsumerConnection consumerConnectionList = mqClientAPIImpl.getConsumerConnectionList(addr, s, 1000);
                                if (!consumerConnectionList.getConnectionSet().isEmpty()) {
                                    System.out.println(s + "在线");
//                                    List<String> consumerIdListByGroup = mqClientAPIImpl.getConsumerIdListByGroup(addr, s, 1000);
//                                    System.out.println(consumerIdListByGroup);
//                                    ConsumerRunningInfo consumerRunningInfo = mqClientAPIImpl.getConsumerRunningInfo(addr, s, consumerIdListByGroup.get(0), false, 1000);
//                                    System.out.println(JSON.toJSONString(consumerRunningInfo));
                                }
                            } catch (Exception e) {
                                System.out.println(s + "离线");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            break;
        }

        return null;
    }
}
