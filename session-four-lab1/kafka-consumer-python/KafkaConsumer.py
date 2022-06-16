#!/usr/bin/python3

from confluent_kafka import Consumer
import time, sys

TOPIC = 'session-four-lab-topic-python'
BOOTSTRAP_SERVERS = 'localhost:9092,localhost:9093,localhost:9094'
GROUP_ID = 'KafkaExampleConsumer'

def createConsumer():
    # Set Consumer configuration.
	conf = {'bootstrap.servers':BOOTSTRAP_SERVERS,
		'group.id':GROUP_ID}

	# Create the Consumer using configuration.
	consumer = Consumer(**conf)

	# Subscribe to the topic.
	consumer.subscribe([TOPIC])
	return consumer


# TODO: Edit here to implement the consumer
# TODO: Try to print key(), value(), partition() and Offset()
def runConsumer(consumer):
	try:
	    
	except KeyboardInterrupt:
	    sys.stderr.write('%% Aborted by user\n')

	finally:
	    # Close down consumer to commit final offsets.
	    consumer.close()

if __name__ == '__main__':
    consumer = createConsumer()
    runConsumer(consumer)
