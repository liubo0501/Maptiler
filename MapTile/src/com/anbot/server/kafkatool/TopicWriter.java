package com.anbot.server.kafkatool;

import com.anbot.server.kafkatool.readwrite.KafkaWriter;

public class TopicWriter<T> extends KafkaWriter<T>
{
  private String topic;

  public TopicWriter(String topic, String brokerList, String serializer)
  {
    super(brokerList, serializer);
    this.topic = topic;
  }

  public void Write(T message) throws Exception {
    super.write(this.topic, message);
  }

  public void close() {
    super.close();
  }
}