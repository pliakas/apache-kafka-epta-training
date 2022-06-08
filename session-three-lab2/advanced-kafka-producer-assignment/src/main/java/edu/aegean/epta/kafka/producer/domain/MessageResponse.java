package edu.aegean.epta.kafka.producer.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {

    @JsonProperty("main_queue_message_received")
    private int mainQueueMessageReceived;

    @JsonProperty("retry_queue_message_received")
    private int retryQueueMessageReceived;
}
