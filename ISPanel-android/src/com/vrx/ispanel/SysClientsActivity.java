package com.vrx.ispanel;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SysClientsActivity extends Activity{
	
	SysClients sysclients;
	ListView listview;
	
	int imgOnline, imgOffline;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sysclients);
		
		listview = (ListView)findViewById(R.id.SysClients_listView);
		imgOnline = android.R.drawable.presence_online;
		imgOffline = android.R.drawable.presence_offline;
		sysclients = ((ISPanel)getApplicationContext()).sysclients;
		
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> item;
		String [] FROM = {"img", "text1"};
		int[] TO = {R.id.sysclients_item_img, R.id.sysclients_item_text1};
		
		if(!sysclients.hasNoClients()){
			for(String tempClient : sysclients.all_Clients){
				if(!sysclients.hasNoOnlineClients()){
					for(String tempOnline : sysclients.online_Clients){
						if(tempOnline.contains(tempClient)){
							item = new HashMap<String,String>();
							item.put("img", Integer.toString(imgOnline));
							item.put("text1", tempClient);
							list.add(item);	
						}
						else{
							item = new HashMap<String,String>();
							item.put("img", Integer.toString(imgOffline));
							item.put("text1", tempClient);
							list.add(item);
						}
					}
				}else{
					item = new HashMap<String,String>();
					item.put("img", Integer.toString(imgOffline));
					item.put("text1", tempClient);
					list.add(item);	
				}
			}
		}else{
			item = new HashMap<String,String>();
			item.put("img", null);
			item.put("text1", "No clients registered!");
			list.add(item);	
		}

		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.activity_sysclients_item , FROM, TO);
        listview.setAdapter(adapter);
	}
}
