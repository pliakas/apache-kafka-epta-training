# Introduction
In this lab, you will use `kafka-console-consumer` to producer messages with various keys. 

## Prerequisites

1. You need to start up the apache kafka single node cluster using the command

```
docker-compose -f single_zookeeper_multiple_kafka.yml up
```

2. Verify that the zookeeper and kafka containers are up and running

```sh
docker ps
```
3. Create a topic with name **session_three_topic*** with 3 partition and `replication-factor` to 1, using the following command:

```sh
docker exec -it kafka1  kafka-topics --create --bootstrap-server 127.0.0.1:19092 --replication-factor 1 --partitions 3 --topic session_four_topic
```

3. After the finish of the lab, you can stop the container using the command:

```sh
docker-compose -f single_zookeeper_multiple_kafka.yml down
```

To start producing to a topic you need to execute the following command to a new terminal:

```sh
docker exec -ti kafka1 kafka-console-producer --bootstrap-server 127.0.0.1:19092 --topic session_four_topic

```


## Start consuming  messages (records) to a topic

### Only for the future messages

To start consuming to a topic, for only the new messages, you need to execute the following command to a new terminal:

```sh
docker exec -ti kafka1 kafka-console-consumer --bootstrap-server 127.0.0.1:19092 --topic session_four_topic

```

### Consuming all the historical messages and future ones in a kafka topic 

```sh
docker exec -ti kafka1 kafka-console-consumer --bootstrap-server 127.0.0.1:19092 --topic session_four_topic  --from-beginning

```

Note that, the Kafka Console Consumer will remain opened until you exit it, and will keep on displaying messages to the screen. It assumes all the messages coming in can be de-serialized as text (`String`). By default, the Kafka Console Consumer does not show the key, or any partition information. If you do not see any output but you know your Kafka topic has data in it, don't forget to use the `--from-beginning option`.

### Important options you can set


  * `--from-beginning`: To read all historical messages
  * `--formatter` : To display messages in a particular format (example below to display keys)
  * `--consumer-property` : To pass in any consumer property, such as the allow.auto.create.topics setting
  * `--group` : By default a random consumer group ID is chosen, but you can override it with this option. 
  * `--max-messages` : Number of messages to consume before exiting
  * `--partition`: If you want to only consume from a specific partition.

### Hints & Troubleshooting
Here are some hints when using the `kafka-console-consumer.sh` command:


  * Messages by default will not display the key or metadata information (see below for how to do it).
  * When you start a kafka-console-consumer, unless specifying the --from-beginning option, only future messages will be displayed and read.
  * If the topic does not exist, the console consumer will automatically create it with default
  * You can consume multiple topics at a time with a comma-delimited list or a pattern.
  * If a consumer group id is not specified, the `kafka-console-consumer` generates a random consumer group.
  * If messages do not appear in order, remember that the order is at the partition level, not at the topic level.

## Consumer a Kafka topic and show both key and value

By default, the console consumer will show only the value of the Kafka record. Using the command below you can show both the key and value. Using the formatter `kafka.tools.DefaultMessageFormatter and` using the properties `print.timestamp=true`` print.key=true` `print.value=true`:


```
docker exec -ti kafka1 kafka-console-consumer --bootstrap-server 127.0.0.1:19092 --topic session_four_topic  --from-beginning --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true

```

More properties are available such as: `print.partition`, `print.offset`, `print.headers`, `key.separator`, `line.separator`, `headers.separator`. Try to experiment with each one of the properties.


## Kafka Consumers in Group CLI
In this section, we will experiment with different scenarios to learn how Kafka consumers in a consumer group work using Kafka console consumer CLI. In addition, we will provide an optional consumer group parameter with `--group` flag.

How to create consumers in a Kafka Consumer Group? 

To start consumers in a consumer group, do the following:

  * Create a topic with at least 3 partitions and send data to it. 
  * Create a first `kafka-console-consumer` and assign a group name with `--group`.
  * Create a second `kafka-console-consumer` and use the same `--group` argument, in another terminal. 
  * Send data to the topic and you will see consumers sharing the reads


