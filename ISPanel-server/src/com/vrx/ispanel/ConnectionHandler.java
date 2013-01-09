package com.vrx.ispanel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
 private final static String TAG = "ConnectionHandler";

 private boolean authorized = false;
 String tempUser, tempPass, message;
 Socket socket = null;
 ObjectOutputStream oos = null;
 ObjectInputStream ois = null;

 public ConnectionHandler(Socket s) {
  socket = s;
 }
 
 // called when thread starts
 @Override
 public void run() {
  try {
   oos = new ObjectOutputStream(socket.getOutputStream());
   ois = new ObjectInputStream(socket.getInputStream());
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "run() I/O: " + e.getMessage());
   closeConnection();
   return;
  }

  authorize(); // first start client log-in
  if (authorized) {
   sendData(); // if succeed, send sysobjects and.... 
   processRequest(); // enter in a while loop waiting for commands like refresh
  }
  closeConnection(); // when client disconnect, close connection
 }
 
 // this method read a command from client and process it
 public void processRequest() {
  boolean exit = false;
  while (!exit) {
   String request = readString();
   switch (request) {
   case "exit":
    exit = true;
    break;
   case "refresh":
    sendData();
    break;
   default:
    break;
   }
  }
 }

 // this method close the connection
 public void closeConnection() {
  try {
   oos.close();
   ois.close();
   socket.close();
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "closeConnection() I/O: " + e.getMessage());
  }
 }

 // Write/read methods
 public void writeString(String s) {
  try {
   oos.writeObject(s);
   oos.flush();
   oos.reset();
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "writeString() I/O: " + e.getMessage());
  }
 }

 public void writeSysInfo(SysInfo sysinfo) {
  try {
   oos.writeObject(sysinfo);
   oos.flush();
   oos.reset();
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "writeSysInfo() I/O: " + e.getMessage());
  }
 }

 public void writeSysClients(SysClients sysclients) {
  try {
   oos.writeObject(sysclients);
   oos.flush();
   oos.reset();
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "writeSysClients() I/O: " + e.getMessage());
  }
 }

 public String readString() {
  try {
   return (String) ois.readObject();
  } catch (ClassNotFoundException | IOException e) {
   ISPanelMain.log.write(TAG, "readString() I/O: " + e.getMessage());
  }
  return null;
 }

 // Autentification method
 private void authorize() {
  tempUser = readString();
  tempPass = readString();

  if (Server.username.equals(tempUser) && Server.password.equals(tempPass)) {
   message = "Connected!";
   writeString(message);
   authorized = true;
   ISPanelMain.log.write(TAG, "<< " + tempUser + " >> connected!");
  } else {
   message = "User or password incorrect!";
   writeString(message);
   authorized = false;
   ISPanelMain.log.write(TAG, "Authentification failed for username: "
     + tempUser + " and password: " + tempPass);
  }
 }

 // send SysOjbects to client
 private void sendData() {
  SysInfo sysinfo = ISPanelMain.sysinfo;
  writeSysInfo(sysinfo);
  SysClients sysclients = ISPanelMain.sysclients;
  writeSysClients(sysclients);
 }
}