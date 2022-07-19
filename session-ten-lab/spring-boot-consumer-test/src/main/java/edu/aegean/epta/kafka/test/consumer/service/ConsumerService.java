package edu.aegean.epta.kafka.test.consumer.service;

import edu.aegean.epta.kafka.test.consumer.model.Employee;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ConsumerService {

    @KafkaListener(groupId ="groups", topics = "kafka-test-topic")
    public void listerner(@Payload Employee data) {
        log.info("A message received with data: {}", data.toString() );
    }
}
