package com.vrx.ispanel;

import java.io.IOException;

public class SysObjectsUpdater implements Runnable{
	
	public SysInfo sysinfo;
	public SysClients sysclients;
	private boolean exit;
	
	@Override
	public void run() {
		exit = false;
		sysinfo = new SysInfo();
		sysclients = new SysClients();
		
		while(!exit){
			updateOnlineClients();
			sysinfo.update();
			sysclients.update();
			try {
				Thread.currentThread();
				Thread.sleep(60000); // Update every minute
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void updateOnlineClients(){
		// need to use a temp file because when scanning, the file is empty
		String [] cmd = {"/bin/sh", "-c", "cp -f ~/ISPanel/clients.online.temp ~/ISPanel/clients.online"}; 
		String [] cmd2 = {"/bin/sh", "-c", "arp-scan --interface=eth1 --ignoredups -f=clients.all | grep '192.' | tee ~/ISPanel/clients.online.temp"};
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec(cmd);	
			rt.exec(cmd2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("SysObjectUpdater: Can't exec updateOnlineClients method");
		}
	}
	
	public void stopUpdateing(){
		exit = true;
	}
}
