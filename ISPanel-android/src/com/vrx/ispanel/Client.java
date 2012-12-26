package com.vrx.ispanel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private String serveraddress;
	private int serverport;
	private Socket socket = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	
	public Client(String s, int p){
		serveraddress = s;
		serverport = p;
	}

	public void start() throws UnknownHostException, IOException{
		socket = new Socket(serveraddress, serverport);
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois =new ObjectInputStream(socket.getInputStream());
	 }
	public void close() throws IOException{
		oos.close();
		ois.close();
		socket.close();
	}
	
	public void writeString(String s) throws IOException{
		oos.writeObject(s);
		oos.flush();
	}
	public String readString() throws OptionalDataException, ClassNotFoundException, IOException{
		return (String)ois.readObject();
	}
	public SysInfo readSysInfo() throws OptionalDataException, ClassNotFoundException, IOException{
		return (SysInfo) ois.readObject();
	}
}


