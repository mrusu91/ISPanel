package com.vrx.ispanel;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

	private int port = 7777;
	private boolean connected = false;
	ServerSocket serversocket=null;
	Socket socket = null;
	ObjectOutputStream oos;
	ObjectInputStream ois;

// to start the server method
	public void start(){
		try {
			serversocket = new ServerSocket(port);
			System.out.println("Server started on port: "+ port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		    System.out.println("Could not listen on port: "+ port);
		    return;
		}
		try {
			socket = serversocket.accept();
			connected = true;
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
		    System.out.println("Connection lost!");
		}
	}

// Close socket connection method
	private void closeConnection(){
		try {
			if(oos!=null)oos.close();
			if(ois!=null)ois.close();
			if(socket!=null)socket.close();
			connected = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection lost!: " +e);
		}
	}
	
// Stop server method

	public void stop(){
		if(serversocket.isClosed())
			System.out.println("Server is not started!!!");
		else{
			try {
				if(connected)closeConnection();
				serversocket.close();
				System.out.println("Server stopped!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

// to check from outside if server is running
	public boolean isConnected(){
		if(connected) return true;
		else return false;
	}
	
// Write/read methods
	public void writeString(String s){
		try {
			oos.writeObject(s);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writeSysInfo(SysInfo sysinfo){
		try {
			oos.writeObject(sysinfo);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String readString(){
		try {
			return (String)ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}return null;
	}
}
