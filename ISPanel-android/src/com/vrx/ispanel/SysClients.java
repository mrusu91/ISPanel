package com.vrx.ispanel;

import java.io.Serializable;
import java.util.ArrayList;

public class SysClients implements Serializable {

	private static final long serialVersionUID = 3730209298392638091L;
	
	ArrayList<String> all_Clients;
	ArrayList<String> online_Clients;
	private boolean noOnlineClients;
	private boolean noClients;
	
	public SysClients(){
		all_Clients = new ArrayList<String>();
		online_Clients = new ArrayList<String>();
		
	}
	
	public ArrayList<String> getAllClients(){
		return all_Clients;
	}
	public ArrayList<String> getOnlineClients(){
		return online_Clients;
	}
	
	public boolean hasNoClients(){
		return noClients;
	}
	public boolean hasNoOnlineClients(){
		return noOnlineClients;
	}
}
