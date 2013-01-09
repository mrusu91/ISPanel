package com.vrx.ispanel;

import java.io.IOException;
import java.io.OptionalDataException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

 private boolean correctFields = false;
 int serverPort;
 String username, password, serverAddress;
 EditText mUsername, mPassword, mServerAddress, mServerPort;
 Button mLogIn;
 Intent intent;

 ConnectTask connectTask;
 ProgressDialog progressDialog;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_login); // set the layout

  mServerAddress = (EditText) findViewById(R.id.LogIn_editText_serverAddress);
  mServerPort = (EditText) findViewById(R.id.LogIn_editText_serverPort);
  mUsername = (EditText) findViewById(R.id.LogIn_editText_username);
  mPassword = (EditText) findViewById(R.id.LogIn_editText_password);
  mLogIn = (Button) findViewById(R.id.LogIn_button);
  mLogIn.setOnClickListener(this);
  progressDialog = new ProgressDialog(this);
  progressDialog.setIndeterminate(true);
 }

 // Check fields method
 private boolean checkFields() {
  if (mServerAddress.getText().length() == 0) {
   Toast.makeText(this, "Please enter a vald server address!",
     Toast.LENGTH_SHORT).show();
   return false;
  }
  if (mServerPort.getText().length() == 0
    || Integer.parseInt(mServerPort.getText().toString()) > 65534) {
   Toast.makeText(this, "Please enter a vald server port!", Toast.LENGTH_SHORT)
     .show();
   return false;
  }
  if (mUsername.getText().length() == 0) {
   Toast.makeText(this, "Please enter a vald username!", Toast.LENGTH_SHORT)
     .show();
   return false;
  }
  if (mPassword.getText().length() == 0) {
   Toast.makeText(this, "Please enter a vald password!", Toast.LENGTH_SHORT)
     .show();
   return false;
  }
  serverAddress = mServerAddress.getText().toString();
  serverPort = Integer.parseInt(mServerPort.getText().toString());
  username = mUsername.getText().toString();
  password = mPassword.getText().toString();
  return true;
 }

 // when clicking the log in button
 @Override
 public void onClick(View v) {
  correctFields = false;
  correctFields = checkFields();
  if (correctFields) {
   
   // Need to use a AsyncTask to comunicate with the server in background, Not to block the UI
   connectTask = new ConnectTask();
   connectTask.execute(1);
  }
 }

 // show Panel with all infos
 private void showPanel() {
  intent = new Intent(this, PanelActivity.class);
  startActivity(intent);
 }

 // when back button is pressed, if pushed while connecting, dismiss the progress dialog
 @Override
 public void onBackPressed() {
  super.onBackPressed();
  if(connectTask != null){
  connectTask.cancel(true);
  progressDialog.dismiss();
  }
 }

 // the asynctask inner class
 public class ConnectTask extends AsyncTask<Integer, Integer, String> {
  private static final String TAG = "ConnectTask";

  // just show the progress dialog
  @Override
  protected void onPreExecute() {
   super.onPreExecute();
   progressDialog.setTitle(R.string.app_name);
   progressDialog.setMessage("Connecting to server...");
   progressDialog.show();
  }

  // change the text of progress dialog depending of stage
  @Override
  protected void onProgressUpdate(Integer... values) {
   super.onProgressUpdate(values);
   switch (values[0]) {
   case 1:
    progressDialog.setMessage("Authentificating...");
    break;
   case 2:
    progressDialog.setMessage("Getting informations...");
    break;
   default:
    break;
   }
  }

  // after background task is executed
  @Override
  protected void onPostExecute(String result) {
   super.onPostExecute(result);
   progressDialog.dismiss();
   if (result.contains("OK!"))
    showPanel(); // if authentification success, and sysobject updated, show the panel with all info
   else {
    Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
   }
  }  
  
  // background task
  @Override
  protected String doInBackground(Integer... params) {
   boolean connected = false;
   boolean authorized = false;
   String result;
   connected = ConnectToServer(); // connect to server
   if (connected) {
    publishProgress(1); // change text of progress dialog
    authorized = authorize(); // try to authorize
   } else {
    result = "Cannot connect to server!";
    return result;
   }

   if (authorized) {
    publishProgress(2); // if auth succeed, change text of progress dialog
    updateSysObjects(); // update sys objects
    result = "OK!"; // tell the UI to show panel
   } else {
    result = "Authentification failled!";
   }
   return result;
  }

  // Connect to server method
  private boolean ConnectToServer() {
   ((ISPanel) getApplicationContext()).client = new Client(serverAddress,
     serverPort);
   try {
    ((ISPanel) getApplicationContext()).client.start();
    return true;
   } catch (UnknownHostException e) {
    Log.e(TAG, "Unknown Host Exception: " + e.getMessage());
   } catch (IOException e) {
    Log.e(TAG, "IOException: " + e.getMessage());
   }
   return false;
  }

  // Authorize user method
  private boolean authorize() {
   ;
   try {
    ((ISPanel) getApplicationContext()).client.writeString(username);
    ((ISPanel) getApplicationContext()).client.writeString(password);
   } catch (IOException e) {
    Log.e(TAG, "IOException: " + e.getMessage());
    closeConnection();
    return false;
   }

   try {
    String result = ((ISPanel) getApplicationContext()).client.readString();
    String message = "Connected!";
    if (result.equals(message))
     return true;
   } catch (OptionalDataException e) {
    Log.e(TAG, "OptionalDataException: " + e.getMessage());
   } catch (ClassNotFoundException e) {
    Log.e(TAG, "ClassNotFoundException: " + e.getMessage());
   } catch (IOException e) {
    Log.e(TAG, "IOException: " + e.getMessage());
   }
   return false;
  }

  // Close connection method
  private void closeConnection() {
   try {
    ((ISPanel) getApplicationContext()).client.close();
   } catch (IOException e) {
    Log.e(TAG, "IOException: " + e.getMessage());
   }
  }
  
  private void updateSysObjects(){
   try {
    ((ISPanel)getApplication()).getSysObjects();
   } catch (OptionalDataException e) {
    Log.e(TAG, "OptionalDataException: " + e.getMessage());
   } catch (ClassNotFoundException e) {
    Log.e(TAG, "ClassNotFoundException: " + e.getMessage());
   } catch (IOException e) {
    Log.e(TAG, "IOException: " + e.getMessage());
   }
  }
 }
}
