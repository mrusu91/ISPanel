package com.vrx.ispanel.android;

import java.util.List;

import com.vrx.ispanel.common.Client;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ClientsListAdapter extends ArrayAdapter<Client> {
  private int mListItemLayoutResId;

  public ClientsListAdapter(Context context, Client[] clients) {
    this(context, R.layout.fragment_clients_item, clients);
  }

  public ClientsListAdapter(Context context, int listItemLayoutResourceId,
      Client[] clients) {
    super(context, listItemLayoutResourceId, clients);
    mListItemLayoutResId = listItemLayoutResourceId;
  }

  public ClientsListAdapter(Activity activity, List<Client> clients) {
    this(activity, R.layout.fragment_clients_item, clients);
  }

  public ClientsListAdapter(Context context, int listItemLayoutResourceId,
      List<Client> clients) {
    super(context, listItemLayoutResourceId, clients);
    mListItemLayoutResId = listItemLayoutResourceId;
  }

  @Override
  public android.view.View getView(int position, View convertView,
      ViewGroup parent) {

    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
        Context.LAYOUT_INFLATER_SERVICE);

    View listItemView = convertView;
    if (convertView == null) {
      listItemView = inflater.inflate(mListItemLayoutResId, parent, false);
    }

    ImageView imageView = (ImageView) listItemView
        .findViewById(R.id.img_clients);
    TextView title = (TextView) listItemView
        .findViewById(R.id.txt_clients_title);
    TextView subtitle = (TextView) listItemView
        .findViewById(R.id.txt_clients_subtitle);

    Client client = (Client) getItem(position);
    imageView.setImageResource((client.isOnline() ? R.drawable.ic_client_online
        : R.drawable.ic_client_offline));
    title.setText(client.getName());
    subtitle.setText(client.getIp() + "\t" + client.getMac());
    return listItemView;
  }
}
