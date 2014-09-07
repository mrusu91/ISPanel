package com.vrx.ispanel.android;

import java.util.ArrayList;

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
import android.widget.Toast;

import com.vrx.ispanel.common.Client;
import com.vrx.ispanel.common.Clients;

public class ClientsFragment extends Fragment {
  ListView          listView;
  ArrayList<Client> listItems;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    listItems = new ArrayList<Client>();
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View layout = inflater.inflate(R.layout.fragment_clients, container, false);
    listView = (ListView) layout.findViewById(R.id.list_clients);
    listView.setAdapter(new ClientsListAdapter(getActivity(), listItems));
    return layout;
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

  private void refreshListItems(Clients clients) {
    listItems.clear();
    for (Client client : clients.get())
      listItems.add(client);
    ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
  }

  private class RefreshTask extends AsyncTask<Void, Void, Clients> {
    ProgressDialog progress;

    @Override
    protected void onPreExecute() {
      progress = new ProgressDialog(getActivity());
      progress.setMessage(getResources().getString(R.string.refreshing));
      progress.show();
      super.onPreExecute();
    }

    @Override
    protected Clients doInBackground(Void... params) {
      return ISPanelApp.remoteAccessClient.refreshClients();
    }

    @Override
    protected void onPostExecute(Clients result) {
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
