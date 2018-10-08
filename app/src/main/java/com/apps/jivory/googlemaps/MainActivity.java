package com.apps.jivory.googlemaps;

import android.app.Dialog;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.*;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        startActivity(new Intent(this, MapsActivity.class));
    }

}
