package com.vrx.ispanel;

public class ConnThread implements Runnable {

	Server server = new Server();
	private String username = "demo";
	private String password = "demo";
	private String tempUser, tempPass, message;
	private boolean authorized = false;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		server.start();
		while(server.isConnected()){
			authUser();
			
			if(authorized){
				sendSysInfo();
				CloseConnection();
			}
		}
	}
	
// Authentificate user Method
	private void authUser(){
		tempUser = server.readString();
		tempPass = server.readString();
		
		if(username.equals(tempUser) && password.equals(tempPass)){
			message = "Connected!";
			server.writeString(message);
			authorized = true;
			System.out.println("User connected!");
		}else{
			message = "User or password incorrect!";
			server.writeString(message);
			System.out.println("Authentification failed!");
			authorized = false;
		}
	}

//  Send System Information Method
	private void sendSysInfo(){
		SysInfo sysinfo = new SysInfo();
		sysinfo.update();
		server.writeSysInfo(sysinfo);
		System.out.println("System information sent!");
	}
	
// Close Connection
	public void CloseConnection(){
		server.stop();
	}
}
