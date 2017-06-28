package com.anbot.server.kafkatool.common;

import java.nio.charset.Charset;

public class GlobalConstants
{
  public static final String GLOBAL_CHARSET_NAME = "utf-8";
  public static final Charset GLOBAL_CHARSET = Charset.forName("utf-8");
  public static final String KVDATA_ENCODER_CLASS = "com.anbot.server.kafkatool.codec.KVDataEncoder";
  public static final String ANBOTDATA_ENCODER_CLASS = "com.anbot.server.kafkatool.codec.AnbotDataEncoder";
  public static final String STRING_ENCODER_CLASS = "kafka.serializer.StringEncoder";
}