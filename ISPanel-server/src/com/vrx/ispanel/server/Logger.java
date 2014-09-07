package com.vrx.ispanel.server;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

  private final String  DEFAULT_LOG_FILE = "ISPanel.log";
  private static Logger instance;

  private Logger() {
  }

  public static Logger getInstance() {
    if (instance == null)
      instance = new Logger();
    return instance;
  }

  public void write(String tag, String message) {
    write(DEFAULT_LOG_FILE, tag, message);
  }

  private void write(String file, String tag, String message) {
    Date now = new Date();
    DateFormat df = new SimpleDateFormat("z yyyy.MM.dd HH:mm:ss");
    String currentTime = df.format(now);
    FileWriter fileWriter;
    try {
      fileWriter = new FileWriter(file, true);
      fileWriter.write(currentTime + " | " + tag + ": " + message + "\n");
      fileWriter.flush();
      fileWriter.close();
    } catch (IOException e) {
      System.err.println("Can't write to logfile '" + file + "': "
          + e.getMessage());
    }
  }
}
