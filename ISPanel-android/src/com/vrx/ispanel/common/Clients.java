package com.vrx.ispanel.common;

import java.io.Serializable;
import java.util.ArrayList;

public class Clients implements Serializable {

  private static final long serialVersionUID = 3730209298432638091L;

  private ArrayList<Client> clients;

  public Clients() {
    clients = new ArrayList<Client>();
  }

  public void add(Client client) {
    clients.add(client);
  }

  public ArrayList<Client> get() {
    return clients;
  }
}