### Create Consumer Group Example
Launch a consumer in a consumer group, named `my-first-group`, using a command: 

```sh
docker exec -ti kafka1 kafka-console-consumer --bootstrap-server 127.0.0.1:19092 --topic session_four_topic --group my-first-group --property print.partition=true
```

You can add more consumers, by executing the same command in a terminal. 

*Question*: If you add more consumers to a consumer group, that are more than the partition number of the topic, what will happen? 

*Hint*: Observe that each consumer is reading data from specific partition(s). 

  * *Action One*: Try to stop one of the consumer and verify that the remaining consumer(s) are reading messages, from all partitions. 
  * *Action Two*: Stop All consumers *BUT* keep on producing some messages. Upon restart of a consumer in the group, the consumer will read from the latest committed message offsets and read only the messages that you have produced. *Verify this behavior*.
  
  
## Kafka Consumer Group Management

The Kafka Consumer Groups CLI `kafka-consumer-groups` is used to manage consumer groups in Kafka. Make sure you have started Kafka beforehand. 

### Reset a Kafka consumer group using CLI

To reset a Kafka consumer groups, we need to: 
 * Define the broker hostname and port e.g., localhost:9092
 * Understand the offset reset strategy (to earliest, to latest, to specific offset, shift by...)
 * Stop the running consumer groups (otherwise the command will fail)
 * Use the `kafka-consumer-groups.sh` CLI with the `--reset-offsets` option
 
 Ensure that all the consumer are stopped, using the command: 
 
 ```sh
  docker exec -ti kafka1 kafka-consumer-groups --bootstrap-server 127.0.0.1:19092 --group my-first-group --describe
 ```

the output mus be:

```sh
Consumer group 'my-first-group' has no active members.

GROUP           TOPIC              PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID     HOST            CLIENT-ID
my-first-group  session_four_topic 0          9               26              17              -               -               -
my-first-group  session_four_topic 1          11              11              0               -               -               -
my-first-group  session_four_topic 2          12              12              0               -               -               -


```

Observe the current offsets (it might be different than the example) for your consumer groups. Now, you will reset the offsets to the earliest position in order to read the topic entirely again using the command: 

```sh
docker exec -ti kafka1 kafka-consumer-groups --bootstrap-server 127.0.0.1:19092 --group my-first-group --reset-offsets --to-earliest --execute --topic session_four_topic
```

and the output will be : 

```sh
GROUP                          TOPIC                          PARTITION  NEW-OFFSET
my-first-group                 session_four_topic             0          0
my-first-group                 session_four_topic             1          0
my-first-group                 session_four_topic             2          0
```


As you can see the new offsets for that consumer group for all partitions are 0, which means that upon restarting a consumer in that group, it will read from the beginning of each partition. Verify this by starting a consumer for the specific consumer group. 

  *Action One*: Try to reset the offsets by shifting `-2`, using the option `--shift-by`. What will happen?

### List of all Kafka Consumer groups using the CLI
Listing the consumer groups help you understand which ones could be down, or not stable. Listing consumer groups state command is: 

```
docker exec -ti kafka1 kafka-consumer-groups --bootstrap-server 127.0.0.1:19092 --list --state
```

*Note*: Create two consumer groups (e.g my-first-group, my-second-group) with at least two consumer per group. 

### Describe all consumer groups and state 
To describe all consumer groups and states you can use the following command:


```sh
docker exec -ti kafka1 kafka-consumer-groups --bootstrap-server 127.0.0.1:19092 --all-groups --state

```

### Deleting a consumer group 
To delete a consumer group in order to reset entirely the reading mechanism. For this, you can use the delete option:

```
docker exec -ti kafka1 kafka-consumer-groups --bootstrap-server 127.0.0.1:19092 --delete --group my-first-group
```

Alternatively, if you want to only delete offsets for a specific topic (helpful when your consumer group is reading from multiple topics) you can use the following command:

```sh
docker exec -ti kafka1 kafka-consumer-groups --bootstrap-server 127.0.0.1:19092 --group my-first-group --delete-offsets --topic session_four_topic

```
