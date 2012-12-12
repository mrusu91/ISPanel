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

	  // Make a separated thread to run the server
	  ConnThread connthread = new ConnThread();

	  // Show the menu
	  while(true){
		  menu.showMenu();
		  
	      int picker = in.nextInt();
	      if(picker == 1){
	    	Thread thread = new Thread(connthread);
	    	thread.setDaemon(true);
	    	thread.start();
	  	  }
	      else if (picker ==2){
	 	    connthread.CloseConnection();
	  	  }
	      else if (picker == 3){
	    	sysinfo.update();
		    System.out.println("Hostname: "+ sysinfo.getHostname());
		    System.out.println("OS: " +sysinfo.getOS());
		    System.out.println("CPU: " +sysinfo.getCpu());
		    System.out.println("Load average: " +sysinfo.getLoadAverage());
		    System.out.println("Total Memory (KB) : " +sysinfo.getMemTotal());
		    System.out.println("Free Memory (KB) : " +sysinfo.getMemFree());
		    System.out.println("Uptime: " +sysinfo.getUptime());
		    System.out.println("Time on server: " +sysinfo.getServerTime());
	  	  }
	      else if(picker == 0)break;
	  }
	 
	  in.close();
	    
	}
}
