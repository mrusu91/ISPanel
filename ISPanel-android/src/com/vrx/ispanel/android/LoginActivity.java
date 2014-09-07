package com.vrx.ispanel.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

  EditText etAddress, etPort, etUsername, etPassword;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (ISPanelApp.remoteAccessClient != null
        && ISPanelApp.remoteAccessClient.isConnected())
      showPanel();
    setContentView(R.layout.activity_login);
    etAddress = (EditText) findViewById(R.id.et_login_address);
    etPort = (EditText) findViewById(R.id.et_login_port);
    etUsername = (EditText) findViewById(R.id.et_login_username);
    etPassword = (EditText) findViewById(R.id.et_login_password);
    findViewById(R.id.btn_login_connect).setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.btn_login_connect)
      new ConnectTask().execute();
  }

  private void showPanel() {
    startActivity(new Intent(this, PanelActivity.class));
    finish();
  }

  private class ConnectTask extends AsyncTask<Void, Integer, String> {
    ProgressDialog progress;

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progress = new ProgressDialog(LoginActivity.this);
      progress.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      super.onProgressUpdate(values);
      switch (values[0]) {
      case 0:
        progress.setMessage(getResources().getString(R.string.check_input));
        break;
      case 1:
        progress.setMessage(getResources().getString(R.string.try_to_connect));
        break;
      case 2:
        progress.setMessage(getResources().getString(R.string.try_to_auth));
        break;
      default:
        break;
      }
    }

    @Override
    protected String doInBackground(Void... params) {
      int inputError = checkInput();
      if (inputError == 0) {
        if (connect()) {
          if (auth()) {
            return "OK";
          } else {
            return getResources().getString(R.string.auth_error);
          }
        } else {
          return getResources().getString(R.string.connection_error);
        }
      } else {
        return getResources().getString(inputError);
      }
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      progress.dismiss();
      if (result.contains("OK"))
        showPanel();
      else {
        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
      }
    }

    /**
     * Check input
     * 
     * @return 0 if input is good, otherwise return the id of the string error
     */
    private int checkInput() {
      setProgress(0);
      if (etAddress.getText().length() == 0) {
        return R.string.invalid_address;
      }
      if (etPort.getText().length() == 0
          || Integer.parseInt(etPort.getText().toString()) > 65534) {
        return R.string.invalid_port;
      }
      if (etUsername.getText().length() == 0) {
        return R.string.invalid_username;
      }
      if (etPassword.getText().length() == 0) {
        return R.string.invalid_password;
      }
      return 0;
    }

    private boolean connect() {
      setProgress(1);
      ISPanelApp.remoteAccessClient = new RemoteAccessClient(etAddress
          .getText().toString(), Integer.valueOf(etPort.getText().toString()));
      return ISPanelApp.remoteAccessClient.connect();
    }

    private boolean auth() {
      setProgress(2);
      return ISPanelApp.remoteAccessClient.auth(
          etUsername.getText().toString(), etPassword.getText().toString());
    }
  }

}
