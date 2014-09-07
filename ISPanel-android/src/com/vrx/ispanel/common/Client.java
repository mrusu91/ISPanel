package com.vrx.ispanel.common;

import java.io.Serializable;

public class Client implements Serializable {
  private static final long serialVersionUID = 373234209292638091L;


  private String  ip, mac, name;
  private boolean isOnline;

  public Client(String ip, String mac, String name) {
    this.ip = ip;
    this.mac = mac;
    this.name = name;
    isOnline = false;
  }

  public String getIp() {
    return ip;
  }

  public String getMac() {
    return mac;
  }

  public String getName() {
    return name;
  }

  public boolean isOnline() {
    return isOnline;
  }

  public void setOnline(boolean isOnline) {
    this.isOnline = isOnline;
  }

  @Override
  public String toString() {
    String status = (isOnline ? "[*]" : "[ ]");
    return status + "\t" + ip + "\t" + mac + "\t" + name;
  }
}
