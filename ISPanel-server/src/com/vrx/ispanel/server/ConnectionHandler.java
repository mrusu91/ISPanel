package com.vrx.ispanel.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.vrx.ispanel.common.Client;
import com.vrx.ispanel.common.Clients;
import com.vrx.ispanel.common.Status;

public class ConnectionHandler {

  private boolean            authorized, close;
  private Socket             socket;
  private ObjectOutputStream oos;
  private ObjectInputStream  ois;

  public ConnectionHandler(Socket socket) {
    this.socket = socket;
    new Worker().start();
  }

  private void processRequest() {
    while (!close) {
      String request = readString();
      if (request == null)
        return;
      switch (request) {
      case "auth":
        auth();
        break;
      case "exit":
        close();
        break;
      case "refresh_status":
        sendStatus();
        break;
      case "refresh_clients":
        sendClients();
        break;
      default:
        break;
      }
    }
  }

  private void writeString(String string) {
    try {
      oos.writeObject(string);
      oos.flush();
      oos.reset();
    } catch (IOException e) {
      Logger.getInstance().write(getClass().getName(),
          "Error writing String: " + e.getMessage());
    }
  }

  private void writeStatus(Status status) {
    try {
      oos.writeObject(status);
      oos.flush();
      oos.reset();
    } catch (IOException e) {
      Logger.getInstance().write(getClass().getName(),
          "Error writing status: " + e.getMessage());
    }
  }

  private void writeClients(Clients clients) {
    try {
      oos.writeObject(clients);
      oos.flush();
      oos.reset();
    } catch (IOException e) {
      Logger.getInstance().write(getClass().getName(),
          "Error writing clients: " + e.getMessage());
    }
  }

  private String readString() {
    try {
      return (String) ois.readObject();
    } catch (ClassNotFoundException | IOException e) {
      Logger.getInstance().write(getClass().getName(),
          "Error reading string: " + e.getMessage());
    }
    return null;
  }

  private void auth() {
    String tempUser = readString();
    String tempPass = readString();
    String message;
    if (Const.RemoteAccess.USERNAME.equals(tempUser)
        && Const.RemoteAccess.PASSWORD.equals(tempPass)) {
      message = "OK";
      authorized = true;
      Logger.getInstance().write(getClass().getName(),
          "<< " + tempUser + " >> connected!");
    } else {
      message = "ERROR";
      authorized = false;
      Logger.getInstance().write(
          getClass().getName(),
          "Auth failed for username: " + tempUser + " and password: "
              + tempPass);
    }
    writeString(message);
  }

  private void sendStatus() {
    if (authorized) {
      StatusManager sm = StatusManager.getInstance();
      Status status = new Status(sm.getHostname(), sm.getOS(), sm.getDistro(),
          sm.getCpu(), sm.getLoadAverage(), sm.getUptime(), sm.getServerTime(),
          sm.getMemUsage(), sm.getDiskUsage());
      writeStatus(status);
    }
  }

  private void sendClients() {
    if (authorized) {
      ClientManager cm = ClientManager.getInstance();
      Clients clients = new Clients();
      for (Client client : cm.clients.values())
        clients.add(client);
      writeClients(clients);
    }
  }

  public void close() {
    if (!close) {
      close = true;
      try {
        socket.close();
      } catch (IOException e) {
        Logger.getInstance().write(getClass().getName(),
            "Error closing socket: " + e.getMessage());
      }
    }
  }

  private class Worker extends Thread {

    @Override
    public void run() {
      try {
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
      } catch (IOException e) {
        Logger.getInstance().write(getName(),
            "Error getting streams: " + e.getMessage());
        return;
      }
      processRequest();
      close();
    }
  }
}