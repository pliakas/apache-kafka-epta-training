package edu.aegean.epta.kafka.producer.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import edu.aegean.epta.kafka.producer.consumer.AdvancedKafkaConsumerService;
import edu.aegean.epta.kafka.producer.domain.AdvancedKafkaMessage;
import edu.aegean.epta.kafka.producer.domain.MessageResponse;
import edu.aegean.epta.kafka.producer.sender.AdvancedKafkaProducerService;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

import static java.util.Objects.requireNonNull;

@Log4j2
@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {

    private final AdvancedKafkaProducerService advancedKafkaProducerService;
    private final AdvancedKafkaConsumerService advancedKafkaConsumerService;

    public KafkaController(
            AdvancedKafkaProducerService advancedKafkaProducerService,
            AdvancedKafkaConsumerService advancedKafkaConsumerService) {
        this.advancedKafkaProducerService = advancedKafkaProducerService;
        this.advancedKafkaConsumerService = advancedKafkaConsumerService;
    }

    @PostMapping(value = "/advanced")
    public MessageResponse sendMessageToKafka(@RequestBody AdvancedKafkaMessage message)
            throws InterruptedException {
        requireNonNull(message, "Cannot be null.");
        try {
            // just initialize counters
            advancedKafkaConsumerService.initializeCounter();

            advancedKafkaProducerService.sendMessage(message);

        } catch (JsonProcessingException e) {
            log.error("Error Unable to convert message");
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return MessageResponse.builder()
                .mainQueueMessageReceived(advancedKafkaConsumerService.getMainCounter().intValue())
                .retryQueueMessageReceived(advancedKafkaConsumerService.getRetryCounter().intValue())
                .build();
    }
}
