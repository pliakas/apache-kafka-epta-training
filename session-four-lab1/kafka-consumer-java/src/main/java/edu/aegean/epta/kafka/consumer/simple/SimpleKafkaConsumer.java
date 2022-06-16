package edu.aegean.epta.kafka.consumer.simple;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

public class SimpleKafkaConsumer {

    private final static String TOPIC = "session-four-lab-topic-java";
    private final static String BOOTSTRAP_SERVERS =
            "localhost:9092,localhost:9093,localhost:9094";


    // TODO: Edit this method to create a consumer
    private static KafkaConsumer<Long, String> createConsumer() {

        //TODO : Create a Properties object and put the needed configuration
        Properties props = new Properties();

        //TODO:  Create the consumer using props.
        final KafkaConsumer<Long, String> consumer = null;

        // TODO: Subscribe to the topic.


        return consumer;
    }


    // TODO: Edit this method to consumer messages
    static void runConsumer() throws InterruptedException {
        final KafkaConsumer<Long, String> consumer = createConsumer();
        try {
            //TODO: Implement the code to read only the new messages.
        } finally {
            consumer.close();
        }

        System.out.println("DONE");
    }


    public static void main(String... args) throws Exception {
        System.setProperty("io.advantageous.boon.faststringutils.disable", "true");
        runConsumer();
    }

}
