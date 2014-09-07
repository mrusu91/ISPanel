package com.vrx.ispanel.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.vrx.ispanel.common.Client;

public class ClientManager extends AbstractManager {
  private static ClientManager instance;

  HashMap<String, Client>      clients;

  private ClientManager() {
    clients = new HashMap<String, Client>();
    updateInterval = 15000;
    loadClients();
  }

  public static ClientManager getInstance() {
    if (instance == null)
      instance = new ClientManager();
    return instance;
  }

  /**
   * <pre>
   * clients.all is a file in the project root directory
   * containing all clients, one per line
   * the format net to be like that:
   * 123.123.123.123	FF:FF:FF:FF:FF:FF  Terry A
   * IPv4             MAC	               Name
   * note that between IP, MAC and name, must be only a TAB (\t) space,
   * character in order to be parsed correctly
   * </pre>
   */
  private void loadClients() {
    clients.clear();
    try {
      FileInputStream fstream = new FileInputStream("clients.all");
      BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
      String line;
      while ((line = br.readLine()) != null) {
        String[] lineSplited = line.split("\t");
        clients.put(lineSplited[0], new Client(lineSplited[0], lineSplited[1],
            lineSplited[2]));
      }
      br.close();
      fstream.close();
    } catch (FileNotFoundException e) {
      Logger.getInstance().write(getClass().getName(),
          "\"clients.all\": File not found!");
    } catch (IOException e) {
      Logger.getInstance().write(getClass().getName(),
          "Can't read \"clients.all\" file: " + e.getMessage());
    }
  }

  /**
   * Scan for online clients using arp-scan tool
   * 
   * TODO add dynamic ip in awk filter
   */
  public void updateOnlineClients() {
    String[] cmd = {
        Const.System.SHELL,
        "-c",
        "arp-scan --interface=" + Const.Network.LAN_INTERFACE
            + " --ignoredups -f=clients.all | awk '/192./ {print($1)}'" };
    try {
      BufferedReader br = ShellExecutor.getInstance().exec(cmd);
      for (Client client : clients.values()) {
        client.setOnline(false);
      }
      String line;
      while ((line = br.readLine()) != null) {
        clients.get(line).setOnline(true);
      }
    } catch (IOException | InterruptedException e) {
      Logger.getInstance().write(getClass().getName(),
          "Can't scan for online clients: " + e.getMessage());
    }

  }

  @Override
  public void update() {
    updateOnlineClients();
  }

  @Override
  public String toString() {
    String newLine = System.getProperty("line.separator");
    StringBuilder sb = new StringBuilder();
    for (Client client : clients.values()) {
      sb.append(client.toString());
      sb.append(newLine);
    }
    return sb.toString();
  }
}
