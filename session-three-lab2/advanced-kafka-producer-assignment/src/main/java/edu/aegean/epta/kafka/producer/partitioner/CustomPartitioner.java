package edu.aegean.epta.kafka.producer.partitioner;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

@Log4j2
public class CustomPartitioner implements Partitioner {

  private String mainKey = "main-key";

  private String retryKey = "retry-key";

  @Override
  public void configure(Map<String, ?> configMap) {
    log.info("Configure custom partition = {}", () -> configMap);
  }

  @Override
  /**TODO: Implement the custom partiotioner with the following requirements
   * messages with main-key are being sent on 0-6 partitions
   * messages with retry-key are being sent to 7-9 partitions. 
  **/
  public int partition(
      String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {

    if ((keyBytes == null) || (!(key instanceof String))) {
      throw new InvalidRecordException("All messages must have key");
    }

    return 0;
  }

  @Override
  public void close() {
    // do nothing
  }
}
