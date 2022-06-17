# Kafka Monitoring Stack for Docker Compose (Prometheus / Grafana)

This example  demonstrates how to use Prometheus and Grafana for monitoring an Apache Kafka cluster.



## Single Zookeeper / Multiple Kafka with Prometheus/Grafana

### Startup the cluster

To startup the full stack you need to execute the command below: 
```sh
docker-compose -f  single-zookeeper-multiple-kafka.yml up
```
----

To shutdown it you need to execute the following command, in a new terminal. 

```sh
docker-compose -f  single-zookeeper-multiple-kafka.yml down
```

### Multiple brokers WITHOUT zookeeper

You can start kafka cluster WITHOUT zookeeper using the command: 

```sh
docker-compose -f zkless-kafka-multiple-nodes-stack.yml  up 
```




### Create Topic

Create `sample-topic` with 6 partitions and 3 replicas.

```sh
docker exec -it kafka101 \
    kafka-topics \
    --create \
    --partitions 6 \
    --replication-factor 3 \
    --topic sample-topic \
    --bootstrap-server kafka101:29092
```

### Produce messages

Open a new terminal window, generate some message to simulate producer load.

```sh
docker exec -it kafka101 \
kafka-producer-perf-test \
--throughput 500 \
--num-records 100000000 \
--topic sample-topic \
--record-size 100 \
--producer-props bootstrap.servers=kafka101:29092
```


### Consume messages

Open a new terminal window, generate some message to simulate consumer load.

`**`sh
docker exec -it kafka101 \
kafka-consumer-perf-test \
--messages 100000000 \
--timeout 1000000 \
--topic sample-topic \
--reporting-interval 1000 \
--show-detailed-stats \
--bootstrap-server kafka101:29092
`****


### Open Grafana

Open your favorite web browser and open one of the provided Grafana dashboards :

* Kafka Cluster / Global Health Check 
* Kafka Cluster / Performance
* Kafka Cluster / Zookeeper Connections
* Kafka Cluster / JVM & OS
* Kafka Cluster / Hard disk usage
* Kafka Cluster / Topic Logs


**Hint One**: Grafana is accessible at the address : http://localhost:3000. With the following credentials:
  * user : `admin`
  * password : `kafka**


**Hint Two**: Prometheus is accessible at the address : http://localhost:9090

### Multiple brokers without zookeeper

