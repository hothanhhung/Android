package com.hth.filestransfer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hth.utils.DataSevices;
import com.hth.webserver.AndroidWebServer;

public class MainActivity extends AppCompatActivity {
    private AndroidWebServer androidWebServer = null;
    private Button startButton;
    private EditText txtURL;
    private TextView lblGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!this.isOnline())
        {
            this.showAlertErrorNoInternet(this, true);
            return;
        }

        startButton = (Button) findViewById(R.id.btStartServer);
        txtURL = (EditText) findViewById(R.id.txtURL);
        lblGuide = (TextView) findViewById(R.id.lblGuide);

        if(androidWebServer == null)
            androidWebServer = new AndroidWebServer();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(androidWebServer == null)
                    androidWebServer = new AndroidWebServer();
                if(androidWebServer.isAlive())
                {
                    androidWebServer.stop();
                }else{
                    try{
                        androidWebServer.startAndTryPort(getLastPort(), getParent());
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                updateUI();
            }
        });
        updateUI();
    }

    private void updateUI()
    {
        if(androidWebServer.isAlive())
        {
            txtURL.setText(getIP()+":"+androidWebServer.getListeningPort());
            lblGuide.setVisibility(View.VISIBLE);
            txtURL.setVisibility(View.VISIBLE);
            startButton.setText(R.string.stop);
            DataSevices.saveLastPort(this, androidWebServer.getListeningPort());
        }else{
            txtURL.setText("");
            lblGuide.setVisibility(View.INVISIBLE);
            txtURL.setVisibility(View.INVISIBLE);
            startButton.setText(R.string.start);
        }
    }

    public int getLastPort()
    {
       return DataSevices.getLastPort(this);
    }

    public String getIP()
    {
        WifiManager wm = (WifiManager) this.getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }


    private Boolean isOnline()
    {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnected();

    }

    public static AlertDialog showAlertErrorNoInternet(final Activity activity, final Boolean isCloseThis)
    {
        AlertDialog alertDialog = new android.app.AlertDialog.Builder(activity)
                .setTitle(R.string.error_title)
                .setMessage(R.string.error_internet)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if(isCloseThis) activity.finish();
                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
        return alertDialog;
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

    }
}
