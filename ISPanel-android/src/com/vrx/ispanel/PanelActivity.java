/// TO DO --
/// extending TabActivity is deprecated
/// need to use Fragments or actionbar instead


package com.vrx.ispanel;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class PanelActivity extends TabActivity{
	TabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_panel_main);
		
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		TabHost.TabSpec spec;
		Intent intent;
		
		// SysInfo tab
		intent = new Intent(this, SysInfoActivity.class);
		spec = mTabHost.newTabSpec("sysinfo")
				.setContent(intent)
				.setIndicator("System Information");
		
		mTabHost.addTab(spec);
		
		// Clients tab
		intent = new Intent(this, SysClientsActivity.class);
		spec = mTabHost.newTabSpec("sysclients")
				.setContent(intent)
				.setIndicator("Clients");
		
		mTabHost.addTab(spec);
		//mTabHost.setCurrentTab(0);
	}
}
