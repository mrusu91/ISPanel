package com.vrx.ispanel.server;

public class ManagerUpdater {
  private static ManagerUpdater instance;
  private boolean               stop, isRunning;
  StatusManager                 statusManager;
  ClientManager                 clientManager;

  private ManagerUpdater() {
    statusManager = StatusManager.getInstance();
    clientManager = ClientManager.getInstance();
  }

  public static ManagerUpdater getInstance() {
    if (instance == null)
      instance = new ManagerUpdater();
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
    if (isRunning)
      stop = true;
  }

  private class Worker extends Thread {
    @Override
    public void run() {
      isRunning = true;
      stop = false;
      while (!stop) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          Logger.getInstance().write(getName(),
              "Error while trying to sleep: " + e.getMessage());
          stop = true;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime > statusManager.getLastUpdate()
            + statusManager.getUpdateInterval())
          statusManager.update();
        if (currentTime > clientManager.getLastUpdate()
            + clientManager.getUpdateInterval())
          clientManager.update();
      }
      isRunning = false;
    }
  }
}
