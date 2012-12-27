package com.vrx.ispanel;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SysInfoActivity extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sysinfo);
		
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> item;
		
		SysInfo sysinfo = ((ISPanel)getApplicationContext()).sysinfo;
		
		String[] sysinfo_data = {sysinfo.getHostname(), sysinfo.getOS(), sysinfo.getCpu(), sysinfo.getLoadAverage(),
				sysinfo.getMemTotal(), sysinfo.getMemFree(), sysinfo.getUptime(), sysinfo.getServerTime()};
		String [] sysinfo_titles = {"Hostname:", "OS:", "CPU:", "Load average:",
				"Total memory:", "Free Memory:", "Uptime:", "Server time:"};
		String [] FROM = {"line1", "line2"};
		int[] TO = {android.R.id.text1, android.R.id.text2};
		
		
		for(int i=0;i<sysinfo_titles.length; i++){
		    item = new HashMap<String,String>();
		    item.put( "line1", sysinfo_titles[i]);
		    item.put( "line2", sysinfo_data[i]);
		    list.add(item);			
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.two_line_list_item, FROM, TO);
		
        ListView listview = (ListView) findViewById(R.id.SysInfo_listView);
        listview.setAdapter(adapter);
	}
	
}
