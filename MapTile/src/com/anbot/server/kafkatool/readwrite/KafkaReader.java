package com.anbot.server.kafkatool.readwrite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.Decoder;

public class KafkaReader<T>
{
  private String topic;
  private Decoder<T> decoder;
  private Properties consumerProps;
  private ConsumerConnector kafkaConsumer;
  private ConsumerIterator<byte[], T> it;

  public KafkaReader(Properties props, String topic, Decoder<T> decoder)
  {
    this.topic = topic;
    this.consumerProps = props;
    this.decoder = decoder;
    init();
  }

  public KafkaReader(String zkConnect, String topic, String groupID, Decoder<T> decoder) {
    this.topic = topic;
    Properties props = new Properties();
    props.put("group.id", groupID);
    props.put("zookeeper.connect", zkConnect);
    props.put("auto.offset.reset", "smallest");

    this.consumerProps = props;
    this.decoder = decoder;
    init();
  }

  private void init() {
    ConsumerConfig consumerConfig = new ConsumerConfig(this.consumerProps);
    this.kafkaConsumer = Consumer.createJavaConsumerConnector(consumerConfig);

    Map topicCountMap = new HashMap();
    topicCountMap.put(this.topic, Integer.valueOf(1));

    Map consumerMap = this.kafkaConsumer
      .createMessageStreams(topicCountMap, null, this.decoder);
    KafkaStream stream = (KafkaStream)((List)consumerMap.get(this.topic)).get(0);

    this.it = stream.iterator();
  }

  public T read() {
    Object message = null;
    if (this.it.hasNext()) {
      MessageAndMetadata messageWithMetadata = this.it.next();
      message = messageWithMetadata.message();
    }
    return (T) message;
  }

  public void close() {
    this.kafkaConsumer.shutdown();
  }
}