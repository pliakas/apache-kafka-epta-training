# Implementing a Kafka Producer in Java
Welcome to the session 3 lab. In this lab, you are going to create simple Java Kafka consumer using Java Clients. You 
create a new replicated Kafka topic called **session-four-lab-topic-javac**, then you create a Kafka consumer that uses 
this topic to consume records. 

## Learning Objectives


# Prerequisites

1. You need to start up the apache kafka single node cluster using the command

```
docker-compose -f single_zookeeper_multiple_kafka.yml up
```

2. Verify that the zookeeper and kafka containers are up and running

```sh
docker ps
```
3. Create a topic with name **session-four-lab-topic-javac** with 3 partition and `replication-factor` to 3, using 
   the following command:

```sh
docker exec -it kafka1  kafka-topics --create --bootstrap-server 127.0.0.1:19092 --replication-factor 3 --partitions 
3 --topic session-four-lab-topic-java
```

4. You need to startup a consumer that consume messages from the topic  **session-four-lab-topic-javac**

```sh
docker exec -ti kafka1 kafka-console-producer --bootstrap-server localhost:19092 --topic session-four-lab-topic-java
```

5. After the finish of the lab, you can stop the container using the command:

```sh
docker-compose -f single_zookeeper_multiple_kafka.yml down
```


## Implement a SimpleKafkaConsumer

To create a Kafka consumer, you will need to pass it a list of bootstrap servers (a list of Kafka brokers). You will 
also specify a client.id that uniquely identifies this consumer client. In this example, we are going to consume 
messages 
with ids. The message body is a string, so we need a record value serializer as we will send the message body in the Kafka's records value field. The message id (long), will be sent as the Kafka's records key. You will need to specify a Key serializer and a value serializer, which Kafka will use to encode the message id as a Kafka record key, and the message body as the Kafka record value.


To create a Kafka producer, you use `java.util.Properties` and define certain properties that we pass to the constructor of a KafkaProducer. 


You need to edit the method `createConsumer()` in the file SimpleKafkaConsumer.java

```java
// TODO: Edit this method to create a consumer
private static KafkaConsumer<Long, String> createConsumer() {

        //TODO : Create a Properties object and put the needed configuration
        Properties props = new Properties();

        //TODO:  Create the consumer using props.
        final KafkaConsumer<Long, String> consumer = null;

        // TODO: Subscribe to the topic.


        return consumer;
}
```

## Implement AdvancedKafkaConsumer
A consumer is a type of Kafka client that consumes records from Kafka cluster. The Kafka Consumer automatically handles Kafka broker failure,
adapt as topic partitions leadership moves in Kafka cluster. The consumer works with Kafka broker to form consumer groups and load balance consumers. 
The consumer maintains connections to Kafka brokers in the cluster. The consumer must be closed to not leak resources. 
The Kafka client API for Consumers are NOT thread-safe.

## Stock Price Consumer
The Stock Price Consumer example has the following classes:

  * StockPrice - holds a stock price has a name, euros, and cents
  * AdvancedConsumer - consumes StockPrices and display batch lengths for the poll method call
  * StockPriceDeserializer - can deserialize a StockPrice from byte[]

### Action 1: Implement a deserializer
Edit class ` edu.aegean.epta.kafka.consumer.advanced.consumer.StockDeserializer.java` to implement a custom 
deserializer and enable it the consumer

### Action 2: Implement Consumer
Implement Consumer to receive the messages sent by producer

 * Edit class `edu.aegean.epta.kafka.consumer.advanced.consumer.AdvancedConsumer.java`
 * You can run producer by run the class `edu.aegean.epta.kafka.consumer.advanced.producer.StockPriceProducer.java`. 
   You should see the size of each partition read, the total record count and each stock at its current price.
 * Verify that you are receiving all the messages that are produced. 


