package com.vrx.ispanel.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class RemoteAccessServer {

  private static RemoteAccessServer    instance;
  private ArrayList<ConnectionHandler> connections;
  ServerSocket                         serversocket;
  private boolean                      isRunning;

  private RemoteAccessServer() {
    connections = new ArrayList<ConnectionHandler>();
  }

  public static RemoteAccessServer getInstance() {
    if (instance == null)
      instance = new RemoteAccessServer();
    return instance;
  }

  public boolean isRunning() {
    return isRunning;
  }

  public void start() {
    if (!isRunning) {
      new Worker().start();
    }
  }

  public void stop() {
    if (isRunning) {
      try {
        serversocket.close();
        for (ConnectionHandler conn : connections)
          conn.close();
        Logger.getInstance().write(getClass().getName(), "stopped!");
      } catch (IOException e) {
        Logger.getInstance().write(getClass().getName(),
            "Error stopping: " + e.getMessage());
      }
    }
  }

  private class Worker extends Thread {
    @Override
    public void run() {
      isRunning = true;
      connections.clear();
      try {
        serversocket = new ServerSocket(Const.RemoteAccess.PORT);
        Logger.getInstance().write(getName(),
            "Server started on port " + serversocket.getLocalPort());
        while (!serversocket.isClosed()) {
          connections.add(new ConnectionHandler(serversocket.accept()));
        }
      } catch (IOException e) {
        Logger.getInstance()
            .write(getName(), "Socket error: " + e.getMessage());
      }
      isRunning = false;
    }
  }
}
