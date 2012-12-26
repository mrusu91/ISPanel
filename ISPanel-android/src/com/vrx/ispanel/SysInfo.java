package com.vrx.ispanel;
import java.io.Serializable;


public class SysInfo implements Serializable{

	private static final long serialVersionUID = 221L;
	
	private String hostname;
	private String OS;
	private String cpu;
	private String loadAverage;
	private String memTotal;
	private String memFree;
	private String uptime;
	private String serverTime;
		
//    GETTERS
	public String getHostname(){
		return hostname;
	}
	public String getOS(){
		return OS;
	}
	public String getUptime(){
		return uptime;
	}
	public String getCpu(){
		return cpu;
	}
	public String getServerTime(){
		return serverTime;
	}
	public String getMemFree(){
		return memFree;
	}
	public String getMemTotal(){
		return memTotal;
	}
	public String getLoadAverage(){
		return loadAverage;
	}
// ------------------------------
}
