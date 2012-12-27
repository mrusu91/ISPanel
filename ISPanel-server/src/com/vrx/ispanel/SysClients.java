package com.vrx.ispanel;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

public class SysClients implements Serializable {

	private static final long serialVersionUID = 3730209298392638091L;
	
	ArrayList<String> all_Clients;
	ArrayList<String> online_Clients;
	private boolean noOnlineClients;
	private boolean noClients;
	
	
	FileInputStream fstream;
	DataInputStream in;
	BufferedReader br;
	
	public SysClients(){
		all_Clients = new ArrayList<String>();
		online_Clients = new ArrayList<String>();
	}
	private void updateAllClients(){
		try {
			fstream = new FileInputStream("clients.all");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String line;
			noClients = false;
			
			all_Clients.clear();
			
			while ((line = br.readLine()) != null){
				all_Clients.add(line);
			}
			
			if(all_Clients.size() == 0)
				noClients = true;
			
			br.close();
			br = null;
			in.close();
			in = null;
			fstream.close();
			fstream = null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("clients.all: File not found!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("clients.all: Can't read from file!");
		}
	}
	
	private void updateOnlineClients(){
		try {
			fstream = new FileInputStream("clients.online");
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String line;
			noOnlineClients = false;
			
			online_Clients.clear();
			
			while ((line = br.readLine()) != null){
				//line = line.substring(0, line.indexOf(' '));
				online_Clients.add(line);
			}
			if(online_Clients.size() == 0)
				noOnlineClients = true;
			
			br.close();
			br = null;
			in.close();
			in = null;
			fstream.close();
			fstream = null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("clients.online: File not found!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("clients.online: Can't read from file!");
		}
	}
	
	public void update(){
		updateOnlineClients();
		updateAllClients();
	}
	
	public ArrayList<String> getAllClients(){
		return all_Clients;
	}
	public ArrayList<String> getOnlineClients(){
		return online_Clients;
	}

	public void show() {	
		if(!noClients){
			for(String tempClient : all_Clients){
				if(!noOnlineClients){
					for(String tempOnline : online_Clients){
						if(tempOnline.contains(tempClient))
							System.out.println("(*)" + tempClient);
						else
							System.out.println(tempClient);
					}
				}else System.out.println(tempClient);
			}
		}else System.out.println("No clients registered!");
	}
}
