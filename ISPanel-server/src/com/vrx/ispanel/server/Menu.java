package com.vrx.ispanel.server;

import java.util.Scanner;

public class Menu {
  private MenuHandler handler;
  private boolean     stop;
  private Scanner     scanner;

  public Menu(MenuHandler handler) {
    this.handler = handler;
    scanner = new Scanner(System.in);
  }

  public void show() {
    while (!stop) {
      printMenu();
      try {
        int selection = scanner.nextInt();
        handler.onMenuItemSelected(selection);
      } catch (Exception e) {
        Logger.getInstance().write(getClass().getName(),
            "Error on menu selection: " + e.getMessage());
        handler.onMenuItemSelected(0);
      }
    }

  }

  public void exit() {
    stop = true;
  }

  private void printMenu() {
    System.out.println("\n\n\n");
    System.out.println("=== MAIN MENU ===\n");

    System.out.println("1. System informations");
    System.out.println("2. Clients list");
    
    System.out.println("");

    System.out.println("0. Quit");
  }
}