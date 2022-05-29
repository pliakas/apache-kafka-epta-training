# Introduction

In this lesson, we will have the opportunity to work with a single node kafka cluster using the command line tools provided through Kafka. Ultimately, Kafka makes it easy to build applications that interact with the Kafka cluster, but it also provides some command line tools. We can use these to create ad-hoc producers and consumers, plus perform a variety of administrative tasks that require you to interact with the cluster.

## Learning Objectives
Successfully complete this lab by achieving the following learning objectives. 


## Description Create a topic that meets the following specifications:

The topic name should be **fruits_inventory**.
Number of partitions: 1
Replication factor: 1
Publish some test data to the topic. Since this is just a test, the data can be anything you want, but here is an example:

product: apples, quantity: 5
product: lemons, quantity: 7

Set up a consumer from the command line and verify that you see the test data you published to the topic.

This cluster is single node kafka cluster, so you can access the Kafka command line utilities directly from the path, i.e. kafka-topics.

## Prerequisites

1. You need to start up the apache kafka single node cluster using the command

```
docker-compose -f single_zookeeper_single_kafka.yml up
```

2. Verify that the zookeeper and kafka containers are up and running

```sh
docker ps
```

3. After the finish of the lab, you can stop the container using the command:

```sh
docker-compose -f single_zookeeper_single_kafka.yml down
```

### Create a Kafka Topic for the Fruit Basket Inventory

```sh
docker exec -ti kafka1 kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic fruits_inventory
```

### Test the Setup by Publishing and Consuming Some Data

1. Verify that the topic **fruits_inventory** created.

```sh
docker exec -ti kafka1 kafka-topics --list --bootstrap-server localhost:9092

```

2. Start a command line for the *producer*. 
```sh
docker exec -ti kafka1 kafka-console-producer --broker-list localhost:9092 --topic fruits_inventory
```

*Hint*: Open a new terminal and execute the above command. -

4. Type in a few lines of test data. Since we are working with merely test data, a specific format is not required. It could look like this:

```sh
product: apples, quantity: 3
product: lemons, quantity: 2
```

*Note*: Once the test messages are published, we can exit the producer.


5. Start up a command line *consumer* to consume data from *fruits_inventory* topic.

```sh
docker exec -ti kafka1 kafka-console-consumer --bootstrap-server localhost:9092 --topic fruits_inventory --from-beginning
```

*Hint*: Open a new terminal and execute the above command. 

6. Verify that the test messages were published earlier. 

```sh
product: apples, quantity: 3
product: lemons, quantity: 2
```

7. Repeat the steps 2-3 many times with different data and values. 

### Use Kcat tool instead (Optional)
Kcat (formerly kafkacat) is a command-line utility that you can use to test and debug Apache Kafka deployments. You can use kcat to produce, consume, and list topic and partition information for Kafka. Described as “netcat for Kafka”, it is a swiss-army knife of tools for inspecting and creating data in Kafka.

It is similar to Kafka Console Producer (kafka-console-producer) and Kafka Console Consumer (kafka-console-consumer), used earlier but even more powerful.


1. Verify that the early created topic *fruits_inventory* existing

```sh
docker run -it --rm --network session-two-lab_default confluentinc/cp-kafkacat kafkacat -b kafka1:9092 -L
```

2. Sending data to *fruits_inventory* topic using kcat tool

```sh
docker run -it --rm --network host --name producer confluentinc/cp-kafkacat  kafkacat -b localhost:9092 -P -t fruits_inventory
```

*Hint*: Try to send some sample data

3. Consuming data from *fruits_inventor*

```sh
docker run -it --rm --network host --name consumer confluentinc/cp-kafkacat  kafkacat -b localhost:9092 -C -t fruits_inventory

```
