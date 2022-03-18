package com.adriane.demo.kafkastreams;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;

@Slf4j
@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    public static final String STREAM_GROUP_ID = "demo.kafkastreams";

    public static final String STREAM_INPUT_TOPIC = "streams.input";
    public static final String STREAM_OUTPUT_TOPIC = "streams.output";

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, STREAM_GROUP_ID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Double().getClass().getName());
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public StreamsBuilderFactoryBeanConfigurer configurer() {
        return fb -> fb.setStateListener((newState, oldState) -> {
            System.out.println("State transition from " + oldState + " to " + newState);
        });
    }

    @Bean
    public KStream<String, Double> sourceProcessor(StreamsBuilder streamsBuilder) {
        // StreamBuilder: Use the builders to define the actual processing topology, e.g. to specify
        // from which input topics to read, which stream operations (filter, map, etc.)
        // should be called, and so on.
        KStream<String, Double> stream = streamsBuilder.stream(STREAM_INPUT_TOPIC);

        KStream<String, Double> resultStream = stream.map((key, value) -> new KeyValue<>(randomUUID().toString(), value))
                .selectKey((actualKey, value) -> format("%s.%s", "stream.demo", actualKey));
        resultStream.to(STREAM_OUTPUT_TOPIC);

        resultStream.print(Printed.toSysOut());

        return resultStream;
    }

}
