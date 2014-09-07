package com.vrx.ispanel.server;

public abstract class AbstractManager {
  protected boolean isUpdating;
  protected int     updateInterval;
  protected long    lastUpdate;

  public int getUpdateInterval() {
    return updateInterval;
  }

  public void setUpdateInterval(int millisec) {
    updateInterval = millisec;
  }

  public long getLastUpdate() {
    return lastUpdate;
  }

  public abstract void update();
}
