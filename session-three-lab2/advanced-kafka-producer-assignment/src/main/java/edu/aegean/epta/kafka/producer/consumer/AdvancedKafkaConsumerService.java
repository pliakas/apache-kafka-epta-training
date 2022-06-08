package edu.aegean.epta.kafka.producer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Service
//TODO: Do not edit
public class AdvancedKafkaConsumerService {

    private final ObjectMapper objectMapper;

    private AtomicInteger mainCounter;
    private AtomicInteger retryCounter;

    public AdvancedKafkaConsumerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topicPartitions =
            @TopicPartition(
                    topic = "kafka-advanced-topic",
                    partitions = {"0", "1", "2", "3", "4", "5", "6"}))
    public void listenToMainParitions(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("Main Received Message: {} from partition: {}", message, partition);

        mainCounter.incrementAndGet();
    }

    @KafkaListener(
            topicPartitions =
            @TopicPartition(
                    topic = "kafka-advanced-topic",
                    partitions = {"7", "8", "9"}))
    public void listenToRetryParitions(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("Retry Received Message: {} from partition: {}", message, partition);

        retryCounter.incrementAndGet();
    }

    public void initializeCounter() {
        mainCounter = new AtomicInteger(0);
        retryCounter = new AtomicInteger(0);
    }

    public AtomicInteger getMainCounter() {
        return mainCounter;
    }

    public AtomicInteger getRetryCounter() {
        return retryCounter;
    }
}
