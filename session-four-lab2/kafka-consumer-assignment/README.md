# Implementing a Kafka Consumer and managing Offsets 
In this lab, you are going to control consumer position.
# Prerequisites

1. You need to start up the apache kafka single node cluster using the command

```
docker-compose -f single_zookeeper_multiple_kafka.yml up
```

2. Verify that the zookeeper and kafka containers are up and running

```sh
docker ps
```
3. Create a topic with name **session-four-lab-two** with 3 partition and `replication-factor` to 3, using 
   the following command:

```sh
docker exec -it kafka1  kafka-topics --create --bootstrap-server 127.0.0.1:19092 --replication-factor 3 --partitions 
3 --topic session-four-lab-two
```

4. After the finish of the lab, you can stop the container using the command:

```sh
docker-compose -f single_zookeeper_multiple_kafka.yml down
```


## Assignment Description
Consumer position is the topic, partition and offset of the last record per partition consuming. Offset for each 
record in a partition as a unique identifier record location in the partition. Consumer position gives offset of 
next (highest) record that it consumes and the position advances automatically for each call to `poll(..)`.  

### Consumer committed Position
Consumer committed position is the last offset that has been stored to the Kafka broker if the consumer fails, this 
allows the consumer to picks up at the last committed position. Consumer can auto commit offsets (`enable.auto.commit`)
periodically (`auto.commit.interval.ms`) or do commit explicitly using `commitSync()` and `commitAsync()`.  

### Consumer Groups
Kafka organizes Consumers into consumer groups. Consumer instances that have the same group.id are in the same 
consumer group. Pools of consumers in a group divide work of consuming and processing records. In the consumer 
groups, processes and threads can run on the same box or run distributed for scalability/fault tolerance.  
 

### Partition Reassignment
Consumer partition reassignment in a consumer group happens automatically. Consumers are notified via 
`ConsumerRebalanceListener` and triggers consumers to finish necessary clean up. A Consumer can use the API to assign 
specific partitions using the assign(Collection) method, but using assign disables dynamic partition assignment and 
consumer group coordination. Dead consumers may see `CommitFailedException` thrown from a call to` commitSync()`. Only 
active members of consumer group can commit offsets.    

### Controlling Consumers Position
You can control consumer position moving to forward or backward. Consumers can re-consume older records or skip to 
the most recent records. 

### Action 1: Managing Offsets
Use `consumer.seek(TopicPartition, long)` to specify. E.g. `consumer.seekToBeginning(Collecion)` and `consumer.seekToEnd`
(Collection) Use Case Time-sensitive record processing: Skip to most recent records.

For the consumer to manage its own offset you just need to do the following: Set enable.auto.commit = false Use 
offset provided with each ConsumerRecord to save your position (partition/offset). On restart restore consumer 
position using `kafkaConsumer.seek(TopicPartition, long)`. Usage like this simplest when the partition assignment is 
also done manually using `assign()` instead of` subscribe()`.  

If using automatic partition assignment, you must handle cases where partition assignments changes. Pass 
`ConsumerRebalanceListener` instance in call to `kafkaConsumer.subscribe(Collection, ConsumerRebalanceListener)` and 
`kafkaConsumer.subscribe(Pattern, ConsumerRebalanceListener)`. When partitions taken from consumer, commit its offset 
for partitions by implementing `ConsumerRebalanceListener.onPartitionsRevoked(Collection)` and when partitions are 
assigned to consumer, look up offset for new partitions and correctly initialize consumer to that position by 
implementing `ConsumerRebalanceListener.onPartitionsAssigned(Collection)`.

You need to edit the following classes: 
* edu.aegean.epta.kafka.consumer.AdvancedConsumer.java
* edu.aegean.epta.kafka.consumer.CustomRebalancer.java


### Action 2 : Run Different scenarios
* RUN AdvancedConsumer with moving to start of log (e.g. java AdvancedConsumer START)
* RUN StockPriceKafkaProducer
* RUN AdvancedConsumer with moving to end of log
* RUN AdvancedConsumer run with moving to a certain location in log


 
It should all run. Stop consumer and producer when finished.
