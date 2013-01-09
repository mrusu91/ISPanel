package com.vrx.ispanel;

import java.io.IOException;
import java.io.OptionalDataException;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class PanelActivity extends FragmentActivity implements
  OnNavigationListener {

 private static final String TAG = "PanelActivity";
 private String currentFragTAG; // helps to identify the current fragment
 private MenuItem currentItem; // helps to identify what button is clicked

 FragmentTransaction fragTrans;
 FragmentManager fragMan;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_panel_main);

  final ActionBar actionBar = getActionBar();
  fragMan = getSupportFragmentManager();

  // Specify that a dropdown list should be displayed in the action bar.
  actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

  // Hide the title
  actionBar.setDisplayShowTitleEnabled(false);

  ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
    android.R.layout.simple_spinner_dropdown_item, getResources()
      .getStringArray(R.array.actionbar_menulist));

  // Specify an Adapter to populate the dropdown list.
  actionBar.setListNavigationCallbacks(adapter, this);

  // When panel is first created show the item with index 0
  actionBar.setSelectedNavigationItem(0);
 }

 // Provide a listener to be called when an item is selected.
 @Override
 public boolean onNavigationItemSelected(int itemPosition, long itemId) {
  fragTrans = fragMan.beginTransaction();
  switch (itemPosition) { // depends on what item is selected from dropdown menu,
  case 0:                 // change to coresponding fragment and put a TAG, to help identify later
   currentFragTAG = "sysinfo";
   fragTrans.replace(R.id.panel_layout, new SysInfoFragment(), currentFragTAG);
   break;
  case 1:
   currentFragTAG = "sysclients";
   fragTrans.replace(R.id.panel_layout, new SysClientsFragment(),
     currentFragTAG);
   break;
  default:
   break;
  }
  fragTrans.commit(); // make the change
  return true;
 }

 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
  // Inflate the menu; this adds items to the action bar if it is present.
  // for older phone < 3.0, it's called when MENU button is clicked
  getMenuInflater().inflate(R.menu.activity_panel_menu, menu);
  return true;
 }

 // cancel the back button
 // instead I put a (X) button
 @Override
 public void onBackPressed() {
  return;
 }

 //When (X) button is clicked tell the server and close connection
 private void onExitPressed() {
  super.onBackPressed();
  try {
   ((ISPanel) getApplication()).client.writeString("exit");
   ((ISPanel) getApplication()).client.close();
  } catch (IOException e) {
   Log.d(TAG, e.getMessage());
  }
 }

 // Whem a Item from actionbar/menu is clicked, NOT the dropdown menu
 @Override
 public boolean onMenuItemSelected(int featureId, MenuItem item) {
  switch (item.getItemId()) {
  case R.id.actionbaritem_exit:
   onExitPressed();
   break;
  case R.id.actionbaritem_refresh:
   refresh(item);
   break;
  default:
   break;
  }
  return super.onMenuItemSelected(featureId, item);
 }

 // when refresh is clicked, make a background task that update the Sys objects
 private void refresh(MenuItem item) {
  currentItem = item;
  new RefreshTask().execute(1); 
 }

 public class RefreshTask extends AsyncTask<Integer, Integer, String> {

  @Override
  protected void onPreExecute() {
   super.onPreExecute();

   // set refresh button invisible
   currentItem.setVisible(false);
  }

  // background process
  @Override
  protected String doInBackground(Integer... params) {
   try {
    ((ISPanel) getApplication()).client.writeString("refresh"); // tell the server to send the Sys Objects to client
    ((ISPanel) getApplication()).getSysObjects(); // receive the sys objects from server
   } catch (OptionalDataException e) {
    Log.e(TAG, "OptionalDataException: " + e.getMessage());
   } catch (ClassNotFoundException e) {
    Log.e(TAG, "ClassNotFoundException: " + e.getMessage());
   } catch (IOException e) {
    Log.e(TAG, "IOException: " + e.getMessage());
   }
   return "DONE";
  }

  // after background task is finished
  @Override
  protected void onPostExecute(String result) {
   super.onPostExecute(result);

   // refresh the fragment with new data
   FragmentTransaction tx = fragMan.beginTransaction();
   if (currentFragTAG == "sysinfo")
    tx.replace(R.id.panel_layout, new SysInfoFragment(), currentFragTAG);
   else
    tx.replace(R.id.panel_layout, new SysClientsFragment(), currentFragTAG);
   tx.commit();

   // set the refresh button visible
   currentItem.setVisible(true);
  }
 }
}
