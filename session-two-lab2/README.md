# Introduction

In this lab, we will have the opportunity to work with a multinode node kafka cluster using the command line tools provided through Kafka to apply **topics**. 

Kafka Topics CLI, , ***kafka-topics*** is used to create, delete, describe, or change a topic in Kafka. 


**Hint**: Use CLI commands with appropriate extensions for other platforms, e.g., kafka-topics.bat for windows, kafka-topics.sh for Linux and Mac


## Learning Objectives
Successfully complete this lab by achieving the following learning objectives. 

- **Create** a Kafka topic.
- **List** Kafka topics.
- **Describe** a Kafka topic.
- **Increase the number of partitions** of a Kafka topic. 
- **Delete** a Kafka topic
- **Override** Kafka topic configuration

## Prerequisites

1. You need to start up the apache kafka single node cluster using the command

```
docker-compose -f single_zookeeper_multiple_kafka.yml up
```

2. Verify that the zookeeper and kafka containers are up and running

```sh
docker ps
```

3. After the finish of the lab, you can stop the container using the command:

```sh
docker-compose -f single_zookeeper_multiple_kafka.yml down
```

## Create a Kafka Topic

To create a Kafka topic, we need to provide the mandatory parameters:
  * If older version of Kafka, use the Zookeeper URL and port e.g. localhost:2181 (*Not applicable for our lab*)
  * Provide the mandatory parameters: topic name, number of partitions and replication factor.
  * Use the `kafka-topics.sh` CLI with the `--create` option



**Note***: For recent version of Kafka, we strongly recommend you to use the command with the --bootstrap-server option because the Zookeeper option is now deprecated and is removed as part of Kafka v3.

### Example

Creating the Kafka topic **session_two_first_topic** with *3* partitions and a replication factor of *1* assuming that the Kafka broker is running at `localhost:9092` or inside the docker for port `localhost:19092`. For our cases we are using `localhost:19902` since we are executing the command from a **kafka1** docker instance


```sh
docker exec -it kafka1  kafka-topics --create --bootstrap-server 127.0.0.1:19092 --replication-factor 1 --partitions 3 --topic session_two_first_topic
```


The output command will be: 

```sh
Created topic session_two_first_topic
```

### Notes and tricks
Here are the common mistakes with the `kafka-topics.sh` --create command:


  * You cannot specify a replication factor greater than the number of brokers you have.
  * You can specify as many partitions as you want, 3 is a good number to get started with in dev.
  * There are no default for partitions and replication factor and you must specify those explicitly.
  * The topic name must contain only ASCII alphanumerics, '.', '_' and '-'.
  
### Important extra options

Some important option that you may consider: 
  * `--config` : You can set topic-level configurations (e.g. `--config max.message.bytes=6400`)
  
## List Kafka Topics

To list Kafka topics, we need to provide the mandatory parameters:

  * If Kafka v2.2+, use the Kafka hostname and port e.g., localhost:9092
  * If older version of Kafka, use the Zookeeper URL and port e.g. localhost:2181
  * Use the kafka-topics.sh CLI with the --list option
  
### Example
Listing topics when my Kafka broker is running at `localhost:19092`

```sh
docker exec -it kafka1  kafka-topics --list --bootstrap-server 127.0.0.1:19092 
```

and the output will be: 

```sh
session_two_first_topic
```

### Notes and tricks
Here are the common mistakes with the kafka-topics.sh --list command:

  * This command lists internal topics that you won't have created (such as __consumer_offsets). **Do not try to delete these topics**. 
  * Use `--exclude-internal` if you want to hide these topics

