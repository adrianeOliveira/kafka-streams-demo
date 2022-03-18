package com.adriane.demo.kafkastreams;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import static com.adriane.demo.kafkastreams.KafkaStreamsConfig.STREAM_GROUP_ID;

@Slf4j
public class AppConsumer {

    @KafkaListener(topics = {KafkaStreamsConfig.STREAM_OUTPUT_TOPIC}, groupId = STREAM_GROUP_ID)
    public void consumeSaPoura(ConsumerRecord<String, Double> consumerRecord) {
      log.info("Message consumed, key {}, value{}", consumerRecord.key(), consumerRecord.value());
    }

}
