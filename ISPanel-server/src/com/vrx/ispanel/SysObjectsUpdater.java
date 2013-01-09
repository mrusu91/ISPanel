package com.vrx.ispanel;

public class SysObjectsUpdater implements Runnable {

 private static final String TAG = "SysObjectsUpdater";
 public SysInfo sysinfo;
 public SysClients sysclients;
 private boolean exit;

 @Override
 public void run() {
  exit = false;
  sysinfo = ISPanelMain.sysinfo;
  sysclients = ISPanelMain.sysclients;

  while (!exit) {
   sysclients.update();
   sysinfo.update();
   try {
    Thread.currentThread();
    Thread.sleep(60000); // Update every minute
   } catch (InterruptedException e) {
    ISPanelMain.log.write(TAG, "run() InterruptedException: " + e.getMessage());
   }
  }

 }

 public void stopObjectsUpdateing() {
  exit = true;
 }
}
