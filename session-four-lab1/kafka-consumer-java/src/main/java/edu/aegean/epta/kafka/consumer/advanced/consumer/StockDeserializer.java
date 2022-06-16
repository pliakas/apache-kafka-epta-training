package edu.aegean.epta.kafka.consumer.advanced.consumer;



import edu.aegean.epta.kafka.consumer.advanced.model.StockPrice;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

//TODO: Implement a custom StockPrice Deserialzier
public class StockDeserializer implements Deserializer<StockPrice> {

    @Override
    public StockPrice deserialize(final String topic, final byte[] data) {

        return null;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}