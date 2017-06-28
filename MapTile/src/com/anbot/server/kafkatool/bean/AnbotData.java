package com.anbot.server.kafkatool.bean;

public class AnbotData
{
  String anbotID = "";
  String json = "";

  public String getAnbotID() {
    return this.anbotID;
  }
  public void setAnbotID(String anbotID) {
    this.anbotID = anbotID;
  }
  public String getJson() {
    return this.json;
  }
  public void setJson(String json) {
    this.json = json;
  }

  public String toString()
  {
    return "AnbotData [anbotID=" + this.anbotID + ", json=" + this.json + "]";
  }
}