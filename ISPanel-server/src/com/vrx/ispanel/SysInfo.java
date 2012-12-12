package com.vrx.ispanel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


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
	
	
// ------------------------------
	
// update hostname method
	private static String updateHostname(){
		String hn = null;
		try {
			hn = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hn;
	}
	
//  update OS method
	private static String updateOS(){
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("os.name") + " ");
		sb.append(System.getProperty("os.version") + " ");
		sb.append(System.getProperty("os.arch"));
		
		return sb.toString();
	}
	
//  update CPU method
	private static String updateCPU(){
		String [] cmd = {"/bin/sh", "-c", "cat /proc/cpuinfo | grep 'model name'"};
		String temp = null;
		Runtime rt = Runtime.getRuntime();
		try {
			Process proc = rt.exec(cmd);
			BufferedReader shellInputStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			temp = shellInputStream.readLine();
			shellInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp = temp.substring(temp.indexOf(":") + 2, temp.length()-1);
	}
	
// update server time method
	private static String updateServerTime(){
		String cmd = "date";
		String temp = null;
		Runtime rt = Runtime.getRuntime();
		try {
			Process proc = rt.exec(cmd);
			BufferedReader shellInputStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			temp = shellInputStream.readLine();
			shellInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
	
// update Uptime method
	private static String updateUptime(){
		String temp = null;
		try {
			temp = new Scanner(new FileInputStream("/proc/uptime")).next();
			temp = temp.substring(0, temp.indexOf("."));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long secs = Long.parseLong(temp);
		
		//  convert seconds to days, hours, mins
        long days = TimeUnit.SECONDS.toDays(secs);
        secs -= TimeUnit.DAYS.toSeconds(days);
        long hours = TimeUnit.SECONDS.toHours(secs);
        secs -= TimeUnit.HOURS.toSeconds(hours);
        long minutes = TimeUnit.SECONDS.toMinutes(secs);
        secs -= TimeUnit.MINUTES.toSeconds(minutes);
        long seconds = secs;

        StringBuilder sb = new StringBuilder(64);
        
        sb.append(days);
        sb.append(" Days ");
        sb.append(hours);
        sb.append(" Hours ");
        sb.append(minutes);
        sb.append(" Minutes ");
        sb.append(seconds);
        sb.append(" Seconds");

        return(sb.toString());	
	}
	
// update Memory info method
	private static String[] updateMem(){
		String [] cmd = {"/bin/sh", "-c", "cat /proc/meminfo | grep 'Mem'"};
		String[] temp = new String[2];
		Runtime rt = Runtime.getRuntime();
		try {
			Process proc = rt.exec(cmd);
			BufferedReader shellInputStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			temp[0] = shellInputStream.readLine();
			temp[1] = shellInputStream.readLine();
			shellInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp[0] = temp[0].replaceAll("\\D*", "");
		temp[1] = temp[1].replaceAll("\\D*", "");
		return temp;
	}
	
//  update the load average method
	private static String updateLoadAverage(){
		String cmd = "uptime";
		String temp = null;
		Runtime rt = Runtime.getRuntime();
		try {
			Process proc = rt.exec(cmd);
			BufferedReader shellInputStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			temp = shellInputStream.readLine();
			shellInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp.substring(temp.length()-16, temp.length());
	}
	
	
//  Public method to update the variables	
	public void update() {
	//	self explained
		hostname = updateHostname();
		OS = updateOS();
		cpu = updateCPU();
		loadAverage = updateLoadAverage();
		memTotal = updateMem()[0];
		memFree = updateMem()[1];
		uptime = updateUptime();
		serverTime = updateServerTime();
	}   
// ------------------------------
	
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
