///   What is in this class can be accessed globally
///

package com.vrx.ispanel;

import java.io.IOException;
import java.io.OptionalDataException;

import android.app.Application;

public class ISPanel extends Application {
 Client client;

 SysInfo sysinfo;
 SysClients sysclients;
   
 public void getSysObjects() throws OptionalDataException, ClassNotFoundException, IOException {
  sysinfo = client.readSysInfo();
  sysclients = client.readSysClients();
 }
}
