package com.adriane.demo.kafkastreams;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Controller {

    final KafkaTemplate<String, Double> kafkaTemplate;

    @GetMapping("/foo")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void produceSaPoura() {
        Random random = new Random();
        String messageKey = String.format("%d", random.nextLong());
        Double messageValue = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0, 9))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        log.info("Produce key {}, value {}", messageKey, messageValue);
        kafkaTemplate.send(KafkaStreamsConfig.STREAM_INPUT_TOPIC, messageKey, messageValue);
    }

}
