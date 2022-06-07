# Implementing a Kafka Producer in Java
Welcome to the session 3 lab. In this lab, you are going to create simple Java Kafka producer. You create a new replicated Kafka topic called **session-three-lab-two-topic**, then you create a Kafka producer that uses this topic to send records. You will send records with the Kafka producer. You will send records synchronously. Later, you will send records asynchronously.

## Learning Objectives

The learning objectives of this labs are: 
  * Setup a Producer Configuration. 
  * Implement a simple producer to send messages to Kafka cluster
  * Implement a producer to send messages synchronously to Kafka cluster. 
  * Implement a producer to send messages asynchronous to Kafka cluster

# Prerequisites

1. You need to start up the apache kafka single node cluster using the command

```
docker-compose -f single_zookeeper_multiple_kafka.yml up
```

2. Verify that the zookeeper and kafka containers are up and running

```sh
docker ps
```
3. Create a topic with name **session-three-lab-two-topic*** with 3 partition and `replication-factor` to 1, using the following command:

```sh
docker exec -it kafka1  kafka-topics --create --bootstrap-server 127.0.0.1:19092 --replication-factor 1 --partitions 3 --topic session-three-lab-two-topic
```

4. You need to startup a consumer that consume messages from the topic  **session-three-lab-two-topic**

```sh
docker exec -ti kafka1 kafka-console-consumer --bootstrap-server localhost:19092 --topic session-three-lab-two-topic --from-beginning
```

5. After the finish of the lab, you can stop the container using the command:

```sh
docker-compose -f single_zookeeper_multiple_kafka.yml down
```


## Set up a producer configuration 

To create a Kafka producer, you will need to pass it a list of bootstrap servers (a list of Kafka brokers). You will also specify a client.id that uniquely identifies this Producer client. In this example, we are going to send messages with ids. The message body is a string, so we need a record value serializer as we will send the message body in the Kafka's records value field. The message id (long), will be sent as the Kafka's records key. You will need to specify a Key serializer and a value serializer, which Kafka will use to encode the message id as a Kafka record key, and the message body as the Kafka record value.


To create a Kafka producer, you use `java.util.Properties` and define certain properties that we pass to the constructor of a KafkaProducer. 


You need to edit the method `createProducer` in the file FireAndForgetProducer.java

```java
    private static Producer<Long, String> createProducer() {

        //TODO Create props and set ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to BOOTSTRAP_SERVERS constant

        // This sets up the bootstrap Kafka brokers.
        // TODO Give the Kafka producer a client id.
        // HINT props.put(ProducerConfig.CLIENT_ID_CONFIG, "FireAndForgetProducer");
        // TODO setup ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
        // TODO setup ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
        // TODO return new KafkaProducer<>(props);
        return null;
    }
```

## Send messages 

Kafka provides a  send method to send a record to a topic with various strategies.Use this method to send some message ids and messages to the Kafka topic we created earlier.

You need to edit the method `runProducer()` in all files (FireAndForgetProducer.java, SynchronousProducer.java, AsynchronousProduer.java) and implement different strategies. 

