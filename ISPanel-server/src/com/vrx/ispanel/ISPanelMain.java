package com.vrx.ispanel;

import java.util.Scanner;

public class ISPanelMain {

	public static SysObjectsUpdater sysobjupdater;
	
	public static void main(String[] args){
		
		  Menu menu = new Menu();
		  Scanner in = new Scanner(System.in);
		  boolean exit = false;
		  
		  // Make a separated thread to update the system objects
		  sysobjupdater = new SysObjectsUpdater();
		  
		  Thread updateThread = new Thread(sysobjupdater);
		  updateThread.setDaemon(true);
		  updateThread.start();
		  
		  // Make a separated thread to run the server in background
		  ServerHandler serverHandler = null;
		  Thread serverThread = null;
		  
		  // Show the menu
		  while(!exit){
			  menu.showMenu();
		      int picker = in.nextInt();
		      
			  switch(picker){
			  case 1:
				  if(serverThread != null)
			    	System.out.println("SERVER IS ALREADY STARTED!!!");
				  else{
					serverHandler = new ServerHandler();
					serverThread = new Thread(serverHandler);
					serverThread.setDaemon(true); // in order to stop the thread when main terminates
				    serverThread.start();
				  }break;
			  case 2:
				  if(serverThread != null){
					serverHandler.closeServer();
					serverHandler = null;
				 	serverThread = null;
				  }else
			    	System.out.println("SERVER IS NOT STARTED!!!");
				  break;
			  case 3:
				  sysobjupdater.sysinfo.show();
				  break;
				  
			  case 4:
				  sysobjupdater.sysclients.show();
				  break;
				  
			  case 0:
				  exit = true;
				  break;
			  
			  default:
				  menu.showMenu();
				  break;
			  }
		  }
		  in.close();
		}

}
