package com.shopnosoft.androidgpstrackingactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AndroidGPSTrackingActivity extends AppCompatActivity {

    private WifiManager wfm;
    private ConnectivityManager cntm;

    // GPSTracker class
    GPSTracker gps;
    Button btnShowLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_gpstracking);

        if(!network()){
            createNetErrorDialog();
        }
        else{
            Location();
        }

//////////////////////////<<-------------------------BUTTON CLICK LOCATION VIEW--------------------->>//////////////////////////////////

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location();
            }
        });
//////////////////////////<<-------------------------BUTTON CLICK LOCATION VIEW Ends--------------------->>//////////////////////////////////
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!network()){
            createNetErrorDialog();
        }
        else{
            Location();
        }
    }
    ////////<<-------------------------------CHECKING NETWORK CONNECTION STATUS------------------------->>///////////////////////


    public boolean network() {

//Get the wifi service of device
        wfm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
//Get the Mobile Dtata service of device
        cntm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo nInfo = cntm.getActiveNetworkInfo();

        if ((wfm.isWifiEnabled()) || (nInfo != null && nInfo.isConnected())) {
            return true;
        }

        else{
            return false;
        }

    }


////////<<-------------------------------CHECKING NETWORK CONNECTION STATUS ENDS-------------------->>///////////////////////





///////<<-------------------------------CREATING ERROR DIALOG--------------------------------------->>////////////////////////

protected void createNetErrorDialog() {


    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("You need a network connection to use this application. Please turn on mobile network or Wi-Fi in Settings.")
            .setTitle("Unable to connect")
            .setCancelable(false)
            .setPositiveButton("Enable",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(Settings.ACTION_SETTINGS);

                            //startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));

                            startActivity(i);
                            //finish();
                        }
                    }
            )  .setNegativeButton("Cancel",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            }
    )

    ;
    AlertDialog alert = builder.create();
    alert.show();
}

///////<<-------------------------------CREATING ERROR DIALOG ENDS--------------------------------------->>///////////////////

///////<<-------------------------------- SHOWING LOCATION CO-ORDINATES  --------------------------->>////////////////////////
    public void Location(){

        gps = new GPSTracker(AndroidGPSTrackingActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
///////<<-------------------------------- SHOWING LOCATION CO-ORDINATES  ENDS--------------------------->>////////////////////////








}