package com.android.app.weatherapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by nandi_000 on 02-04-2016.
 * helper class to make api call based on data connection check and location check
 * helper methods to show toast and show alert
 * interface for api call
 */
public class ApiCall {

    Context context;

    public ApiCall(Context context) {
        this.context = context;
    }

    public boolean canMakeApiCall() {
        return isOnline() && isLocationEnabled();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public boolean isLocationEnabled() {
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            return false;
        } else {
            return true;
        }
    }

    public void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
        dialog.setPositiveButton(context.getResources().getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(myIntent);
            }
        });
        dialog.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                //finish();
            }
        });
        dialog.show();
    }

    public interface MakeApiCall {
        void makeApiCall();
    }
}
