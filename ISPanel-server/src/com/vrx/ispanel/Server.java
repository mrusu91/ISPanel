package com.vrx.ispanel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
 private final static String TAG = "Server";

 public static String username = "demo";
 public static String password = "demo";
 private int port = 7777;
 ArrayList<ConnectionHandler> NrConnections; // how many connection are
 ServerSocket serversocket = null;
 Socket socket = null;

 // to start the server method
 public void start() {
  try {
   serversocket = new ServerSocket(port);
   NrConnections = new ArrayList<ConnectionHandler>();
   ISPanelMain.log.write(TAG, "Server started on port: " + port);
   System.out.println("Server started on port: " + port);
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "Could not listen on port: " + port);
   System.out.println("Could not listen on port: " + port);
   return;
  }

  // if serversocket is successfully started, listen for clients. When a client is connected start a new thread and handle it
  while (serversocket != null) {
   try {
    socket = serversocket.accept();
    NrConnections.add(new ConnectionHandler(socket));
    Thread t = new Thread(NrConnections.get(NrConnections.size() - 1));
    t.start();
   } catch (IOException e) {
    ISPanelMain.log.write(TAG, "Not accepting clients anymore!");
    System.out.println("Not accepting clients anymore!");
   }
  }
 }

 // Close socket connection method
 public void closeConnection() {
  // Close all client connections and their thread
  for (int i = 0; i < NrConnections.size(); i++)
   NrConnections.get(i).closeConnection();

  // close initial socket
  try {
   if (socket != null) {
    socket.close();
    socket = null;
   }
  } catch (IOException e) {
   ISPanelMain.log.write(TAG, "closeConnection I/O:" + e.getMessage());
  }
 }

 // Stop server method

 public void stop() {
  if (serversocket == null)
   System.out.println("Server is not started!!!");
  else {
   try {
    closeConnection();
    serversocket.close();
    serversocket = null;
    ISPanelMain.log.write(TAG, "Server stopped!");
   } catch (IOException e) {
    ISPanelMain.log.write(TAG, "stop() I/O: " + e.getMessage());
   }
  }
 }
}
