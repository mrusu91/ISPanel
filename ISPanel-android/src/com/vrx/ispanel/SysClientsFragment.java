package com.vrx.ispanel;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SysClientsFragment extends Fragment {

 SysClients sysclients;
 ListView listview;

 int imgOnline, imgOffline;

 
 @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
  return inflater.inflate(R.layout.fragment_sysclients, container, false);
 }

 @Override
 public void onStart() {
  super.onStart();

  // initialize variables from resources
  listview = (ListView) getView().findViewById(R.id.SysClients_listView);
  imgOnline = R.drawable.ic_client_online;
  imgOffline = R.drawable.ic_client_offline;
  
  sysclients = ((ISPanel) getActivity().getApplicationContext()).sysclients;

  // make a hashmap containing information about clients in order to use for make an adapter
  ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
  HashMap<String, String> item;
  if (!sysclients.hasNoClients()) {
   for (int i=0;i<sysclients.allClients_IP.size();i++) {
    if (!sysclients.hasNoOnlineClients()) {
     for (String tempOnline : sysclients.onlineClients_IP) {
      if (tempOnline.contains(sysclients.allClients_IP.get(i))) {
       item = new HashMap<String, String>();
       item.put("img", Integer.toString(imgOnline));
       item.put("name", sysclients.allClients_NAME.get(i));
       item.put("ipmac", "IP: " +sysclients.allClients_IP.get(i) + "   MAC: " + sysclients.allClients_MAC.get(i));
       list.add(item);
      } else {
       item = new HashMap<String, String>();
       item.put("img", Integer.toString(imgOffline));
       item.put("name", sysclients.allClients_NAME.get(i));
       item.put("ipmac", "IP: " +sysclients.allClients_IP.get(i) + "   MAC: " + sysclients.allClients_MAC.get(i));
       list.add(item);
      }
     }
    } else {
     item = new HashMap<String, String>();
     item.put("img", Integer.toString(imgOffline));
     item.put("name", sysclients.allClients_NAME.get(i));
     item.put("ipmac", "IP: " +sysclients.allClients_IP.get(i) + "   MAC: " + sysclients.allClients_MAC.get(i));
     list.add(item);
    }
   }
  } else {
   item = new HashMap<String, String>();
   item.put("img", null);
   item.put("name", "No clients registered!");
   item.put("ipmac", "");
   list.add(item);
  }

  // Create the adapter and populate the listview with it
  String[] FROM = { "img", "name", "ipmac"};
  int[] TO = { R.id.sysclients_item_img, R.id.sysclients_item_title, R.id.sysclients_item_subtitle };

  SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.fragment_sysclients_item, FROM, TO);
  listview.setAdapter(adapter);
 }
}
