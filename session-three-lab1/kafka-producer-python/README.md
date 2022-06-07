# Introduction
In this lab, you will use `kafka-console-producer` to producer messages with various keys. 

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
5. You need to have python 3 and install confluent kafka client using the command:

```sh
pip install confluent-kafka
```

5. After the finish of the lab, you can stop the container using the command:

```sh
docker-compose -f single_zookeeper_multiple_kafka.yml down
```


## Start sending messages (records) to a topic
You need to edit the file `KafkaProducer.py` in order to complete the needed steps for: 
* Configuring the producer - You need to edit `createProducer()` method.
* Implement the sending functionality - You need to edit `runProducer()` method. 

In order to execute the client you can use the following command: 

```sh 
python3 KafkaProducer.py
````
Verify the result by checking the console output of the consumer. 
