package com.vrx.ispanel.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class StatusManager extends AbstractManager {

  private static StatusManager instance;
  private static final String  UNKNOWN          = "unknown";
  private String               hostname;
  private String               os;
  private String               distro;
  private String               cpu;
  private String               loadAverage;
  private String               uptime;
  private String               serverTime;
  private String               memUsage;
  private String               diskUsage;

  private StatusManager() {
    updateInterval = 5000;
  }

  public static StatusManager getInstance() {
    if (instance == null)
      instance = new StatusManager();
    return instance;
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

  private void updateDiskUsage() {
    String[] cmd = {
        Const.System.SHELL,
        "-c",
        "df -h --total | awk '/total/ {print(\"Total:\"$2 \" Used:\" $3 \"(\"$5\")\" \" Free:\" $4)}'" };
    try {
      BufferedReader bf = ShellExecutor.getInstance().exec(cmd);
      String result = bf.readLine();
      diskUsage = result.trim();
      bf.close();
    } catch (IOException | InterruptedException e) {
      diskUsage = UNKNOWN;
      Logger.getInstance().write(getClass().getName(), "Error updateing DiskUsage: "
          + e.getMessage());
    }
  }

  private void updateDistro() {
    String[] cmd = { Const.System.SHELL, "-c", "lsb_release -ds" };
    try {
      BufferedReader bf = ShellExecutor.getInstance().exec(cmd);
      String result = bf.readLine();
      distro = result.trim();
      bf.close();
    } catch (IOException | InterruptedException e) {
      distro = UNKNOWN;
      Logger.getInstance().write(getClass().getName(), "Error updateing Distro: "
          + e.getMessage());
    }
  }

  private void updateHostname() {
    try {
      hostname = java.net.InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      hostname = UNKNOWN;
      Logger.getInstance().write(getClass().getName(), "Error updateing hostname:"
          + e.getMessage());
    }

  }

  private void updateOs() {
    StringBuilder temp = new StringBuilder();
    temp.append(System.getProperty("os.name") + " ");
    temp.append(System.getProperty("os.version") + " ");
    temp.append(System.getProperty("os.arch"));
    os = temp.toString();
  }

  private void updateCpu() {
    String[] cmd = { Const.System.SHELL, "-c",
        "cat /proc/cpuinfo | awk -F\": \" '/model name/ {printf(\"%s\\n\",$2)}'" };
    try {
      BufferedReader bf = ShellExecutor.getInstance().exec(cmd);
      String result = bf.readLine();
      cpu = result.trim();
      bf.close();
    } catch (IOException | InterruptedException e) {
      cpu = UNKNOWN;
      Logger.getInstance().write(getClass().getName(),
          "Error updateing CPU: " + e.getMessage());
    }
  }

  private void updateServerTime() {
    String[] cmd = new String[] { Const.System.SHELL, "-c", "date" };
    try {
      BufferedReader bf = ShellExecutor.getInstance().exec(cmd);
      String result = bf.readLine();
      serverTime = result.trim();
      bf.close();
    } catch (IOException | InterruptedException e) {
      serverTime = UNKNOWN;
      Logger.getInstance().write(getClass().getName(),
          "Error updateing Server Time: " + e.getMessage());
    }
  }

  private void updateUptime() {
    String[] cmd = new String[] { Const.System.SHELL, "-c",
        "cat /proc/uptime | awk -F\".\" '{printf(\"%s\\n\",$1)}'" };
    try {
      BufferedReader bf = ShellExecutor.getInstance().exec(cmd);
      long secs = Long.parseLong(bf.readLine());
      // convert seconds to days, hours, mins to be human readable
      long days = TimeUnit.SECONDS.toDays(secs);
      secs -= TimeUnit.DAYS.toSeconds(days);
      long hours = TimeUnit.SECONDS.toHours(secs);
      secs -= TimeUnit.HOURS.toSeconds(hours);
      long minutes = TimeUnit.SECONDS.toMinutes(secs);
      secs -= TimeUnit.MINUTES.toSeconds(minutes);
      StringBuilder sb = new StringBuilder(64);
      sb.append(days);
      sb.append(" Days ");
      sb.append(hours);
      sb.append(" Hours ");
      sb.append(minutes);
      sb.append(" Minutes ");
      sb.append(secs);
      sb.append(" Seconds");
      uptime = sb.toString();
      bf.close();
    } catch (IOException | InterruptedException e) {
      uptime = UNKNOWN;
      Logger.getInstance().write(getClass().getName(), "Error updateing Uptime: "
          + e.getMessage());
    }
  }

  private void updateMemUsage() {
    String[] cmd = { "/bin/sh", "-c",
        "cat /proc/meminfo | awk '/Mem/ {printf(\"%.2f\\n\",$2/1024)}'" };
    try {
      BufferedReader bf = ShellExecutor.getInstance().exec(cmd);
      String total = bf.readLine();
      String free = bf.readLine();
      memUsage = "Total:" + total + "MB  Free:" + free + "MB";
      bf.close();
    } catch (IOException | InterruptedException e) {
      memUsage = UNKNOWN;
      Logger.getInstance().write(getClass().getName(),
          "Error updateing memory usage: " + e.getMessage());
    }
  }

  private void updateLoadAverage() {
    String[] cmd = { "/bin/sh", "-c",
        "uptime | awk -F\": \" '{printf(\"%s\\n\",$2)}'" };
    try {
      BufferedReader bf = ShellExecutor.getInstance().exec(cmd);
      String result = bf.readLine();
      loadAverage = result;
      bf.close();
    } catch (IOException | InterruptedException e) {
      loadAverage = UNKNOWN;
      Logger.getInstance().write(getClass().getName(),
          "Error updateing load average: " + e.getMessage());
    }
  }

  public void showStatus() {
    System.out.println(toString());
  }

  @Override
  public void update() {
    if (!isUpdating) {
      new Thread(new Runnable() {

        @Override
        public void run() {
          isUpdating = true;
          updateHostname();
          updateOs();
          updateDistro();
          updateCpu();
          updateLoadAverage();
          updateMemUsage();
          updateDiskUsage();
          updateUptime();
          updateServerTime();
          lastUpdate = System.currentTimeMillis();
          isUpdating = false;
        }
      }).start();
    }

  }

  @Override
  public String toString() {
    String newLine = System.getProperty("line.separator");
    StringBuilder sb = new StringBuilder();
    sb.append("HostName: " + hostname + newLine);
    sb.append("OS: " + os + newLine);
    sb.append("Distro: " + distro + newLine);
    sb.append("CPU: " + cpu + newLine);
    sb.append("Load average: " + loadAverage + newLine);
    sb.append("Memory Usage: " + memUsage + newLine);
    sb.append("Disk Usage: " + diskUsage + newLine);
    sb.append("Uptime: " + uptime + newLine);
    sb.append("Time on server: " + serverTime + newLine);
    return sb.toString();
  }
}