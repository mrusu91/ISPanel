package com.vrx.ispanel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	public static String username = "demo";
	public static String password = "demo";
	private int port = 7777;
	ArrayList<ConnectionHandler> NrConnections; // how many connection are
	ServerSocket serversocket=null;
	Socket socket = null;

// to start the server method
	public void start(){
		try {
			serversocket = new ServerSocket(port);
			NrConnections = new ArrayList<ConnectionHandler>();
			System.out.println("Server started on port: "+ port);
		} catch (IOException e) {
		    System.out.println("Could not listen on port: "+ port);
		    return;
		}
		
		while(serversocket != null){
			try {
				socket = serversocket.accept();
				NrConnections.add(new ConnectionHandler(socket));
				Thread t = new Thread(NrConnections.get(NrConnections.size()-1));
				t.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			    System.out.println("Not accepting clients anymore!");
			}
		}
	}

// Close socket connection method
	public void closeConnection(){
		// Close all client connections and their thread
		for(int i=0; i<NrConnections.size(); i++)
			NrConnections.get(i).closeConnection();
		
		// close intial socket
		try {
			if(socket!=null){
				socket.close();
				socket = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection lost!: " +e);
		}
	}
	
// Stop server method

	public void stop(){
		if(serversocket == null)
			System.out.println("Server is not started!!!");
		else{
			try {
				closeConnection();
				serversocket.close();
				serversocket = null;
				System.out.println("Server stopped!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error stopping server:\n" + e);
			}
		}
	}
}
