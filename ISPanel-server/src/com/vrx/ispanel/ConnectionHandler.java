package com.vrx.ispanel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable{
	private boolean authorized = false;
	String tempUser, tempPass, message;
	Socket socket = null;
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;

	public ConnectionHandler(Socket s){
		socket = s;
	}
	
	@Override
	public void run() {
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: I/O on getting connection streams: \n" +e);
			closeConnection();
			return;
		}
			
		authorize();
		if(authorized)
			sendSysInfo();
		closeConnection();
	}
	
// -------------------------------
	public void closeConnection() {
		try {
			oos.close();
			ois.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: I/O on closing connection streams: \n" +e);
		}

	}
// ---------------------------------
	
// Write/read methods
	public void writeString(String s){
		try {
			oos.writeObject(s);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error writting String to socket \n" +e);
		}
	}
	public void writeSysInfo(SysInfo sysinfo){
		try {
			oos.writeObject(sysinfo);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error writting SysInfo to socket \n" +e);
		}
	}
	
	public String readString(){
		try {
			return (String)ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error reading String from socket \n" +e);
		}return null;
	}
// ----------------------------
	
// Autentification method
	private void authorize(){
		tempUser = readString();
		tempPass = readString();
		
		if(Server.username.equals(tempUser) && Server.password.equals(tempPass)){
			message = "Connected!";
			writeString(message);
			authorized = true;
			System.out.println("<< " + tempUser +" >> connected!");
		}else{
			message = "User or password incorrect!";
			writeString(message);
			authorized = false;
			System.out.println("Authentification failed for username: "+tempUser + " and password: " +tempPass);
		}
	}
	private void sendSysInfo(){
		SysInfo sysinfo = new SysInfo();
		sysinfo.update();
		writeSysInfo(sysinfo);
		System.out.println("System information sent!");
	}
}
