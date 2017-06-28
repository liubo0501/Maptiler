package com.anbot.server.kafkatool.common;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class PartitionRoute
  implements Partitioner
{
  public PartitionRoute(VerifiableProperties props)
  {
  }

  public int partition(Object key, int numPartitions)
  {
    return (int)(Math.random() * numPartitions);
  }
}