### Additional parameters
Extra Important options you can set (advanced)


  * `--exclude-internal`: Exclude internal topics when running list or describe command (they're listed by default)
  
To filter topics based on replication status:


  * `--at-min-isr-partitions` : If set when describing topics, only show partitions whose isr count is equal to the configured minimum.
  * `--unavailable-partitions` : Only show partitions whose **leader** is not available.
  * `--under-min-isr-partitions` : Only show partitions whose **isr** count is less than the configured minimum.
  * `--under-replicated-partitions` : Only show **under replicated** partitions


## Describe a Kafka topic

To describe a Kafka topic and get partition details, we need to provide the mandatory parameters

  * If Kafka v2.2+, use the Kafka hostname and port e.g., localhost:9092
  * Use the `kafka-topics.sh` CLI with the `--describe` option
  
# Example
Listing topics when my Kafka broker is running at localhost:9092



```sh
docker exec -it kafka1  kafka-topics --describe --bootstrap-server 127.0.0.1:19092 --topic session_two_first_topic
```

and the output will be: 

```sh
Topic: session_two_first_topic	TopicId: 7ElAqrjmRMKT7NUBSaYMMg	PartitionCount: 3	ReplicationFactor: 1	Configs: 
	Topic: session_two_first_topic	Partition: 0	Leader: 3	Replicas: 3	Isr: 3
	Topic: session_two_first_topic	Partition: 1	Leader: 1	Replicas: 1	Isr: 1
	Topic: session_two_first_topic	Partition: 2	Leader: 2	Replicas: 2	Isr: 2

```

  * **Leader** : 3 means that for partition 0, the broker with the ID 3 is the leader.
  * **Replicas*** : 3 means that for partition 0, the broker with the ID 3 is a replica.
  * **Isr**: 3 means that for partition 0, the broker with the ID 1 is an in-sync replica.
  
Let's try to create a topic with many partition and replication factor 3

```sh
docker exec -it kafka1  kafka-topics --create --bootstrap-server 127.0.0.1:19092 --replication-factor 3 --partitions 4 --topic session_two_third_topic

```

Now try to describe the topic 

```sh
docker exec -it kafka1  kafka-topics --describe --bootstrap-server 127.0.0.1:19092 --topic session_two_third_topic
```

and the output will be: 

```sh
Topic: session_two_third_topic	TopicId: D8YviI0wSVS0xK1zHorDkg	PartitionCount: 4	ReplicationFactor: 3	Configs: 
	Topic: session_two_third_topic	Partition: 0	Leader: 1	Replicas: 1,2,3	Isr: 1,2,3
	Topic: session_two_third_topic	Partition: 1	Leader: 2	Replicas: 2,3,1	Isr: 2,3,1
	Topic: session_two_third_topic	Partition: 2	Leader: 3	Replicas: 3,1,2	Isr: 3,1,2
	Topic: session_two_third_topic	Partition: 3	Leader: 1	Replicas: 1,3,2	Isr: 1,3,2
```

Explanation: 

  * Leader: 1 means that for partition 0, the broker with the ID12 is the leader.
  * Replicas: 1,2,3 means that for partition 0, the brokers with the ID 1, 2 and 3 are replicas.
  * Isr: 1,2,3 means that for partition 0, the brokers with the ID 1, 2 and 3 are replicas in-sync replica.
  
### Hints
Here are the some hints when using the kafka-topics.sh --describe command:


  * You can specify a comma delimited list to describe a list of topics
  * If you don't specify a --topic option, it will describe all topics

### Additional Parameters

To filter partitions based on replication status:


  * `--at-min-isr-partitions` : If set when describing topics, only show partitions whose ISR count is equal to the configured minimum.
  * `--unavailable-partitions` : Only show partitions whose leader is not available
  * `--under-min-isr-partitions` : Only show partitions whose ISR count is less than the configured minimum.
  * `--under-replicated-partitions** : Only show under replicated partitions

## Increase the number of partitions of a Kafka topic
To increase the number of partitions in a Kafka topic, we need to provide the mandatory parameters:


  * Provide the mandatory parameters: topic name and number of partitions
  * Use the kafka-topics.sh CLI with the --alter option

**HINT** :  Increasing the number of partitions in a Kafka topic a **DANGEROUS OPERATION** if your applications are relying on key-based ordering. In that case, create a new topic and copy all data there instead to have keys properly re-distributed.


# Example

Increasing the Kafka topic first_topic to have 5 partitions when my Kafka broker is running at `localhost:9092`


```sh
docker exec -it kafka1 kafka-topics --bootstrap-server localhost:19092 --alter --topic session_two_first_topic --partitions 5
```

Now describe the topic to see the result:

```sh
docker exec -it kafka1  kafka-topics --describe --bootstrap-server 127.0.0.1:19092 --topic session_two_first_topic

```

and the output will be: 

```sh
Topic: session_two_first_topic	TopicId: 7ElAqrjmRMKT7NUBSaYMMg	PartitionCount: 5	ReplicationFactor: 1	Configs: 
	Topic: session_two_first_topic	Partition: 0	Leader: 3	Replicas: 3	Isr: 3
	Topic: session_two_first_topic	Partition: 1	Leader: 1	Replicas: 1	Isr: 1
	Topic: session_two_first_topic	Partition: 2	Leader: 2	Replicas: 2	Isr: 2
	Topic: session_two_first_topic	Partition: 3	Leader: 3	Replicas: 3	Isr: 3
	Topic: session_two_first_topic	Partition: 4	Leader: 1	Replicas: 1	Isr: 1
```


## Hints
Please find below some hints related to `--alter` command
  * This command is **NOT RECOMMENDED** to run when your consumers are relying on key-based ordering as changing the number of partitions changes the key-hashing technique
  * You can only add partitions, not remove partitions


## Delete a Kafka topic
Deleting the topic **session_two_first_topic** when my Kafka broker is running at `localhost:19092`

as a first step let's try to list the current topics: 

```sh
kafka-topics.sh --bootstrap-server  localhost:19092 --list 
```

the output will be: 

```sh
session_two_first_topic
session_two_third_topic
```


```sh
docker exec -it kafka1  kafka-topics --bootstrap-server localhost:19092 --delete --topic session_two_first_topic
```

try no to list the topics, to verify that the **session_two_first_topic** was deleted:

```sh
docker exec -it kafka1  kafka-topics --list --bootstrap-server 127.0.0.1:19092
```

and the output will be: 

```sh
session_two_third_topic
```

## How to change a Kafka Topic Configuration
Broker configurations impact how topic behave in Kafka. You can find all broker configurations in the Kafka documentation, and you may choose while running Kafka in production to use the defaults or set your own values. The parameters you will set will impact topic performance and behavior (some explanation will be provide below, but in the Kafka Internals Session a full details will be provided), and the defaults are usually fine, but some topics may need different values than the defaults, for example for the following values:

  * Replication Factor
  * Number of Partitions
  * Message size
  * Compression level
  * Log Cleanup Policy
  * Min Insync Replicas


In order to override the Kafka Topics configuration defaults, we can use the `kafka-conφσfigs`. More details you can find [here](https://kafka.apache.org/documentation/#topicconfigs)

### Changing ```min.insync.replicas``` of a Kafka topic

Create a topic named configured-topic with 3 partitions and a replication factor of 1, using Kafka topics CLI, kafka-topics as described in previous sections. 

```sh
docker exec -it kafka1  kafka-topics --create --bootstrap-server 127.0.0.1:19092 --replication-factor 1 --partitions 3 --topic fun_topic
```

Describe the topic to check if there are any configuration override set for this topic.

```sh
docker exec -it kafka1  kafka-topics --describe --bootstrap-server 127.0.0.1:19092 --topic fun_topic
```

the output might be: 

```sh
Topic: fun_topic	TopicId: eLkka9WAQQmM6425SM5wSA	PartitionCount: 3	ReplicationFactor: 1	Configs: 
	Topic: fun_topic	Partition: 0	Leader: 2	Replicas: 2	Isr: 2
	Topic: fun_topic	Partition: 1	Leader: 3	Replicas: 3	Isr: 3
	Topic: fun_topic	Partition: 2	Leader: 1	Replicas: 1	Isr: 1

```

There is no configuration override set. Set the `min.insync.replicas` value for the topic *fun_topic* to 2. 

```sh 
docker exec -it kafka1 kafka-configs --bootstrap-server localhost:19092 --alter --entity-type topics --entity-name fun_topic --add-config min.insync.replicas=2

```

the output of the command will be: 

```sh
Completed updating config for topic fun_topic.
```

Describe again the topic to verify that the changes happened: 

```sh
docker exec -it kafka1  kafka-topics --describe --bootstrap-server 127.0.0.1:19092 --topic fun_topic
```

and the output will be: 

```sh
opic: fun_topic	TopicId: eLkka9WAQQmM6425SM5wSA	PartitionCount: 3	ReplicationFactor: 1	Configs: min.insync.replicas=2
	Topic: fun_topic	Partition: 0	Leader: 2	Replicas: 2	Isr: 2
	Topic: fun_topic	Partition: 1	Leader: 3	Replicas: 3	Isr: 3
	Topic: fun_topic	Partition: 2	Leader: 1	Replicas: 1	Isr: 1

```

You can observe that is a topic configuration override set (at the right side of the output) - min.insync.replicas=2.

You can delete the configuration override by passing `--delete-config` in place of the `--add-config` flag. 

```sh
docker exec -it kafka1 kafka-configs --bootstrap-server localhost:19092 --alter --entity-type topics --entity-name fun_topic --delete-config min.insync.replicas
```

