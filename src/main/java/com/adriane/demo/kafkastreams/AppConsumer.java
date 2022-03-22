package com.adriane.demo.kafkastreams;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppConsumer {

    @KafkaListener(topics = {KafkaStreamsConfig.STREAM_OUTPUT_TOPIC}, groupId = "demo.kafkastreams.out")
    public void consumeSaPoura(ConsumerRecord<String, Double> consumerRecord) {
      log.info("Message consumed, key {}, value{}", consumerRecord.key(), consumerRecord.value());
    }

}
