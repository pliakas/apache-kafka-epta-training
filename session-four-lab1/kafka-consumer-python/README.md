# Introduction
Implement a Kafka Consumer using python SDK provided by confluent. 

# Prerequisites
1. You need to start up the apache kafka cluster using the command

```
docker-compose -f single_zookeeper_multiple_kafka.yml up
```

2. Verify that the zookeeper and kafka cluster are up and running

```sh
docker ps
```
3. Create a topic with name **session-four-lab-topic-python*** with 3 partition and `replication-factor` to 3, using the following command:

```sh
docker exec -it kafka1  kafka-topics --create --bootstrap-server 127.0.0.1:19092 --replication-factor 3 --partitions 3 --topic  session-four-lab-topic-python
```

4. You need to startup a consumer that consume messages from the topic  **session-four-lab-topic-python**

```sh
docker exec -ti kafka1 kafka-console-producer --bootstrap-server localhost:19092 --topic session-four-lab-topic-python
```
5. You need to have python 3 and install confluent kafka client using the command:

```sh
pip install confluent-kafka
```

5. After the finish of the lab, you can stop the container using the command:

```sh
docker-compose -f single_zookeeper_multiple_kafka.yml down
```


## Start sending messages (records) to a topic

You need to edit the file `KafkaConsumer.py` in order to complete the needed steps for: 
* Configuring the consumer - You need to edit `createConsumer()` method.

In order to execute the client you can use the following command: 

```sh 
python3 KafkaConsumer.py
````
Verify the result by checking the console output of the consumer. 
