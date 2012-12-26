package com.vrx.ispanel;

import java.io.IOException;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SysInfoActivity extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sysinfo);
		
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> item;
		
		SysInfo sysinfo = getSysInfo();
		
		
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
	
	// Get sysinfo method
		private SysInfo getSysInfo(){
			try {
				SysInfo Tempsysinfo = ((ISPanel)getApplicationContext()).client.readSysInfo();
				//Toast.makeText(this, "GOT IT!!!", Toast.LENGTH_LONG).show();
				return Tempsysinfo;
			} catch (OptionalDataException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Not an object!", Toast.LENGTH_SHORT).show();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Class not found", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "I/O Exception", Toast.LENGTH_SHORT).show();
			}	
			return null;
		}

}
