package edu.aegean.epta.kafka.producer.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class AdvancedKafkaMessage {

    @JsonProperty("mainQueueMessages")
    private int main;

    @JsonProperty("retryQueueMessages")
    private int retry;
}
