// TO DO: change the app to use a config file to be easy to configure

package com.vrx.ispanel;

import java.util.Scanner;

public class ISPanelMain {

 private final static String TAG = "ISPanelMain";
 
// Global objects
 static SysInfo sysinfo;
 static SysClients sysclients;
 static Logger log;

 static SysObjectsUpdater sysobjupdater;

 public static void main(String[] args) {
  sysinfo = new SysInfo();
  sysclients = new SysClients();
  log = new Logger();

  boolean exit = false;

  Menu menu = new Menu();
  Scanner in = new Scanner(System.in);

  // Make a separated thread to update Sysobjects
  sysobjupdater = new SysObjectsUpdater();
  Thread updateThread = new Thread(sysobjupdater);
  updateThread.setDaemon(true);
  updateThread.start();

  // Make a separated thread to run the server in background
  ServerHandler serverHandler = null;
  Thread serverThread = null;

  // Show the menu
  while (!exit) {
   menu.showMenu();
   int picker = in.nextInt();

   switch (picker) {
   case 1:// start the server
    if (serverThread != null) {
     System.out.println("Server is already started!");
     log.write(TAG, "Server is already started!");
    } else {
     serverHandler = new ServerHandler();
     serverThread = new Thread(serverHandler);
     serverThread.setDaemon(true);
     serverThread.start();
    }
    break;
   case 2: // stop the server
    if (serverThread != null) {
     serverHandler.closeServer();
     serverHandler = null;
     serverThread = null;
    } else {
     System.out.println("Server is not started!");
     log.write(TAG, "Server is not started!");
    }
    break;
   case 3:// show system information
    sysinfo.show();
    break;

   case 4:// show clients list if any
    sysclients.show();
    break;

   case 0: // exit the program
    exit = true;
    break;

   default:
    menu.showMenu();
    break;
   }
  }
  in.close();
 }

}
