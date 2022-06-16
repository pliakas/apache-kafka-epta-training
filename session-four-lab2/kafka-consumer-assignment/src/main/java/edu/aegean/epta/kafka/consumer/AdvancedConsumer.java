package edu.aegean.epta.kafka.consumer;

import edu.aegean.epta.kafka.model.StockPrice;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AdvancedConsumer {

    private final static String TOPIC = "session-four-lab-two";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";


        //TODO finish this method
        private static void runConsumer(final SeekToPosition seekTo, final long location,
                                        final int readCountStatusUpdate) throws InterruptedException {
            final Map<String, StockPrice> map = new HashMap<>();

            //TODO fix this...
            // HINT consumer  = createConsumer(seekTo, location)

            try (final Consumer<String, StockPrice> consumer = null) {
                 // ) {
                final int giveUp = 1000; int noRecordsCount = 0; int readCount = 0;
                while (true) {
                    final ConsumerRecords<String, StockPrice> consumerRecords = consumer.poll(1000);
                    if (consumerRecords.count() == 0) {
                        noRecordsCount++;
                        if (noRecordsCount > giveUp) break;
                        else continue;
                    }
                    readCount++;
                    consumerRecords.forEach(record -> {
                        map.put(record.key(), record.value());
                    });
                    if (readCount % readCountStatusUpdate == 0) {
                        displayRecordsStatsAndStocks(map, consumerRecords);
                    }
                    consumer.commitAsync();
                }
            }
            System.out.println("DONE");
        }

        //TODO finish this method
        private static Consumer<String, StockPrice> createConsumer(final SeekToPosition seekTo,
                                                                   final long location) {
            final Properties props = initProperties();

            // Create the consumer using props.
            final Consumer<String, StockPrice> consumer =
                    new KafkaConsumer<>(props);

            // TODO Create CustomRebalancer and assign it to consumerRebalanceListener
            final ConsumerRebalanceListener consumerRebalanceListener = null; //BROKE FIX
            // HINT        new CustomRebalancer(consumer, seekTo, location);

            // Subscribe to the topic.
            consumer.subscribe(Collections.singletonList(TOPIC), consumerRebalanceListener);
            return consumer;
        }



        private static Properties initProperties() {
            final Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

            //Custom Deserializer
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StockDeserializer.class.getName());
            props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
            return props;
        }




        private static void displayRecordsStatsAndStocks(
                final Map<String, StockPrice> stockPriceMap,
                final ConsumerRecords<String, StockPrice> consumerRecords) {
            System.out.printf("New ConsumerRecords partition size %d count %d\n",
                    consumerRecords.partitions().size(),
                    consumerRecords.count());
            stockPriceMap.forEach((s, stockPrice) ->
                    System.out.printf("ticker %s price %d.%d \n",
                            stockPrice.getName(),
                            stockPrice.getEuros(),
                            stockPrice.getCents()));
            System.out.println();
        }

    //HINT: DO not edit
    public static void main(String... args) throws Exception {
        System.setProperty("io.advantageous.boon.faststringutils.disable", "true");

        SeekToPosition seekTo = SeekToPosition.NONE;
        long location = -1;
        int readCountStatusUpdate = 100;

        if (args.length >= 1) {
            seekTo = SeekToPosition.valueOf(args[0].toUpperCase());
            if (seekTo.equals(SeekToPosition.LOCATION)) {
                location = Long.parseLong(args[1]);
            }
        }
        if (args.length == 3) {
            readCountStatusUpdate = Integer.parseInt(args[2]);
        }
        runConsumer(seekTo, location, readCountStatusUpdate);
    }

}