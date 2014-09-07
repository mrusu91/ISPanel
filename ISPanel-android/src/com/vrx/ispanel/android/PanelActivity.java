package com.vrx.ispanel.android;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PanelActivity extends Activity implements
    ListView.OnItemClickListener {
  private DrawerLayout mDrawerLayout;
  private ListView     mDrawerListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_panel);
    String[] drawerListItems = getResources().getStringArray(
        R.array.drawer_list_items);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerListView = (ListView) findViewById(R.id.list_drawer);
    mDrawerListView.setAdapter(new ArrayAdapter<String>(this,
        R.layout.drawer_list_item, drawerListItems));
    mDrawerListView.setOnItemClickListener(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    Fragment fragment = getFragmentManager().findFragmentById(
        R.id.frame_content);
    if (fragment == null) {
      mDrawerListView.setItemChecked(0, true);
      changeFragment(R.id.frame_content, new StatusFragment());
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_panel, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position,
      long id) {
    Fragment fragment = null;
    switch (position) {
    case 0:
      fragment = new StatusFragment();
      break;
    case 1:
      fragment = new ClientsFragment();
      break;
    case 2:
      disconnect();
      break;
    default:
      break;
    }
    if (fragment != null) {
      mDrawerListView.setItemChecked(position, true);
      mDrawerLayout.closeDrawer(mDrawerListView);
      changeFragment(R.id.frame_content, fragment);
    }
  }

  private void changeFragment(int containerViewId, Fragment fragment) {
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction().replace(containerViewId, fragment)
        .commit();
  }

  private void disconnect() {
    new DisconnectTask().execute();
  }

  private class DisconnectTask extends AsyncTask<Void, Void, Void> {
    ProgressDialog progress;

    @Override
    protected void onPreExecute() {
      progress = new ProgressDialog(PanelActivity.this);
      progress.setMessage(getResources().getString(R.string.disconnecting));
      progress.show();
      super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
      ISPanelApp.remoteAccessClient.disconnect();
      return null;
    }

    @Override
    protected void onPostExecute(Void result) {
      progress.dismiss();
      startActivity(new Intent(PanelActivity.this, LoginActivity.class));
      finish();
      super.onPostExecute(result);
    }
  }

}
