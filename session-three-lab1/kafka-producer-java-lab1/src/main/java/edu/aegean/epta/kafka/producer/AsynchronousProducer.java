package edu.aegean.epta.kafka.producer;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AsynchronousProducer {

    //TODO change this to match the name of topic you created with the script.
    private final static String TOPIC = "session-three-lab-two-topic";

    //TODO change this to pass a comma delimited list servers (localhost:9092,localhost:9093,localhost:9094)
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";

    //TODO:  EDIT FireAndForgetProducer.java and define createProducer.
    private static Producer<Long, String> createProducer() {

        //TODO Create props and set ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to BOOTSTRAP_SERVERS constant

        // This sets up the bootstrap Kafka brokers.
        // TODO Give the Kafka producer a client id.
        // HINT props.put(ProducerConfig.CLIENT_ID_CONFIG, "AsynchronousProducer");
        // TODO setup ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
        // TODO setup ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
        // TODO return new KafkaProducer<>(props);
        return null;
    }


    //TODO: EDIT AsynchronousProducer.java and finish runProducer.
    // HINT Async
    static void runProducer(final int sendMessageCount) throws InterruptedException {
        final Producer<Long, String> producer = createProducer();
        long time = System.currentTimeMillis();
        final CountDownLatch countDownLatch = new CountDownLatch(sendMessageCount);

        try {
            for (long index = time; index < time + sendMessageCount; index++) {

               //TODO: create a new record. Use the index as the record key.
                final ProducerRecord<Long, String> record =
                        new ProducerRecord<>(TOPIC, index, "Hello Async Message " + index);

                //TODO: Implement async send (using callback or lambdas
                //HINT: Add countDownLatch.countDown(); on the end of the implementation.

            }
            countDownLatch.await(25, TimeUnit.SECONDS);
        } finally {
            producer.flush();
            producer.close();
        }
    }

    //  send 10 messages
    public static void main(String... args) throws Exception {
        runProducer(10);
    }
}
