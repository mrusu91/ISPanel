package com.vrx.ispanel;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

public class SysClients implements Serializable, SysObjectInterface {

 private static final String TAG = "SysClients";

 private static final long serialVersionUID = 3730209298392638091L;

 ArrayList<String> allClients_IP; // will contain the IP of clients
 ArrayList<String> allClients_MAC;// will contain the MAC of clients
 ArrayList<String> allClients_NAME; // will contain the Name of clients
 ArrayList<String> onlineClients_IP; // contain IP of online clients after a scan

 private boolean noOnlineClients;
 private boolean noClients;

 FileInputStream fstream;
 DataInputStream in;
 BufferedReader br;

 public SysClients() {
  // initializing arrayslists
  allClients_IP = new ArrayList<String>();
  allClients_MAC = new ArrayList<String>();
  allClients_NAME = new ArrayList<String>();
  onlineClients_IP = new ArrayList<String>();
 }

 private void updateAllClients() {
  String line;
  noClients = false;

  // clear the arrays
  allClients_IP.clear();
  allClients_MAC.clear();
  allClients_NAME.clear();

  // clients.all is a file in the project root directory
  // containing all clients, one per line
  // the format net to be like that:
  // 123.123.123.123    FF:FF:FF:FF:FF:FF   Terry A
  // note that beetween IP, MAC and name, must be a TAB space, \t character in order to be parsed corectly
  
  try {
   fstream = new FileInputStream("clients.all");
   in = new DataInputStream(fstream);
   br = new BufferedReader(new InputStreamReader(in));
   while ((line = br.readLine()) != null) {
    allClients_IP.add(line.substring(0, line.indexOf("\t", 0)));
    allClients_MAC.add(line.substring(line.indexOf("\t", 0) + 1,
      line.indexOf("\t", 0) + 1 + 17));
    allClients_NAME.add(line.substring(line.lastIndexOf("\t") + 1));
   }

   br.close();
   br = null;
   in.close();
   in = null;
   fstream.close();
   fstream = null;
  } catch (FileNotFoundException e) {
   ISPanelMain.log.write(TAG, "clients.all: File not found!");
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "clients.all: can't read file" + e.getMessage());
  }
  
  // if no clients are in file
  if (allClients_IP.size() == 0)
   noClients = true;
 }

 // Update online clients method
 public void updateOnlineClients() {
  onlineClients_IP.clear(); // clear the list
  noOnlineClients = false;
  String line;

  // the command to be executed on server in order to see who is online
  String[] cmd = {
    "/bin/sh",
    "-c",
    "arp-scan --interface=eth1 --ignoredups -f=clients.all | awk '/192./ {print($1)}'" };
  Runtime rt = Runtime.getRuntime();
  try {
   Process proc = rt.exec(cmd);
   BufferedReader shellInputStream = new BufferedReader(new InputStreamReader(
     proc.getInputStream()));
   while ((line = shellInputStream.readLine()) != null) {
    onlineClients_IP.add(line);
   }
   shellInputStream.close();
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "updateOnlineClients() I/O: " + e.getMessage());
  }
  // if no clients are connected
  if (onlineClients_IP.size() == 0)
   noOnlineClients = true;
 }

 
 public void update() {
  updateAllClients();
  updateOnlineClients();

 }

 // GETTERS
 public ArrayList<String> getAllClients_IP() {
  return allClients_IP;
 }

 public ArrayList<String> getAllClients_MAC() {
  return allClients_MAC;
 }

 public ArrayList<String> getAllClients_NAME() {
  return allClients_NAME;
 }

 public ArrayList<String> getOnlineClients() {
  return onlineClients_IP;
 }

 // show clients online and offline to console
 public void show() {

  if (!noClients) {
   for (int i = 0; i < allClients_IP.size(); i++) {
    if (!noOnlineClients) {
     for (String tempOnline : onlineClients_IP) {
      if (tempOnline.contains(allClients_IP.get(i))) {
       System.out.println("(*) " + "Name: " + allClients_NAME.get(i) + "\tIP: "
         + allClients_IP.get(i) + "\tMAC: " + allClients_MAC.get(i));
      } else
       System.out.println("Name: " + allClients_NAME.get(i) + "\tIP: "
         + allClients_IP.get(i) + "\tMAC: " + allClients_MAC.get(i));
     }
    } else
     System.out.println("Name: " + allClients_NAME.get(i) + "\tIP: "
       + allClients_IP.get(i) + "\tMAC: " + allClients_MAC.get(i));
   }
  } else {
   System.out.println("No clients registered!");
  }
  System.out.println("\nClients marked with (*) are online");
 }
}
