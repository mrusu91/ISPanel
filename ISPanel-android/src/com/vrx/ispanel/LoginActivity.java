package com.vrx.ispanel;

import java.io.IOException;
import java.io.OptionalDataException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{

	private boolean connected = false;
	private boolean authorised = false;
	String username, password;
	String serverAddress;
	int serverPort;
	TextView mLogInTitle;
	EditText mUsername;
	EditText mPassword;
	Button mLogIn;
	EditText mServerAddress, mServerPort;
	
	TextView sysinfo_title_hostname, sysinfo_title_os, sysinfo_title_cpu, sysinfo_title_loadaverage,
			 sysinfo_title_memtotal, sysinfo_title_memfree, sysinfo_title_uptime, sysinfo_title_servertime;
	TextView sysinfo_value_hostname, sysinfo_value_os, sysinfo_value_cpu, sysinfo_value_loadaverage,
			 sysinfo_value_memtotal, sysinfo_value_memfree, sysinfo_value_uptime, sysinfo_value_servertime;

	Client client;;
	SysInfo sysinfo;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	     StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    	StrictMode.setThreadPolicy(policy);
	    	
		setContentView(R.layout.activity_login);
		
		mServerAddress = (EditText)findViewById(R.id.LogIn_editText_serverAddress);
		mServerPort = (EditText)findViewById(R.id.LogIn_editText_serverPort);
		
		mUsername = (EditText) findViewById(R.id.LogIn_editText_username);
		mPassword = (EditText) findViewById(R.id.LogIn_editText_password);
		mLogIn = (Button) findViewById(R.id.LogIn_button);
		mLogIn.setOnClickListener(this);
	}

// Connect to server method
	private boolean ConnectToServer(){
		serverAddress = mServerAddress.getText().toString();
		if(mServerPort.getText().length() != 0) // need to have something in port filed in order to parse
			serverPort = Integer.parseInt(mServerPort.getText().toString()); 
		else{
			Toast.makeText(this, "Please enter a valid server port!", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(serverAddress.length() == 0)
			Toast.makeText(this, "Please enter a valid server address!", Toast.LENGTH_SHORT).show();
		else if (serverPort == 0 || serverPort > 65534)
			Toast.makeText(this, "Please enter a valid server port!", Toast.LENGTH_SHORT).show();
		else{
			client = new Client(serverAddress, serverPort);
			try {
				client.start();
				return true;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Cannot connect to server!", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "PROBLEM!", Toast.LENGTH_SHORT).show();			}return false;
		}return false;
	}
//	Authentificate method
	private void authUser(){;
		username = mUsername.getText().toString();
		password = mPassword.getText().toString();
		if(username.length() == 0)
			Toast.makeText(this, "Please enter an username!", Toast.LENGTH_LONG).show();
		else if(password.length() == 0)
			Toast.makeText(this, "Please enter a password!", Toast.LENGTH_LONG).show();
		else{
			
			try {
				client.writeString(username);
				client.writeString(password);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Please try again!", Toast.LENGTH_LONG).show();
			}
			
			try {
				String result = client.readString();
				String message = "Connected!";
				Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
				if (result.equals(message)){
					authorised = true;	
				}
			} catch (OptionalDataException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "OptionalDataException", Toast.LENGTH_LONG).show();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Class not found", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "IO", Toast.LENGTH_LONG).show();
			}
		}
	}
	
// Get sysinfo method
	private SysInfo getSysInfo(){
		try {
			SysInfo Tempsysinfo = client.readSysInfo();
			//Toast.makeText(this, "GOT IT!!!", Toast.LENGTH_LONG).show();
			return Tempsysinfo;
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "1", Toast.LENGTH_LONG).show();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "2", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "3", Toast.LENGTH_LONG).show();
		}	
		return null;
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(!connected){
			connected =	ConnectToServer();
		}
		
		if(connected){
			authUser();	
		}
		
		if(authorised){
			sysinfo = getSysInfo();
			showSysInfo();
		}
	}
	
	
// show Sysinfo on the phone method   BAD WAY
	private void showSysInfo(){
		LoginActivity.this.setContentView(R.layout.activity_sysinfo_item);
		
		sysinfo_title_hostname = (TextView)findViewById(R.id.sysinfo_title_hostname);
		sysinfo_title_os = (TextView)findViewById(R.id.sysinfo_title_os);
		sysinfo_title_cpu = (TextView)findViewById(R.id.sysinfo_title_cpu);
		sysinfo_title_memtotal = (TextView)findViewById(R.id.sysinfo_title_memtotal);
		sysinfo_title_memfree = (TextView)findViewById(R.id.sysinfo_title_memfree);
		sysinfo_title_loadaverage = (TextView)findViewById(R.id.sysinfo_title_loadaverage);
		sysinfo_title_uptime = (TextView)findViewById(R.id.sysinfo_title_uptime);
		sysinfo_title_servertime = (TextView)findViewById(R.id.sysinfo_title_servertime);
		
		sysinfo_value_hostname = (TextView)findViewById(R.id.sysinfo_value_hostname);
		sysinfo_value_os = (TextView)findViewById(R.id.sysinfo_value_os);
		sysinfo_value_cpu = (TextView)findViewById(R.id.sysinfo_value_cpu);
		sysinfo_value_memtotal = (TextView)findViewById(R.id.sysinfo_value_memtotal);
		sysinfo_value_memfree = (TextView)findViewById(R.id.sysinfo_value_memfree);
		sysinfo_value_loadaverage = (TextView)findViewById(R.id.sysinfo_value_loadaverage);
		sysinfo_value_uptime = (TextView)findViewById(R.id.sysinfo_value_uptime);
		sysinfo_value_servertime = (TextView)findViewById(R.id.sysinfo_value_servertime);
		
		sysinfo_title_hostname.setText("Hostname:");
		sysinfo_title_os.setText("OS:");
		sysinfo_title_cpu.setText("CPU:");
		sysinfo_title_loadaverage.setText("Load average:");
		sysinfo_title_memtotal.setText("Total Memory:");
		sysinfo_title_memfree.setText("Free Memory:");
		sysinfo_title_uptime.setText("Uptime:");
		sysinfo_title_servertime.setText("Server time:");
		
		sysinfo_value_hostname.setText(sysinfo.getHostname());
		sysinfo_value_os.setText(sysinfo.getOS());
		sysinfo_value_cpu.setText(sysinfo.getCpu());
		sysinfo_value_loadaverage.setText(sysinfo.getLoadAverage());
		sysinfo_value_memtotal.setText(sysinfo.getMemTotal());
		sysinfo_value_memfree.setText(sysinfo.getMemFree());
		sysinfo_value_uptime.setText(sysinfo.getUptime());
		sysinfo_value_servertime.setText(sysinfo.getServerTime());
		
		
		
		
	}
// --- OPTION MENU
/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}*/

}
