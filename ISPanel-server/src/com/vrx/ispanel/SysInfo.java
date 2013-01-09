package com.vrx.ispanel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SysInfo implements Serializable, SysObjectInterface {

 private static final long serialVersionUID = 221L;

 private static final String UNKNOWN = "unknown"; // to put unknown if something failed

 private static final String TAG = "SysInfo";

 private String hostname;
 private String OS;
 private String distro;
 private String cpu;
 private String loadAverage;
 private String uptime;
 private String serverTime;
 private String memUsage;
 private String diskUsage;

 // ------------------------------

 private static String updateDiskUsage() {
  String[] cmd = {
    "/bin/sh",
    "-c",
    "df -h --total | awk '/total/ {print(\"Total:\"$2 \" Used:\" $3 \"(\"$5\")\" \" Free:\" $4)}'" };
  String temp = null;
  Runtime rt = Runtime.getRuntime();
  try {
   Process proc = rt.exec(cmd);
   BufferedReader shellInputStream = new BufferedReader(new InputStreamReader(
     proc.getInputStream()));
   temp = shellInputStream.readLine();
   shellInputStream.close();
   return temp;
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "Error updateing DiskUsage:" + e.getMessage());
  }
  return UNKNOWN;
 }

 private static String updateDistro() {
  String[] cmd = { "/bin/sh", "-c", "lsb_release -ds" };
  String temp = null;
  Runtime rt = Runtime.getRuntime();
  try {
   Process proc = rt.exec(cmd);
   BufferedReader shellInputStream = new BufferedReader(new InputStreamReader(
     proc.getInputStream()));
   temp = shellInputStream.readLine();
   shellInputStream.close();
   return temp;
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "Error updateing Distro:" + e.getMessage());
  }
  return UNKNOWN;
 }

 private static String updateHostname() {
  String temp = null;
  try {
   temp = java.net.InetAddress.getLocalHost().getHostName();
   return temp;
  } catch (UnknownHostException e) {
   ISPanelMain.log.write(TAG, "Error updateing hostname:" + e.getMessage());
  }
  return UNKNOWN;
 }

 private static String updateOS() {
  StringBuilder temp = new StringBuilder();
  temp.append(System.getProperty("os.name") + " ");
  temp.append(System.getProperty("os.version") + " ");
  temp.append(System.getProperty("os.arch"));

  return temp.toString();
 }

 private static String updateCPU() {
  String[] cmd = { "/bin/sh", "-c",
    "awk -F\": \" '/model name/ {printf(\"%s\\n\",$2)}' /proc/cpuinfo" };
  String temp = null;
  Runtime rt = Runtime.getRuntime();
  try {
   Process proc = rt.exec(cmd);
   BufferedReader shellInputStream = new BufferedReader(new InputStreamReader(
     proc.getInputStream()));
   temp = shellInputStream.readLine();
   shellInputStream.close();
   return temp;
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "Error updateing CPU:" + e.getMessage());
  }
  return UNKNOWN;
 }

 private static String updateServerTime() {
  String cmd = "date";
  String temp = null;
  Runtime rt = Runtime.getRuntime();
  try {
   Process proc = rt.exec(cmd);
   BufferedReader shellInputStream = new BufferedReader(new InputStreamReader(
     proc.getInputStream()));
   temp = shellInputStream.readLine();
   shellInputStream.close();
   return temp;
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "Error updateing server time:" + e.getMessage());
  }
  return UNKNOWN;
 }

 private static String updateUptime() {
  String temp = null;
  try {
   temp = new Scanner(new FileInputStream("/proc/uptime")).next();
   temp = temp.substring(0, temp.indexOf("."));
  } catch (FileNotFoundException e) {
   ISPanelMain.log.write(TAG, "Error updateing Uptime:" + e.getMessage());
   return UNKNOWN;
  }

  long secs = Long.parseLong(temp);

  // convert seconds to days, hours, mins to be human readable
  long days = TimeUnit.SECONDS.toDays(secs);
  secs -= TimeUnit.DAYS.toSeconds(days);
  long hours = TimeUnit.SECONDS.toHours(secs);
  secs -= TimeUnit.HOURS.toSeconds(hours);
  long minutes = TimeUnit.SECONDS.toMinutes(secs);
  secs -= TimeUnit.MINUTES.toSeconds(minutes);
  long seconds = secs;

  StringBuilder sb = new StringBuilder(64);

  sb.append(days);
  sb.append(" Days ");
  sb.append(hours);
  sb.append(" Hours ");
  sb.append(minutes);
  sb.append(" Minutes ");
  sb.append(seconds);
  sb.append(" Seconds");

  return (sb.toString());
 }

 private static String updateMem() {
  String[] cmd = { "/bin/sh", "-c",
    "awk '/Mem/ {printf(\"%.2f\\n\",$2/1024)}' /proc/meminfo" };
  String[] temp = new String[2];
  Runtime rt = Runtime.getRuntime();
  try {
   Process proc = rt.exec(cmd);
   BufferedReader shellInputStream = new BufferedReader(new InputStreamReader(
     proc.getInputStream()));
   temp[0] = shellInputStream.readLine();
   temp[1] = shellInputStream.readLine();
   shellInputStream.close();
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "Error updateing memory:" + e.getMessage());
   return UNKNOWN;
  }
  String temp2 = "Total:" + temp[0] + "MB  Free:" + temp[1] + "MB";
  return temp2;
 }

 private static String updateLoadAverage() {
  String[] cmd = { "/bin/sh", "-c",
    "uptime | awk -F\": \" '{printf(\"%s\\n\",$2)}'" };
  String temp = null;
  Runtime rt = Runtime.getRuntime();
  try {
   Process proc = rt.exec(cmd);
   BufferedReader shellInputStream = new BufferedReader(new InputStreamReader(
     proc.getInputStream()));
   temp = shellInputStream.readLine();
   shellInputStream.close();
   return temp;
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "Error updateing load average:" + e.getMessage());
  }
  return UNKNOWN;
 }

 // Public method to update the variables
 public void update() {
  hostname = updateHostname();
  OS = updateOS();
  distro = updateDistro();
  cpu = updateCPU();
  loadAverage = updateLoadAverage();
  memUsage = updateMem();
  diskUsage = updateDiskUsage();

  uptime = updateUptime();
  serverTime = updateServerTime();
 }

 // ------------------------------

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

 public void show() {
  System.out.println("Hostname: " + getHostname());
  System.out.println("OS: " + getOS());
  System.out.println("Distro: " + getDistro());
  System.out.println("CPU: " + getCpu());
  System.out.println("Load average: " + getLoadAverage());
  System.out.println("Memory Usage: " + getMemUsage());
  System.out.println("Disk Usage: " + getDiskUsage());
  System.out.println("Uptime: " + getUptime());
  System.out.println("Time on server: " + getServerTime());
 }
}