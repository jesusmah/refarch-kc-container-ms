import os, time, sys
sys.path.append(os.path.join(os.path.dirname(os.path.dirname(sys.path[0])),'tools'))
import json
import csv
from confluent_kafka import KafkaError, Producer

#KAFKA_BROKERS=os.environ['KAFKA_BROKERS']

containerProducer = Producer({
    'bootstrap.servers': 'kafka1:9092'
})

def delivery_report(err, msg):
        """ Called once for each message produced to indicate delivery result.
            Triggered by poll() or flush(). """
        if err is not None:
            print('Message delivery failed: {}'.format(err))
        else:
            print('Message delivered to {} [{}]'.format(msg.topic(), msg.partition()))

def publishEvent(data):
    #dataStr = json.dumps(data)
    print data
    containerProducer.produce('ContainerMetrics', data.encode('utf-8'), callback=delivery_report)
    containerProducer.flush()



#print("This is the name of the script: ", sys.argv[1])

#data = json.loads(sys.argv[1])
#ContainerPublisher.publishEvent(data)
# for i in data:
#     print(i)
#     ContainerPublisher.publishEvent(i)
#     print('\n')
#     time.sleep(1)