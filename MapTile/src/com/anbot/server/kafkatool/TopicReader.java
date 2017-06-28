package com.anbot.server.kafkatool;

import com.anbot.server.kafkatool.readwrite.KafkaReader;
import kafka.serializer.Decoder;

public class TopicReader<T> extends KafkaReader<T>
{
  public TopicReader(String zkConnect, String topic, String groupID, Decoder<T> decoder)
  {
    super(zkConnect, topic, groupID, decoder);
  }

  public T read() {
    return super.read();
  }

  public void close() {
    super.close();
  }
}