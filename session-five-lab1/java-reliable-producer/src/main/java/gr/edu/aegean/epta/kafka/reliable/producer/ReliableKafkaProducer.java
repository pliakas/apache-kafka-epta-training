package gr.edu.aegean.epta.kafka.reliable.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.edu.aegean.epta.kafka.reliable.model.KafkaMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReliableKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    @Value("${spring.kafka.template.default-topic}")
    private String kafkaTopic;

    public ReliableKafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(final int numberOfMessages) {

        AtomicInteger messageCounter = new AtomicInteger(0);

        Supplier<KafkaMessage> supplier =
                () ->
                        KafkaMessage.builder()
                                .uuid(UUID.randomUUID().toString())
                                .message("Message value: " + messageCounter.incrementAndGet())
                                .build();

        AtomicInteger keyCounter = new AtomicInteger(0);

        var messages = Stream.generate(supplier).limit(numberOfMessages).collect(Collectors.toList());

        messages.forEach(
                value -> {
                    try {
                        kafkaTemplate.send(
                                kafkaTopic,
                                "key-" + keyCounter.incrementAndGet(),
                                objectMapper.writeValueAsString(value));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
    }
}
