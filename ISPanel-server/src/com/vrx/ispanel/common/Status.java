package com.vrx.ispanel.common;

import java.io.Serializable;

public class Status implements Serializable {
  private static final long serialVersionUID = 3730209298392638091L;

  private String            hostname;
  private String            os;
  private String            distro;
  private String            cpu;
  private String            loadAverage;
  private String            uptime;
  private String            serverTime;
  private String            memUsage;
  private String            diskUsage;

  public Status(String hostname, String os, String distro, String cpu,
      String loadAverage, String uptime, String serverTIme, String memUsage,
      String diskUsage) {

    this.hostname = hostname;
    this.os = os;
    this.distro = distro;
    this.cpu = cpu;
    this.loadAverage = loadAverage;
    this.uptime = uptime;
    this.serverTime = serverTIme;
    this.memUsage = memUsage;
    this.diskUsage = diskUsage;
  }

  public String getHostname() {
    return hostname;
  }

  public String getOS() {
    return os;
  }

  public String getUptime() {
    return uptime;
  }

  public String getDistro() {
    return distro;
  }

  public String getCpu() {
    return cpu;
  }

  public String getServerTime() {
    return serverTime;
  }

  public String getMemUsage() {
    return memUsage;
  }

  public String getDiskUsage() {
    return diskUsage;
  }

  public String getLoadAverage() {
    return loadAverage;
  }

}
