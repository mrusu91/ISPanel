package com.vrx.ispanel;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

 protected static String defaultLogFile = "ISPanel.log";

 public void write(String TAG, String s) {
  write(defaultLogFile, TAG, s);
 }

 public void write(String f, String TAG, String s) {
  Date now = new Date(); // this contain the current time when it is created
  DateFormat df = new SimpleDateFormat("z yyyy.MM.dd HH:mm:ss"); // format the date and time
  String currentTime = df.format(now);

  FileWriter aWriter;
  try {
   aWriter = new FileWriter(f, true);
   aWriter.write(currentTime + " | " + TAG + ": " + s + "\n");
   aWriter.flush();
   aWriter.close();
  } catch (IOException e) {
   System.out.println("Can't write to logfile '" + f + "': " + e.getMessage());
  }
 }
}
