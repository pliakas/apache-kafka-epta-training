# Epta :: Apache Kafka 

# Docker Compose Files

This replicates as well as possible real deployment configurations, where you have your zookeeper servers and kafka servers actually all distinct from each other. This solves all the networking hurdles that comes with Docker and docker-compose, and is compatible cross platform.

## Stack version

  - Zookeeper version: 3.6.3 (Confluent 7.1.1)
  - Kafka version: 3.1.0 (Confluent 7.1.1)

# Requirements

You need later docker version for different operating systems.

## Docker

Please export your environment before starting the stack:
```
export DOCKER_HOST_IP=127.0.0.1
```
(that's the default value and you actually don't need to do a thing)

## Single Zookeeper / Single Kafka

This configuration fits most development requirements.

 - Zookeeper will be available at `$DOCKER_HOST_IP:2181`
 - Kafka will be available at `$DOCKER_HOST_IP:9092`
 - (experimental) JMX port at `$DOCKER_HOST_IP:9999`

Run with:
```
docker-compose -f single_zookeeper_single_kafka.yml up
```

Clean state (zookeeper & kafka) with : 
```
docker-compose -f single_zookeeper_single_kafka.yml down
```

## Single Zookeeper / Multiple Kafka

If you want to have three brokers and experiment with kafka replication / fault-tolerance.

- Zookeeper will be available at `$DOCKER_HOST_IP:2181`
- Kafka will be available at `$DOCKER_HOST_IP:9092,$DOCKER_HOST_IP:9093,$DOCKER_HOST_IP:9094`


Run with:
```
docker-compose -f single_zookeeper_multiple_kafka.yml up
```

Clean state (zookeeper & kafka) with :
```
docker-compose -f single_zookeeper_multiple_kafka.yml down
```

# FAQ

## Kafka

**Q: Kafka's log is too verbose, how can I reduce it?**

A: Add the following line to your docker-compose environment variables: `KAFKA_LOG4J_LOGGERS: "kafka.controller=INFO,kafka.producer.async.DefaultEventHandler=INFO,state.change.logger=INFO"`. Full logging control can be accessed here: https://github.com/confluentinc/cp-docker-images/blob/master/debian/kafka/include/etc/confluent/docker/log4j.properties.template

**Q: How do I delete data to start fresh?**

A: Your data is persisted from within docker volumes , so if you want for example to reset the data in the full-stack docker compose, do a `docker-compose -f full-stack.yml rm`.

**Q: Can I change the zookeeper ports?**

A: yes. Say you want to change `zoo1` port to `12181` (only relevant lines are shown):
```
  zoo1:
    ports:
      - "12181:12181"
    environment:
        ZOO_PORT: 12181
        
  kafka1:
    environment:
      KAFKA_ZOOKEEPER_CONNECT: "zoo1:12181"
```

**Q: Can I change the Kafka ports?**

A: yes. Say you want to change `kafka1` port to `12345` (only relevant lines are shown). Note only `LISTENER_DOCKER_EXTERNAL` changes:
```
  kafka1:
    image: confluentinc/cp-kafka:6.2.1
    hostname: kafka1
    ports:
      - "12345:12345"
    environment:
      KAFKA_ADVERTISED_LISTENERS: LISTENER_DOCKER_INTERNAL://kafka1:19092,LISTENER_DOCKER_EXTERNAL://${DOCKER_HOST_IP:-127.0.0.1}:12345
```

**Q: Kafka is using a lot of disk space for testing. Can I reduce it?**

A: yes. This is for testing only!!! Reduce the KAFKA_LOG_SEGMENT_BYTES to 16MB and the KAFKA_LOG_RETENTION_BYTES to 128MB

```
  kafka1:
    image: confluentinc/cp-kafka:6.2.1
    ...
    environment:
      ...
      # For testing small segments 16MB and retention of 128MB
      KAFKA_LOG_SEGMENT_BYTES: 16777216
      KAFKA_LOG_RETENTION_BYTES: 134217728
```

**Q: How do I expose kafka?**

A: If you want to expose kafka outside of your local machine, you must set `KAFKA_ADVERTISED_LISTENERS` to the IP of the machine so that kafka is externally accessible. To achieve this you can set `LISTENER_DOCKER_EXTERNAL` to the IP of the machine.
For example, if the IP of your machine is `50.10.2.3`, follow the sample mapping below:

```
  kafka1:
    image: confluentinc/cp-kafka:6.2.1
    ...
    environment:
      ...
      KAFKA_ADVERTISED_LISTENERS: LISTENER_DOCKER_INTERNAL://kafka2:19093,LISTENER_DOCKER_EXTERNAL://50.10.2.3:9093
```
