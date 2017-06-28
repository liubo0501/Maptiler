package com.anbot.server.kafkatool.bean;

public class KVData
{
  private byte[] key;
  private byte[] value;

  public byte[] getKey()
  {
    return this.key;
  }
  public void setKey(byte[] key) {
    this.key = key;
  }
  public byte[] getValue() {
    return this.value;
  }
  public void setValue(byte[] value) {
    this.value = value;
  }
}