package gr.edu.aegean.epta.kafka.reliable.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


@Log4j2
@Service
public class ReliableConsumer {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapConsumerAddress;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Value("${spring.kafka.template.default-topic}")
    private String kafkaTopic;

    private final ObjectMapper objectMapper;


    public ReliableConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    /* Consumer A - default
     : AutoCommit - Disabled
     : Ack -> Batch :
     : Sync Commit : Enabled
     */
    @KafkaListener(
            topics = {"${spring.kafka.template.default-topic}"},
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumerA(
            @Payload String message,
            @Header(value = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) String offset,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) String timestamp) throws Exception {


        Thread.sleep(1_000);

        log.info("Consumer A :: Prossesing Message :\nBody: {},\nkey: {}\ntopic: {},\npartition: {},\noffset: {}," +
                "\ntime: {}", message, key, topic, partition, offset, timestamp);

        log.info("Consumer A :: Messages received -> .... End Processing ..... ");
    }


}
