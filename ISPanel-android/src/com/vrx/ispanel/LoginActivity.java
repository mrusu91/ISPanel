package com.vrx.ispanel;

import java.io.IOException;
import java.io.OptionalDataException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{

	private boolean connected = false;
	private boolean authorized = false;
	private boolean correctFields = false;
	
	String username, password, serverAddress;
	int serverPort;
	
	EditText mUsername, mPassword, mServerAddress, mServerPort;
	Button mLogIn;
	
	Intent intent;
	
	
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
		((ISPanel)getApplicationContext()).client = new Client(serverAddress, serverPort);
		try {
			((ISPanel)getApplicationContext()).client.start();
			return true;
		}catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "Cannot connect to server!", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "PROBLEM!", Toast.LENGTH_SHORT).show();			
		}
		return false;
	}
	
//	Check fields method
	private boolean checkFields(){
		if(mServerAddress.getText().length() == 0){
			Toast.makeText(this, "Please enter a vald server address!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(mServerPort.getText().length() == 0){
			Toast.makeText(this, "Please enter a vald server port!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(mUsername.getText().length() == 0){
			Toast.makeText(this, "Please enter a vald username!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(mPassword.getText().length() == 0){
			Toast.makeText(this, "Please enter a vald password!", Toast.LENGTH_SHORT).show();
			return false;
		}
		serverAddress = mServerAddress.getText().toString();
		serverPort = Integer.parseInt(mServerPort.getText().toString());
		username = mUsername.getText().toString();
		password = mPassword.getText().toString();
		return true;
	}
	
// Authorize user method
	private boolean authorize(){;
		try {
			((ISPanel)getApplicationContext()).client.writeString(username);
			((ISPanel)getApplicationContext()).client.writeString(password);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show();
			closeConnection();
			return false;
		}
			
		try {
			String result = ((ISPanel)getApplicationContext()).client.readString();
			Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
			String message = "Connected!";
			if (result.equals(message))
				return true;	
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "OptionalDataException", Toast.LENGTH_SHORT).show();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "Class not found", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "IO", Toast.LENGTH_SHORT).show();
		}
		return false;
	}	

// Close connection method
	private void closeConnection(){
		try {
			((ISPanel)getApplicationContext()).client.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "IO - cannot close socket", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		correctFields = false;
		connected = false;
		authorized = false;
		
		correctFields = checkFields();
		if(correctFields)
			connected =	ConnectToServer();
		if(connected)
			authorized = authorize();	
		if(authorized){
			showSysInfo();
		}
	}
	
	
// show Sysinfo on the phone method   BAD WAY
	private void showSysInfo(){
		intent = new Intent(this, PanelActivity.class);
		startActivity(intent);
	}
		
// --- OPTION MENU
/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}*/

}
