# Assignment: Implement a CustomPartitioner

We have an application that send messages with two different keys
* **main-key**
* **retry-key**

to a  topic with name: **kafka-advanced-topic**. This topic has  with 10 partitions.  All messages are being seny
to partition 0 of this topic.

# Objective One
You need to implement a custom partition strategy, such that:
* Messages with key **main-key** will be sent only to first seven partitions (e.g 0, 1, 2, 3, 4, 5, 6)
* Messages with key **retry-key** wil bne sent only to last three parttions (e.g. 7, 8, 9)

**Verify from the log and the response that the messages were sent from the correct partitioned**


# Objective Two
Increase the number of partition for the topic **kafka-advanced-topic** to 15. Rerun the test and observe the results.


# Prerequisites 
# Prerequisites

1. You need to start up the apache kafka single node cluster using the command

```
docker-compose -f single_zookeeper_multiple_kafka.yml up
```

2. Verify that the zookeeper and kafka containers are up and running

```sh
docker ps
```
3. You can use the following curl command to initiate messages to be generated and sent. 

```shell
curl -X POST --location "http://localhost:8080/kafka/advanced" \
    -H "accept: */*" \
    -H "Content-Type: application/json" \
    -d "{ \"mainQueueMessages\": 10, \"retryQueueMessages\": 10}"
```

4. You can start the application (SpringBoot Application) either from your editor or from the following command: 

```shell
mvn spring-boot:run
```

6. After the finish of the lab, you can stop the container using the command:

```sh
docker-compose -f single_zookeeper_multiple_kafka.yml down
```


# Hints
You can find below some hints related to implementation 

* You need only to edit ` edu.aegean.epta.kafka.producer.partitioner.CustomPartitioner.java` 
* 