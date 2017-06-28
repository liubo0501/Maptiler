package com.anbot.server.kafkatool.common;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

public class KeyEncoder
  implements Encoder<Byte>
{
  private byte[] ary = new byte[1];

  public KeyEncoder(VerifiableProperties props)
  {
  }

  public byte[] toBytes(Byte arg0) {
    return this.ary;
  }
}