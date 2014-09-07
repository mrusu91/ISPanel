package com.vrx.ispanel.android;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.vrx.ispanel.common.Clients;
import com.vrx.ispanel.common.Status;

import android.util.Log;

public class RemoteAccessClient {

  private String             address;
  private int                port;
  private Socket             socket;
  private ObjectOutputStream oos;
  private ObjectInputStream  ois;
  private boolean            isConnected;

  public RemoteAccessClient(String address, int port) {
    this.address = address;
    this.port = port;
  }

  public boolean connect() {
    try {
      socket = new Socket(address, port);
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
      return true;
    } catch (UnknownHostException e) {
      Log.w(getClass().getName(), "Host couldn't be reached: " + e.getMessage());
    } catch (IOException e) {
      Log.w(getClass().getName(),
          "I/O Error while trying to connect to server: " + e.getMessage());
    }
    return false;
  }

  public boolean auth(String username, String password) {
    try {
      writeString("auth");
      writeString(username);
      writeString(password);
      String result = readString();
      if (result.equals("OK")) {
        isConnected = true;
        return true;
      }
    } catch (IOException | ClassNotFoundException e) {
      Log.d(getClass().getName(), "Error during auth: " + e.getMessage());
    }
    return false;
  }

  public void disconnect() {
    try {
      writeString("exit");
      socket.close();
    } catch (IOException e) {
      Log.w(getClass().getName(),
          "I/O Error while trying to disconnect: " + e.getMessage());
    }
    isConnected = false;
  }

  public Status refreshStatus() {
    try {
      writeString("refresh_status");
      return readStatus();
    } catch (IOException e){
      Log.d(getClass().getName(), "I/O Error while refreshing Status: "+ e.getMessage());
    }catch ( ClassNotFoundException e) {
      Log.d(getClass().getName(), "CNF Error while refreshing Status: "+ e.getMessage());
    }
    return null;
  }

  public Clients refreshClients() {
    try {
      writeString("refresh_clients");
      return readClients();
    } catch (IOException | ClassNotFoundException e) {
      Log.d(getClass().getName(),
          "Error while refreshing Clients: " + e.getMessage());
    }
    return null;
  }

  public boolean isConnected() {
    return socket.isConnected() && isConnected;
  }

  private void writeString(String s) throws IOException {
    oos.writeObject(s);
    oos.flush();
    oos.reset();
  }

  private String readString() throws OptionalDataException,
      ClassNotFoundException, IOException {
    return (String) ois.readObject();
  }

  private Status readStatus() throws OptionalDataException,
      ClassNotFoundException, IOException {
    return (Status) ois.readObject();
  }

  private Clients readClients() throws OptionalDataException,
      ClassNotFoundException, IOException {
    return (Clients) ois.readObject();
  }
}
