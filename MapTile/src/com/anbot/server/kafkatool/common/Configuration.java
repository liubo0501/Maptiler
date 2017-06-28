package com.anbot.server.kafkatool.common;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration
{
  private static Logger logger = LoggerFactory.getLogger(Configuration.class);
  private static Configuration conf = new Configuration();

  private Properties properties = new Properties();

  public static Configuration getInstance()
  {
    return conf;
  }

  public void load(String configFileName)
    throws ConfigurationException
  {
    XMLConfiguration config = new XMLConfiguration();

    config.setDelimiterParsingDisabled(true);

    URL configFileURL = Thread.currentThread().getContextClassLoader()
      .getResource(configFileName);
    config.load(configFileURL);
    List<HierarchicalConfiguration> hConfs = config
      .configurationsAt("property");

    if (hConfs == null) {
      throw new ConfigurationException("Cannot read config.");
    }

    for (HierarchicalConfiguration sub : hConfs) {
      String key = sub.getString("name");
      String value = sub.getString("value");

      if ((StringUtils.isBlank(key)) || (StringUtils.isBlank(value))) {
        continue;
      }
      this.properties.setProperty(key.trim(), value.trim());
    }
  }

  public String get(String key)
  {
    if (this.properties == null) {
      logger.error("Please load configuration file first.");
      return null;
    }

    return this.properties.getProperty(key);
  }

  public String get(String key, String defaultValue)
  {
    String value = get(key);
    if (value == null) {
      return defaultValue;
    }

    return value;
  }

  public String getString(String key)
  {
    if (this.properties == null) {
      logger.error("Please load configuration file first.");
      return null;
    }
    return this.properties.getProperty(key);
  }

  public String getString(String key, String defaultValue)
  {
    String value = getString(key);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  public int getInt(String key, int defaultValue)
  {
    if ((this.properties == null) || (this.properties.getProperty(key) == null)) {
      return defaultValue;
    }
    return Integer.parseInt(this.properties.getProperty(key));
  }

  public long getLong(String key, long defaultValue)
  {
    if ((this.properties == null) || (this.properties.getProperty(key) == null)) {
      return defaultValue;
    }
    return Long.parseLong(this.properties.getProperty(key));
  }

  public static Map<String, String> load2Map(String configFileName)
    throws ConfigurationException
  {
    Map configMap = new HashMap();

    URL configFileURL = Thread.currentThread().getContextClassLoader()
      .getResource(configFileName);
    XMLConfiguration config = new XMLConfiguration();

    config.setDelimiterParsingDisabled(true);

    config.load(configFileURL);

    List<HierarchicalConfiguration> hConfs = config
      .configurationsAt("property");

    if (hConfs == null) {
      throw new ConfigurationException("Cannot read config.");
    }

    for (HierarchicalConfiguration sub : hConfs) {
      String key = sub.getString("name");
      String value = sub.getString("value");

      if ((StringUtils.isBlank(key)) || (StringUtils.isBlank(value))) {
        continue;
      }
      configMap.put(key.trim(), value.trim());
    }

    return configMap;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry en : this.properties.entrySet()) {
      sb.append(en.getKey());
      sb.append("->");
      sb.append(en.getValue());
      sb.append(", ");
    }
    sb.delete(sb.length() - 2, sb.length());
    return sb.toString();
  }
}