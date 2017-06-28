package com.anbot.server.kafkatool;

import com.anbot.server.kafkatool.common.Configuration;
import kafka.serializer.StringDecoder;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaClient
{
  private static final Logger LOG = LoggerFactory.getLogger(KafkaClient.class);
  private Configuration cfg;
  private String programID;

  public KafkaClient(String programID)
  {
    this.programID = (programID + "-ANBOTCLIENT");
    this.cfg = Configuration.getInstance();
    try {
      this.cfg.load("kafkaclient.xml");
    } catch (ConfigurationException e) {
      LOG.error(e.getMessage(), e);
    }
    LOG.info("Load configuration has sucessful!");
  }

  public TopicWriter<String> getTopicWriter(String topic) {
    String brokerList = Configuration.getInstance().getString("kafka.brokerList");
    return new TopicWriter(topic, brokerList, "kafka.serializer.StringEncoder");
  }

  public TopicReader<String> getTopicReader(String topic) {
    String zkConnect = Configuration.getInstance().getString("kafka.zookeeper");
    return new TopicReader(zkConnect, topic, this.programID, new StringDecoder(null));
  }
}