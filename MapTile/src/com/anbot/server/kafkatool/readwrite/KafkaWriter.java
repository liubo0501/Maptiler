package com.anbot.server.kafkatool.readwrite;

import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaWriter<T>
{
  private final Producer<Byte, T> producer;
  private final Byte key = Byte.valueOf((byte) 0);

  public KafkaWriter(String brokerList, String serializer) {
    Properties props = new Properties();
    props.put("key.serializer.class", "com.anbot.server.kafkatool.common.KeyEncoder");
    props.put("partitioner.class", "com.anbot.server.kafkatool.common.PartitionRoute");
    props.put("serializer.class", serializer);
    props.put("metadata.broker.list", brokerList);
    props.put("producer.type", "async");
    props.put("queue.buffering.max.ms", "200");
    props.put("batch.num.messages", "10");
    this.producer = new Producer(new ProducerConfig(props));
  }

  public KafkaWriter(Properties props) {
    this.producer = new Producer(new ProducerConfig(props));
  }

  public void write(String topic, T message) throws Exception {
    this.producer.send(new KeyedMessage(topic, this.key, message));
  }

  public void close() {
    this.producer.close();
  }
}