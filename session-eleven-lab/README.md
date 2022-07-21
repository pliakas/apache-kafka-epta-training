# Lab Instructions

## Prerequisites
You need to start up the apache kafka single node cluster using the command:

```shell
docker-compose -f single_zookeeper_single_kafka.yml up
```

Verify that the zookeeper and kafka containers are up and running
```shell
docker ps
```

After the finish of the lab, you can stop the container using the command:

```shell
docker-compose -f single_zookeeper_single_kafka.yml down
```

## Smple Kafka Stream Example
Demonstrates, using the high-level KStream DSL, how to implement the WordCount program that* computes a simple word occurrence histogram 
from an input text. This example uses lambda* expressions 

In this example, the input stream reads from a topic named `streams-plaintext-input`, where the values o  messages represent lines of text; 
and the histogram output is written to topic* `streams-wordcount-output`, where each record is an updated count of a single word, 
i.e. {@code word (String) -> currentCount (Long)}.* 

### How to run this example

Create the following topics
```shell
docker exec -ti kafka1 kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic streams-plaintext-input

docker exec -ti kafka1 kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic streams-wordcound-output
```

Start in a new terminal a producer
```shell
docker exec -ti kafka1 kafka-console-producer --broker-list localhost:9092 --topic streams-plaintext-input
```

 You can then enter input data by writing some line of text, followed by ENTER:

```shell
hello kafka streams<ENTER>
all streams lead to kafka<ENTER>
join kafka train<ENTER>
```


Start in a new terminal a consumer

```shell
docker exec -ti kafka1 kafka-console-consumer --bootstrap-server localhost:9092 \
                                              --topic streams-wordcount-output --from-beginning \
                                              --property print.key=true \
                                              --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
```

You should see output data similar to below. Please note that the exact output
sequence will depend on how fast you type the above sentences. If you type them 
slowly, you are likely to get each count update, e.g., kafka 1, kafka 2, kafka 3.
If you type them quickly, you are likely to get fewer count updates, e.g., just kafka 3.
This is because the commit interval is set to 10 seconds. Anything typed within
that interval will be compacted in memory.
```shell
hello    1
kafka    1
streams  1
all      1
streams  2
lead     1
to       1
join     1
kafka    3
train    1
```




## Map Function Example
Demonstrates how to perform simple, state-less transformations via map functions.  Use cases include e.g. basic data sanitization, 
data anonymization by obfuscating sensitive data fields (such as personally identifiable information aka PII). This specific example reads 
incoming text lines and converts each text line to all-uppercase.


## How to RUN it

Create the following topics
```shell
docker exec -ti kafka1 kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic TextLinesTopic

docker exec -ti kafka1 kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic UppercasedTextLinesTopic

docker exec -ti kafka1 kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic OriginalAndUppercasedTopic


```

Start in a new terminal a producer
```shell
docker exec -ti kafka1 kafka-console-producer --broker-list localhost:9092 --topic TextLinesTopic
```

You can then enter input data by writing some line of text, followed by ENTER:

```shell
hello kafka streams<ENTER>
all streams lead to kafka<ENTER>
join kafka train<ENTER>
```


Start in a new terminal a consumer for topic `UppercasedTextLinesTopic`, using the following command:

```shell
docker exec -ti kafka1 kafka-console-consumer --bootstrap-server localhost:9092 \
                                              --topic UppercasedTextLinesTopic \ 
                                              --from-beginning 
```


Start in a new terminal a consumer for topic `UppercasedTextLinesTopic`, using the following command:

```shell
docker exec -ti kafka1 kafka-console-consumer --bootstrap-server localhost:9092 \
                                              --topic OriginalAndUppercasedTopic \ 
                                              --from-beginning 
```



* 3) Start this example application either in your IDE or on the command line.
* <p>
* If via the command line please refer to <a href='https://github.com/confluentinc/kafka-streams-examples#packaging-and-running'>Packaging</a>.
* Once packaged you can then run:
* <pre>
* {@code
* $ java -cp target/kafka-streams-examples-7.1.1-standalone.jar io.confluent.examples.streams.MapFunctionLambdaExample
* }
* </pre>
* 4) Write some input data to the source topic (e.g. via {@code kafka-console-producer}). The already
* running example application (step 3) will automatically process this input data and write the
* results to the output topics.
* <pre>
* {@code
* # Start the console producer.  You can then enter input data by writing some line of text, followed by ENTER:
* #
* #   hello kafka streams<ENTER>
* #   all streams lead to kafka<ENTER>
* #
* # Every line you enter will become the value of a single Kafka message.
* $ bin/kafka-console-producer --broker-list localhost:9092 --topic TextLinesTopic
* }</pre>
* 5) Inspect the resulting data in the output topics, e.g. via {@code kafka-console-consumer}.
* <pre>
* {@code
* $ bin/kafka-console-consumer --topic UppercasedTextLinesTopic --from-beginning \
*                              --bootstrap-server localhost:9092
* $ bin/kafka-console-consumer --topic OriginalAndUppercasedTopic --from-beginning \
*                              --bootstrap-server localhost:9092 --property print.key=true
* }</pre>
* You should see output data similar to:
* <pre>
* {@code
* HELLO KAFKA STREAMS
* ALL STREAMS LEAD TO KAFKA
* }</pre>
* 6) Once you're done with your experiments, you can stop this example via {@code Ctrl-C}.  If needed,
* also stop the Kafka broker ({@code Ctrl-C}), and only then stop the ZooKeeper instance ({@code Ctrl-C}).
  */