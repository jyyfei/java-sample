package com.sample.rocketmq.ons2apache;

/**
 * MQ 配置
 */
public class MqConfig {
    /**
     * 启动测试之前请替换如下 XXX 为您的配置
     */
    public static final String TOPIC = "Topic-WechatApplet-Inspect";
    public static final String GROUP_ID = "GID-Miniapp-Local";
    public static final String ACCESS_KEY = "xx";
    public static final String SECRET_KEY = "xx";
    public static final String TAG = "mq_test_tag";

    public static final String NAMESRV_ADDR = "localhost:9876";

    public static final String ONS_ADDR = "http://localhost:9010/rocketmq/nsaddr4client-internal";

}
