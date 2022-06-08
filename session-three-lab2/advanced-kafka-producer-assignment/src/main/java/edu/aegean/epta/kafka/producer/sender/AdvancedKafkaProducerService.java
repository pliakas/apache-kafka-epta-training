package edu.aegean.epta.kafka.producer.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.aegean.epta.kafka.producer.domain.AdvancedKafkaMessage;
import edu.aegean.epta.kafka.producer.domain.KafkaMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class AdvancedKafkaProducerService {

    @Value("${spring.kafka.template.default-topic}")
    private String defaultTopic;

    private static final int NUMBER_OF_MESSAGES = 10;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AdvancedKafkaProducerService(
            KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(AdvancedKafkaMessage message) throws JsonProcessingException, ExecutionException, InterruptedException {
        final String mainKey = "main-key";
        final String retryKey = "retry-key";

        log.info(
                String.format(
                        "$$$$ -> Producing %s messages for Main Queue", message.getMain()));

        for (int index = 0; index < message.getMain(); index++) {

            KafkaMessage record =
                    KafkaMessage.builder().greeting("Main Messages").name("message-main-" + index).build();

            kafkaTemplate.send(defaultTopic, mainKey,
                    objectMapper.writeValueAsString(record)).get();
        }

        log.info(
                String.format(
                        "$$$$ -> Producing %s Message for Retry Queue ->", message.getRetry()));

        for (int index = 0; index < message.getRetry(); index++) {
            KafkaMessage record =
                    KafkaMessage.builder().greeting("Retry Messages").name("message-retry-" + index).build();
            kafkaTemplate.send(defaultTopic, "retry-key-" + index, objectMapper.writeValueAsString(record));

            Thread.sleep(1_000);
        }
    }
}
