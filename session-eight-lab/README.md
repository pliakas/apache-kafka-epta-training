# Session Eight :: Cross Clustering Mirroring

In this session you will experiment mirror maker configuration

## Learning Objectives

* Configure a mirror-maker to replicate a topic `replicated-topic` to a cluster. 
* Verify configuration 
* Create different configuration and architecture patterns with mirror maker

## Startup Cluster

We have two clusters consisting of one zookeeper and one kafka

[Cluster A]
* zookeeper_1
* kafka_1

[Cluster_B]
* Zookeeper_2
* Kafka_2

To startup the cluster you need to execute the following command: 

```sh
docker-compose -f mirror-maker-example.yml up

```

## Create topics 
Create two topics (`simple-topic` & `replicated-topic`) for each cluster using the following command: 

[Cluster A]

```sh
docker exec -it session-eight-lab_kafka_1_1 \
    kafka-topics \
    --create \
    --partitions 6 \
    --replication-factor 1 \
    --topic sample-topic \
    --bootstrap-server kafka_1:19092


docker exec -it session-eight-lab_kafka_1_1 \
    kafka-topics \
    --create \
    --partitions 6 \
    --replication-factor 1 \
    --topic replicated-topic \
    --bootstrap-server kafka_1:19092
```

[Cluster B]
```sh
docker exec -it session-eight-lab_kafka_2_1 \
    kafka-topics \
    --create \
    --partitions 6 \
    --replication-factor 1 \
    --topic replicated-topic \
    --bootstrap-server kafka_2:29092


docker exec -it session-eight-lab_kafka_2_1 \
    kafka-topics \
    --create \
    --partitions 6 \
    --replication-factor 1 \
    --topic simple-topic \
    --bootstrap-server kafka_2:29092
```

**Note**: Only `replicated-topic` will be replicated in kafka_2

## Start producer for both topics in clusterA

Open a new terminal and start a producer to send messages to replicated-topic

```sh
docker exec -it session-eight-lab_kafka_1_1 kafka-console-producer --topic replicated-topic \ --bootstrap.servers=kafka_:19092
```

and send some messages

# Start consumer on both cluster for `replicated-topic`

you can start the consumer on clusterA using the following command: 

```sh
docker exec -ti session-eight-lab_kafka_1_1 kafka-console-consumer --bootstrap-server 127.0.0.1:19092 --topic replicated-topic --from-beginning
```

you can start the consumer on clusterB using the following command in second terminal: 

```sh
docker exec -ti session-eight-lab_kafka_2_1 kafka-console-consumer --bootstrap-server 127.0.0.1:29092 --topic replicated-topic --from-beginning
`****

## Zipkin

you can check the zipkin traces to verify that there is a connection and communication betweek kafka clusters using mirror-maker. You can find zipking UI in this ur: http://localhost:9411

## Assignments
To further extend this example you need to make the following actions: 

***[Assignment One]***
 Based on the above example create a hub and spoke architecture with three regional clusterand one global cluster. 
* Create the following topics: (topic-user-profile, topic-orders, topic-user-transactions**, where the topics topic-user-profile and topic-user-transactions must be replicated to global cluster. 

**Verify that the data are correctly transmitted** and **Verify in zipkin that there is traffic going between traffic.** 

***[Assignment Two]***
Create an active / active architecture, where two clusters are replicating all the topics that containing. Each cluster must have three topics with prefix `cluster_a_` and  `cluster_b_`. **Verify in zipkin that there is traffic going between traffic.**
