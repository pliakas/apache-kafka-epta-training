package edu.aegean.epta.kafka.consumer.advanced.producer;

import edu.aegean.epta.kafka.consumer.advanced.model.StockPrice;
import io.advantageous.boon.core.Lists;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StockPriceKafkaProducer {

    private final static String TOPIC = "session-four-lab-topic-java";
    private final static String BOOTSTRAP_SERVERS =
            "localhost:9092,localhost:9093,localhost:9094";

    private static final Logger logger =
            LoggerFactory.getLogger(StockPriceKafkaProducer.class);


    private static Producer<String, StockPrice>
    createProducer() {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "StockPriceKafkaProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StockSerializer.class.getName());
        props.put(ProducerConfig.LINGER_MS_CONFIG, 100);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,  16_384 * 4);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,
                CustomPartitioner.class.getName());
        props.put("importantStocks", "IBM,APPLE,NOKIA");
        return new KafkaProducer<>(props);
    }


    public static void main(String... args) throws Exception {

        //TODO: Create Kafka Producer
        final Producer<String, StockPrice> producer = createProducer();

        //TODO: Create StockSender list
        final List<StockSender> stockSenders = getStockSenderList(producer);

        //TODO: Create a thread pool so every stock sender gets it own.
        final ExecutorService executorService =
                Executors.newFixedThreadPool(stockSenders.size());

        //TODO: Run each stock sender in its own thread.
        stockSenders.forEach(executorService::submit);


        //TODO: Register nice shutdown of thread pool, then flush and close producer.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdown();
            try {
                executorService.awaitTermination(200, TimeUnit.MILLISECONDS);
                logger.info("Flushing and closing producer");
                producer.flush();
                producer.close();
            } catch (InterruptedException e) {
                logger.warn("shutting down", e);
            }
        }));
    }




    private static List<StockSender> getStockSenderList(
            final Producer<String, StockPrice> producer) {
        return Lists.list(
                new StockSender(TOPIC,
                        new StockPrice("IBM", 100, 99),
                        new StockPrice("IBM", 50, 10),
                        producer,
                        100, 1000
                ),
                new StockSender(
                        TOPIC,
                        new StockPrice("APPLE", 1000, 99),
                        new StockPrice("APPLE", 50, 0),
                        producer,
                        100, 1000                ),
                new StockSender(
                        TOPIC,
                        new StockPrice("AWS", 100, 99),
                        new StockPrice("AWS", 50, 10),
                        producer,
                        100, 1000
                ),
                new StockSender(
                        TOPIC,
                        new StockPrice("MICROSOFT", 500, 99),
                        new StockPrice("MICROSOFT", 400, 10),
                        producer,
                        100, 1000
                ),
                new StockSender(
                        TOPIC,
                        new StockPrice("DELL", 100, 99),
                        new StockPrice("DELL", 50, 10),
                        producer,
                        100, 1000                ),
                new StockSender(
                        TOPIC,
                        new StockPrice("HP", 100, 99),
                        new StockPrice("HP", 50, 10),
                        producer,
                        100, 1000
                ),
                new StockSender(
                        TOPIC,
                        new StockPrice("COSMOTE", 100, 99),
                        new StockPrice("COSMOTE", 50, 10),
                        producer,
                        100, 1000
                ),
                new StockSender(
                        TOPIC,
                        new StockPrice("NOKIA", 100, 99),
                        new StockPrice("NOKIA", 50, 10),
                        producer,
                        100, 1000
                ),
                new StockSender(
                        TOPIC,
                        new StockPrice("ORACLE", 100, 99),
                        new StockPrice("ORACLE", 50, 10),
                        producer,
                        100, 1000
                ),
                new StockSender(
                        TOPIC,
                        new StockPrice("REDHAT", 100, 99),
                        new StockPrice("REDHAT", 50, 10),
                        producer,
                        100, 1000
                ),
                new StockSender(
                        TOPIC,
                        new StockPrice("ELASTIC", 100, 99),
                        new StockPrice("ELASTIC", 50, 10),
                        producer,
                        100, 1000
                ),
                new StockSender(
                        TOPIC,
                        new StockPrice("LENOVO", 100, 99),
                        new StockPrice("LENOVO", 50, 10),
                        producer,
                        100, 1000
                )
        );

    }
}