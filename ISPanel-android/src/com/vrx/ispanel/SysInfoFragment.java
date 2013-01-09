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

public class SysInfoFragment extends Fragment {

 SysInfo sysinfo;
 ListView listview;

 @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container,
   Bundle savedInstanceState) {
  return inflater.inflate(R.layout.fragment_sysinfo, container, false);
 }

 @Override
 public void onStart() {
  super.onStart();
  sysinfo = ((ISPanel) getActivity().getApplication()).sysinfo;
  listview = (ListView) getView().findViewById(R.id.SysInfo_listView);

  // make a hashmap in order to use for adapter
  ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
  HashMap<String, String> item;

  String[] sysinfo_data = { sysinfo.getHostname(), sysinfo.getOS(),
    sysinfo.getDistro(), sysinfo.getCpu(), sysinfo.getLoadAverage(),
    sysinfo.getMemUsage(), sysinfo.getDiskUsage(), sysinfo.getUptime(),
    sysinfo.getServerTime() };

  String[] sysinfo_titles = { "Hostname:", "OS:", "Distro:", "CPU:",
    "Load average:", "Memory Usage:", "Disk Usage:", "Uptime:", "Server time:" };

  for (int i = 0; i < sysinfo_titles.length; i++) {
   item = new HashMap<String, String>();
   item.put("line1", sysinfo_titles[i]);
   item.put("line2", sysinfo_data[i]);
   list.add(item);
  }

  // create the adapter and populate the list with it
  String[] FROM = { "line1", "line2" };
  int[] TO = { android.R.id.text1, android.R.id.text2 };

  SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
    android.R.layout.two_line_list_item, FROM, TO);

  listview.setAdapter(adapter);
 }

}
