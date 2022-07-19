package edu.aegean.epta.kafka.producer.test;

import edu.aegean.epta.kafka.producer.test.controller.EmployeeController;
import edu.aegean.epta.kafka.producer.test.model.Employee;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
class SpringBootProducerTestApplicationTests {

  static KafkaContainer kafka;

  static {
    kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));
    kafka.start();
  }

  @Autowired
  EmployeeController employeeController;

  @Autowired
  private KafkaAdmin admin;


  @DynamicPropertySource
  public static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers",kafka::getBootstrapServers);


  }

  @Test
  public void testCreationOfTopicAtStartup() throws IOException, InterruptedException, ExecutionException {
    AdminClient client = AdminClient.create(admin.getConfigurationProperties());
    Collection<TopicListing> topicList = client.listTopics().listings().get();

    assertNotNull(topicList);
    assertEquals(topicList.stream().map(TopicListing::name).collect(Collectors.toList()),
            Arrays.asList("producer-test-topic","springboot-topic"));
  }

  @Test
  public void testPublishEmployee() throws IOException, InterruptedException, ExecutionException {


    // arrange
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
    props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    props.put(JsonSerializer.TYPE_MAPPINGS, "Employee:edu.aegean.epta.kafka.producer.test.model.Employee");
    KafkaConsumer<Integer, Employee> consumer = new KafkaConsumer(props);


    consumer.subscribe(Collections.singletonList("producer-test-topic"));

    Employee emp = new Employee();
    emp.setId(1);
    emp.setName("Test");
    employeeController.publishEmployee(emp);



    await().atMost(20, TimeUnit.SECONDS).until(() -> {
      ConsumerRecords<Integer, Employee> records = consumer.poll(Duration.ofMillis(100));

      if (records.isEmpty()) {
        return false;
      }

      records.forEach( r -> System.out.println(r.topic() + " *** "+ r.key() + " *** "+ r.value()));

      Assertions.assertThat(records.count()).isEqualTo(1);
      Assertions.assertThat(records.iterator().next().value().getName()).isEqualTo("Test");
      Assertions.assertThat(records.iterator().next().value().getId()).isEqualTo(1);
      return true;
    });

  }
}
