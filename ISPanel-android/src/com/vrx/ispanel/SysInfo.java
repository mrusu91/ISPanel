package com.vrx.ispanel;

import java.io.Serializable;

public class SysInfo implements Serializable {

 private static final long serialVersionUID = 221L;  // this need to be the same as in server part

 private String hostname;
 private String OS;
 private String distro;
 private String cpu;
 private String loadAverage;
 private String uptime;
 private String serverTime;
 private String memUsage;
 private String diskUsage;

 // GETTERS
 public String getHostname() {
  return hostname;
 }

 public String getOS() {
  return OS;
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

 // ------------------------------
}
