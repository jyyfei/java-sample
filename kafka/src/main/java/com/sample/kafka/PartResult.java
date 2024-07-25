package com.sample.kafka;

/**
 *
 * bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list --new-consumer
 *
 * bin/kafka-simple-consumer-shell.sh --topic __consumer_offsets --partition 47 --broker-list localhost:9092 --formatter "kafka.coordinator.group.GroupMetadataManager\$OffsetsMessageFormatter"
 *
 *
 * @author JiYunFei
 * @date 2022/2/14
 */
public class PartResult {
    public static void main(String[] args) {
        int i = Math.abs("console-consumer-33523".hashCode()) % 50;
        System.out.println(i);
    }
}

/*

47

*
* */