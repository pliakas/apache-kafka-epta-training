package edu.aegean.epta.kafka.admin;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.ElectionType;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.errors.ElectionNotNeededException;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class AdminClientTopicOperations {

    private final static String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";
    private static AdminClient client = null;
    private static String TOPIC_NAME = "my-sample-topic";


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        System.out.println("\n\nInitializing Kafka's Admin Client");
        initializeClient();

        System.out.println("\n\nGet Cluster Metadata");
        retrieveClusterMetadata();

        System.out.println("\n\nInitial: Retrieving available topics ");
        listTopics();

        System.out.println("\n\nAction 1: Creating topic");
        createTopic("my-sample-topic");

        System.out.println("\n\nAfter Creation : Retrieving available topics ");
        listTopics();

        System.out.println("\n\nAfter Creation : Retrieving available topics ");
        retrieveTopicDescription(TOPIC_NAME);


        System.out.println("\n\nChanging Properties for topic");
        changingTopicProperties(TOPIC_NAME);

        System.out.println("\n\nAdding Partition to topic ");
        addingPartitionToTopic(TOPIC_NAME);

        System.out.println("Prefered Leader Election");
        preferedLeaderElection(TOPIC_NAME);

        System.out.println("\n\nReassign Replicas");
        reassignReplicas("my-sample-topic");

        System.out.println("\n\nDeleting Topic");
        deleteTopic(TOPIC_NAME);

        System.out.println("\n\nAfter Deletion: Retrieving available topics ");
        listTopics();
    }


    private static void initializeClient() {
        Map<String, Object> conf = new HashMap<>();
        conf.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
//        conf.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "5000");
        client = AdminClient.create(conf);
    }

    private static void retrieveClusterMetadata() throws ExecutionException, InterruptedException {
        DescribeClusterResult cluster = client.describeCluster();
        System.out.println("Connected to cluster " + cluster.clusterId().get());
        System.out.println("The brokers in the cluster are:");
        System.out.println("The Controller is: " + cluster.controller().get());
        cluster.nodes().get().forEach(node -> System.out.println("Nodes: " + node));
    }

    public static void listTopics() throws ExecutionException, InterruptedException {
        ListTopicsResult ltr = client.listTopics();
        KafkaFuture<Set<String>> names = ltr.names();

        names.get().forEach(entry -> System.out.println("Found topic with name: " + entry));
    }

    public static void createTopic(String topicName) {
        System.out.println("Creating a topic with name: " + topicName);

        int partitions = 8;
        short replicationFactor = 2;


        try {
            KafkaFuture<Void> future = client
                    .createTopics(Collections.singleton(
                            new NewTopic(topicName, partitions, replicationFactor)),
                            new CreateTopicsOptions().timeoutMs(10000))
                    .all();
            future.get();

        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void retrieveTopicDescription(String topicName) throws ExecutionException, InterruptedException {
        DescribeTopicsResult topicsResult = client.describeTopics(Collections.singleton(topicName));
        var topicDescription = topicsResult.values().get(topicName).get();

        System.out.println("Description of demo topic:" + topicDescription);
    }

    private static void addingPartitionToTopic(String topicName) throws ExecutionException, InterruptedException {
        Map<String, NewPartitions> newPartitions = new HashMap<>();
        newPartitions.put(topicName, NewPartitions.increaseTo(8 +2));
        client.createPartitions(newPartitions).all().get();
    }

    public static void changingTopicProperties(String topicName) throws ExecutionException, InterruptedException {
        ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);

        // get the current topic configuration
        DescribeConfigsResult describeConfigsResult = client.describeConfigs(Collections.singleton(resource));
        Config config = describeConfigsResult.all().get().get(resource);

        System.out.println("Printing all ");
        System.out.println("Initial :: ChangingTopicProperties :: Printing new configuration: " + describeConfigsResult.all().get());

        System.out.println("Printing non-defaults: ");
        config.entries().stream().filter(
                entry -> !entry.isDefault()).forEach(System.out::println);


        // create a new entry for updating the retention.ms value on the same topic
        ConfigEntry retentionEntry = new ConfigEntry(TopicConfig.RETENTION_MS_CONFIG, "40000");
        Map<ConfigResource, Collection<AlterConfigOp>> updateConfig = new HashMap<ConfigResource, Collection<AlterConfigOp>>();

        updateConfig.put(resource, Collections.singleton(new AlterConfigOp(retentionEntry, AlterConfigOp.OpType.SET)));
        AlterConfigsResult alterConfigsResult = client.incrementalAlterConfigs(updateConfig);
        alterConfigsResult.all();


        // print the results
        var updateResult = client.describeConfigs(Collections.singleton(resource));
        System.out.println("After :: ChangingTopicProperties :: Printing new configuration: " + updateResult.all().get());

        //print no defaults
        System.out.println("After :: Printing non-defaults: ");
        Config updated = updateResult.all().get().get(resource);
        updated.entries().stream().filter(
                entry -> !entry.isDefault()).forEach(System.out::println);
    }

    public static void deleteTopic(String topicName) {
        KafkaFuture<Void> future = client.deleteTopics(Collections.singleton(topicName)).all();

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    // TODO: Edit here to re-assign replicas
    private static void reassignReplicas(String topicName) throws ExecutionException, InterruptedException {

        Map<TopicPartition, Optional<NewPartitionReassignment>> reassignment = new HashMap<>();
        reassignment.put(new TopicPartition(topicName, 0), Optional.of(new NewPartitionReassignment(Arrays.asList(0,1))));
        reassignment.put(new TopicPartition(topicName, 1), Optional.of(new NewPartitionReassignment(Arrays.asList(1))));
        reassignment.put(new TopicPartition(topicName, 2), Optional.of(new NewPartitionReassignment(Arrays.asList(1,0))));
        reassignment.put(new TopicPartition(topicName, 3), Optional.of(new NewPartitionReassignment(Arrays.asList(1,0))));


        client.alterPartitionReassignments(reassignment).all().get();
        System.out.println("currently reassigning: " +
                client.listPartitionReassignments().reassignments().get());

        var currentTopic = client.describeTopics(Collections.singleton(topicName));
        var topicDescription = currentTopic.values().get(topicName).get();
        System.out.println("Description of demo topic:" + topicDescription);

    }


    //TODO: Change Election Type
    private static void preferedLeaderElection(String topicName) {
        Set<TopicPartition> electableTopics = new HashSet<>();;
        electableTopics.add(new TopicPartition(topicName, 1));
        try {
            client.electLeaders(ElectionType.PREFERRED, electableTopics).all().get();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof ElectionNotNeededException) {
                System.out.println("All leaders are preferred already");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
