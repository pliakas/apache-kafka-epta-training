# Managing Apache Kafka Programmatically

Apache Kafka added the AdminClient in version 0.11 to provide a programmatic API  for administrative functionality 
that was previously done in the command line: listing, creating, and deleting topics; describing the cluster;  
managing ACLs; and modifying configuration.

## AdminClient Overview
As you start using Kafka AdminClient, it helps to be aware of its core design principles. When you understand how 
the AdminClient was designed and how it should be used, the specifics of each method will be much more intuitive.

### Asynchronous and Eventually Consistent API
The Kafka’s AdminClient is that it is asynchronous. Each method returns immediately after delivering a request to 
the cluster controller, and each method returns one or more `Future` objects. Future objects are the result of 
asynchronous operations, and they have methods for checking the status of the asynchronous operation, canceling it, 
waiting for it to complete,  and executing functions after its completion.  

`Kafka’s AdminClient` wraps the Future objects into Result objects, which provide methods to wait for the operation 
to complete and helper methods for common follow-up operations. 

For example, Kafka `AdminClient.createTopics` returns the` CreateTopicsResult object`, which lets you wait until all 
topics are created, check each topic status individually, and retrieve the configuration of a specific topic after it was created. 

Because Kafka’s propagation of metadata from the controller to the brokers is asynchronous, the Futures that 
AdminClient APIs return are considered complete when the controller state has been fully updated. At that point, not 
every broker might be aware of the new state, so a listTopics request may end up handled by a broker that  
is not up-to-date and will not contain a topic that was very recently created. This
property is also called eventual consistency: eventually every broker will know about
every topic, but we can’t guarantee exactly when this will happen.

## Learning Objectives
In this lab you will learn how to perform Admin Operations using java (programmatically). 

## Lab Instructions
