#!/usr/bin/python3

from confluent_kafka import Producer
import time, sys

TOPIC = 'session-three-lab-two-topic'
BOOTSTRAP_SERVERS = 'localhost:9092,localhost:9093,localhost:9094'
CLIENT_ID = 'KafkaProducer'
DEFAULT_MSG_COUNT = 10


# TODO: Set Producer configuration.
def createProducer():

	# TODO: Set Producer configuration.
	conf = {}

	# Create Producer.
	return Producer(**conf)

# Callback function to print delivered record.
def onDelivered(err, msg):
	if err:
		sys.stderr.write('%% Message failed delivery: %s\n' % err)
	else:
		elapsedTime = int(time.time()) - (msg.timestamp()[1] / 1000)
		sys.stderr.write('%% sent record(key=%s value=%s) meta(partition=%d, offset=%d) time=%d\n'
						 %(msg.key(), msg.value(), msg.partition(), msg.offset(), elapsedTime))

# TODO: Implement the producer to send messages to topic
def runProducer(producer, sendMessageCount):
	timeNow = int(time.time())

	try:
		for index in range(timeNow, timeNow + sendMessageCount):

                        # TODO: Implement producer using 'produce.produce' method.

	except BufferError as e:
            sys.stderr.write('%% Local producer queue is full (%d messages awaiting delivery): try again\n' %len(producer))
	finally:
		producer.poll(0)

	# Wait until all messages have been delivered
	sys.stderr.write('%% Waiting for %d deliveries\n' % len(producer))
	producer.flush(30)

if __name__ == '__main__':
	producer = createProducer()
	msgCount = DEFAULT_MSG_COUNT

	if len(sys.argv) == 2:
		msgCount = int(sys.argv[1])

	runProducer(producer, msgCount)
