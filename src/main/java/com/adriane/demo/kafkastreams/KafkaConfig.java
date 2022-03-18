package com.adriane.demo.kafkastreams;

import java.util.HashMap;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.DoubleDeserializer;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import static com.adriane.demo.kafkastreams.KafkaStreamsConfig.STREAM_GROUP_ID;
import static com.adriane.demo.kafkastreams.KafkaStreamsConfig.STREAM_INPUT_TOPIC;
import static com.adriane.demo.kafkastreams.KafkaStreamsConfig.STREAM_OUTPUT_TOPIC;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Double>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Double> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    @Bean
    public KafkaTemplate<String, Double> kafkaTemplate() {
        return new KafkaTemplate<String, Double>(producerFactory());
    }

    @Bean
    public NewTopic inputTopic() {
        return TopicBuilder.name(STREAM_INPUT_TOPIC)
                .partitions(3)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic outputTopic() {
        return TopicBuilder.name(STREAM_OUTPUT_TOPIC)
                .partitions(3)
                .replicas(1)
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
                .build();
    }

    private ProducerFactory<String, Double> producerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfig());
    }

    private ConsumerFactory<String, Double> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(kafkaConsumerConfig());
    }

    private HashMap<String, Object> kafkaProducerConfig() {
        HashMap<String, Object> mapConfig = new HashMap<>();
        mapConfig.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        mapConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, STREAM_GROUP_ID);
        mapConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        mapConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DoubleSerializer.class);
        return mapConfig;
    }

    private HashMap<String, Object> kafkaConsumerConfig() {
        HashMap<String, Object> mapConfig = new HashMap<>();
        mapConfig.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        mapConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        mapConfig.put(StreamsConfig.APPLICATION_ID_CONFIG, STREAM_GROUP_ID);
        mapConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        mapConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DoubleDeserializer.class);
        return mapConfig;
    }

}
