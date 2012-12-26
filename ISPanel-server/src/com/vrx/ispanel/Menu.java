package com.vrx.ispanel;

import java.util.Scanner;

public class Menu {
	
	public void showMenu(){
		System.out.println("\n\n\n");
		System.out.println("=== MAIN MENU ===\n");
		
		System.out.println("1. Start server");
		System.out.println("2. Stop server\n");
		
		System.out.println("3. System Info\n");
		
		System.out.println("0. Quit");
	}
	
	public static void main(String[] args){
	  Menu menu = new Menu();
	  SysInfo sysinfo = new SysInfo();
	  Scanner in = new Scanner(System.in);
	  boolean exit = false;

	  // Make a separated thread to run the server in background
	  ServerHandler serverHandler = null;
	  Thread thread = null;
	  
	  // Show the menu
	  while(!exit){
		  menu.showMenu();
	      int picker = in.nextInt();
	      
		  switch(picker){
		  case 1:
			  if(thread != null)
		    	System.out.println("SERVER IS ALREADY STARTED!!!");
			  else{
				serverHandler = new ServerHandler();
				thread = new Thread(serverHandler);
				thread.setDaemon(true); // in order to stop the thread when main terminates
			    thread.start();
			  }break;
		  case 2:
			  if(thread != null){
				serverHandler.closeServer();
				serverHandler = null;
			 	thread = null;
			  }else
		    	System.out.println("SERVER IS NOT STARTED!!!");
			  break;
		  case 3:
			  sysinfo.update();
			  System.out.println("Hostname: "+ sysinfo.getHostname());
			  System.out.println("OS: " +sysinfo.getOS());
			  System.out.println("CPU: " +sysinfo.getCpu());
			  System.out.println("Load average: " +sysinfo.getLoadAverage());
			  System.out.println("Total Memory (KB) : " +sysinfo.getMemTotal());
			  System.out.println("Free Memory (KB) : " +sysinfo.getMemFree());
			  System.out.println("Uptime: " +sysinfo.getUptime());
			  System.out.println("Time on server: " +sysinfo.getServerTime());
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
