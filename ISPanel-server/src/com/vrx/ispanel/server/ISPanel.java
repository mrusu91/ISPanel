// TO DO: change the app to use a config file to be easy to configure

package com.vrx.ispanel.server;

public class ISPanel implements MenuHandler {

  StatusManager statusManager;
  ClientManager clientManager;
  Logger        log;
  Menu          menu;

  public static void main(String[] args) {
    ISPanel ispanel = new ISPanel();
    ispanel.start();
  }

  public ISPanel() {
    statusManager = StatusManager.getInstance();
    clientManager = ClientManager.getInstance();
    menu = new Menu(this);
  }

  public void start() {
    ManagerUpdater.getInstance().start();
    RemoteAccessServer.getInstance().start();
    menu.show();
  }

  public void exit() {
    ManagerUpdater.getInstance().stop();
    RemoteAccessServer.getInstance().stop();
    menu.exit();
  }

  @Override
  public void onMenuItemSelected(int selection) {
    switch (selection) {
    case 0:
      exit();
      break;
    case 1:
      System.out.println(statusManager);
      break;
    case 2:
      System.out.println(clientManager);
      break;
    default:
      break;
    }
  }

}
