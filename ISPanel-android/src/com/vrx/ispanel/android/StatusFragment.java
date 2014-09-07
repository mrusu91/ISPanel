package com.vrx.ispanel.android;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.vrx.ispanel.common.Status;

public class StatusFragment extends Fragment {
  ListView                           listView;
  ArrayList<HashMap<String, String>> listItems;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    listItems = new ArrayList<HashMap<String, String>>();
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_status, container, false);
    listView = (ListView) view.findViewById(R.id.list_status);
    SimpleAdapter adapter = new SimpleAdapter(getActivity(), listItems,
        android.R.layout.two_line_list_item, new String[] { "line1", "line2" },
        new int[] { android.R.id.text1, android.R.id.text2 });
    listView.setAdapter(adapter);
    return view;
  }

  @Override
  public void onStart() {
    super.onStart();
    new RefreshTask().execute();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_panel_refresh) {
      new RefreshTask().execute();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void refreshListItems(Status status) {
    listItems.clear();
    HashMap<String, String> item;
    String[] status_titles = { "Hostname:", "OS:", "Distro:", "CPU:",
        "Load average:", "Memory Usage:", "Disk Usage:", "Uptime:",
        "Server time:" };
    String[] status_data = { status.getHostname(), status.getOS(),
        status.getDistro(), status.getCpu(), status.getLoadAverage(),
        status.getMemUsage(), status.getDiskUsage(), status.getUptime(),
        status.getServerTime() };
    for (int i = 0; i < status_titles.length; i++) {
      item = new HashMap<String, String>();
      item.put("line1", status_titles[i]);
      item.put("line2", status_data[i]);
      listItems.add(item);
    }
    ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
  }

  private class RefreshTask extends AsyncTask<Void, Void, Status> {
    ProgressDialog progress;

    @Override
    protected void onPreExecute() {
      progress = new ProgressDialog(getActivity());
      progress.setMessage(getResources().getString(R.string.refreshing));
      progress.show();
      super.onPreExecute();
    }

    @Override
    protected com.vrx.ispanel.common.Status doInBackground(Void... params) {
      return ISPanelApp.remoteAccessClient.refreshStatus();
    }

    @Override
    protected void onPostExecute(com.vrx.ispanel.common.Status result) {
      progress.dismiss();
      if (result == null)
        Toast.makeText(getActivity(), R.string.connection_error,
            Toast.LENGTH_SHORT).show();
      else {
        refreshListItems(result);
      }
      super.onPostExecute(result);
    }
  }
}
