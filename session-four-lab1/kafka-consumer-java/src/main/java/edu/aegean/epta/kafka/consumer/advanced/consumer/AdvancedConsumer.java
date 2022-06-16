package edu.aegean.epta.kafka.consumer.advanced.consumer;


import edu.aegean.epta.kafka.consumer.advanced.model.StockPrice;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AdvancedConsumer {

    private final static String TOPIC = "session-four-lab-topic-java";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

    private static Consumer<String, StockPrice> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());


        //TODO Configure the custom Deserializer
        // HINT ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        // HINT StockDeserializer.class.getName()
        // ???????


        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);

        // Create the consumer using props.
        final Consumer<String, StockPrice> consumer =
                new KafkaConsumer<>(props);


        // TODO Subscribe to the topic.
        // ???????


        return consumer;
    }


    //TODO finish this method.
    static void runConsumer() throws InterruptedException {
        final Consumer<String, StockPrice> consumer = createConsumer();
        final Map<String, StockPrice> map = new HashMap<>();
        try {
            final int giveUp = 1000; int noRecordsCount = 0;
            int readCount = 0;

            while (true) {

                // TODO read ConsumerRecords<String, StockPrice> consumerRecords
                // HINT final ConsumerRecords<String, StockPrice> consumerRecords =
                //        consumer.poll(1000);
                final ConsumerRecords<String, StockPrice> consumerRecords = null; //BROKEN

                if (consumerRecords.count() == 0) {
                    noRecordsCount++;
                    if (noRecordsCount > giveUp) break;
                    else continue;
                }
                readCount++;

                // Add records to map
                consumerRecords.forEach(record -> {
                    map.put(record.key(), record.value());
                });

                // Display records every 50 iterations
                if (readCount % 50 == 0) {
                    displayRecordsStatsAndStocks(map, consumerRecords);
                }

                // TODO Call commitAsync on consumer

            }
        }
        finally {
            consumer.close();
        }
        System.out.println("DONE");
    }

    //TODO finish this method.
    private static void displayRecordsStatsAndStocks(
            final Map<String, StockPrice> stockPriceMap,
            final ConsumerRecords<String, StockPrice> consumerRecords) {

        //TODO print out count and number of partitions.
        // HINT "New ConsumerRecords per count %d count %d\n",
        // HINT:
        // System.out.printf("New ConsumerRecords partition count %d count %d\n",
//                consumerRecords.partitions().size(),
//                consumerRecords.count());

        // TODO PRINT out
        //HINT....
//        stockPriceMap.forEach((s, stockPrice) ->
//                System.out.printf("ticker %s price %d.%d \n",
//                        stockPrice.getName(),
//                        stockPrice.getEuros(),
//                        stockPrice.getCents()));
        System.out.println();
    }


    public static void main(String... args) throws Exception {

        runConsumer();
    }


}