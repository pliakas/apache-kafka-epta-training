# Introduction
In this lab, you will use `kafka-console-producer` to producer messages with various keys. 

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
docker exec -it kafka1  kafka-topics --create --bootstrap-server 127.0.0.1:19092 --replication-factor 1 --partitions 3 --topic session_three_topic
```

3. After the finish of the lab, you can stop the container using the command:

```sh
docker-compose -f single_zookeeper_multiple_kafka.yml down
```



## Start sending messages (records) to a topic

To start producing to a topic you need to execute the following command to a new terminal:

```sh
docker exec -ti kafka1 kafka-console-producer --bootstrap-server 127.0.0.1:19092 --topic session_three_topic

```

and the command output will be:

```sh
>
```

After the producer is opened, you should see a > sign. Then any line of text you write afterwards will be sent to the Kafka topic (when pressing Enter)

```sh
>Hello World
>My name is Thomas
>Kafka is awesome!
>^C  (<- Ctrl + C is used to exit the producer)
```

### Important options you can set
You find below some important parameters that you can set: 



  * `--compression-codec`: To enable message compression, default gzip, possible values 'none', 'gzip', 'snappy', 'lz4', or 'zstd'
  * `--producer-property`: To pass in any producer property, such as the acks=all setting.
  * `--request-required-acks`:  An alternative to set the acks setting directly

### Hints & Troubleshooting
Here are some hints with the kafka-console-producer command:

  * Messages are sent with the null key by default (see below for more options)
  * If the topic does not exist, it can be auto-created by Kafka:

A topic with the name provided should exist. If you specify a topic that does not exist yet, a new topic with the name provided will be created with the default number of partitions and replication factor. These are controlled by the broker-side settings (in your config/server.properties file), with the following defaults:


## Produce messages from a file with the Kafka Console Producer CLI. 

Create a file topic-input-file.txt (make sure each message is on a new line)

```
Hello World
My name is Thomas Pliakas
```

execute the following command: 

```sh
docker exec -ti kafka1 kafka-console-producer --bootstrap-server 127.0.0.1:19092 --topic session_three_topic > ./topic-input-file.txt
```

## Produce messages with key in the Kafka Console Producer CLI?
By default messages sent to a Kafka topic will result in messages with null keys. You must use the properties parse.key and key.separator to send the key alongside messages. In this example, the separator between the key and the value is: `:`.

```sh
docker exec -ti kafka1 kafka-console-producer --bootstrap-server 127.0.0.1:19092 --topic session_three_topic --property parse.key=true --property key.separator=:

```

and the example input is: 

```
>key-one:sample-message-one-with-key-one
>key-two:message-with-key-two
```
Do not forget to always include your key/value separator otherwise you will get an exception.


