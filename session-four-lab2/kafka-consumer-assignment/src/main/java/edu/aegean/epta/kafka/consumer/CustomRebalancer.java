package edu.aegean.epta.kafka.consumer;

import edu.aegean.epta.kafka.model.StockPrice;
import org.apache.kafka.clients.consumer.Consumer;


// TODO Make CustomRebalancer implement ConsumerRebalanceListener
public class CustomRebalancer {

    private final Consumer<String, StockPrice> consumer;
    private final SeekToPosition seekTo; private boolean done;
    private final long location;
    private final long startTime = System.currentTimeMillis();



    public CustomRebalancer(final Consumer<String, StockPrice> consumer, final SeekToPosition seekTo, final long location) {
        this.seekTo = seekTo;
        this.location = location;
        this.consumer = consumer;
    }

    //TODO implement onPartitionsAssigned
    // HINT: public void onPartitionsAssigned(final Collection<TopicPartition> partitions) {
//        if (done) return;
//        else if (System.currentTimeMillis() - startTime > 30_000) {
//            done = true;
//            return;
//        }
    // TODO onPartitionsAssigned: if SeekTo is END call seekToEnd
    // TODO onPartitionsAssigned: if SeekTo is START call seekToBeginning
    // TODO onPartitionsAssigned: if SeekTo is LOCATION call consumer.seek

//        switch (seekTo) {
//            case END:                   //Seek to end
//                consumer.seekToEnd(partitions);
//                break;
//            case START:                 //Seek to start
//                consumer.seekToBeginning(partitions);
//                break;
//            case LOCATION:              //Seek to a given location
//                partitions.forEach(topicPartition ->
//                        consumer.seek(topicPartition, location));
//                break;
//        }



//    @Override
//    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
//
//    }

}