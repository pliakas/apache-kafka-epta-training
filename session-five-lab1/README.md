# Reliable Producer and Consumer 

In this assignment you will implement a producer and consumer to create a reliable data delivery

## Reliable Producer
You have to implement a reliable producer that send messages to kafka broker only when all replicas are app. No 
messages will be accepted if only one broker is available

## Reliable Consumer
You need to implement a consumer that accept messages and : 
* Commit messages per record 
* Failed processed messages must be forwarded to different topic 
* Scale up to five consumer in the same consumer group.
* During messages try to shutdown some consumers 


# Prerequisites

To startup up the kafka cluster you need to execute the following command: 
```shell
docker-compose -f single_zookeeper_multiple_kafka.yml up 
```

To shutdown the kafka cluster you need to execute the following command in a new terminal: 

```shell
docker-compose -f single_zookeeper_multiple_kafka.yml up 

```
To build the docker image for consumer you need to execute the following command: 

```shell
cd java-reliable-consumer
mvn clean install
docker build -t epta/java-reliable-consumer .  
```


To build the docker image for producer you need to execute the following command:

```shell
cd java-reliable-producer
mvn clean install
docker build -t epta/java-reliable-producer .  
```

To startup the producer and consumer messages with docker file you can execute the following command 

```shell
docker-compose -f docker-compose-dev-app.yml up
```

It wil start one producer and 3 consumers. 

To trigger 100  messages to be sent by producers you can use the above curl command: 

```shell
curl -X GET --location "http://localhost:42052/kafka/producer/100"
```