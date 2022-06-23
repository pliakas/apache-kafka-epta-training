package gr.edu.aegean.epta.kafka.reliable.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {


    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootStrapProducerAddress;

    @Value("${spring.kafka.template.default-topic}")
    private String kafkaTopicOnePartition;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();

        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapProducerAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic advancedTopic() {
        return TopicBuilder.name(kafkaTopicOnePartition)
                .replicas(3)
                .partitions(10)
                .build();
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapProducerAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 100);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16_384);

        return new DefaultKafkaProducerFactory<>(configProps);
    }


    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}


