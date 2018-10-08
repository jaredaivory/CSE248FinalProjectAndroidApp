package com.apps.jivory.googlemaps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int ERROR_DIALOG_REQUEST = 801;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        //check google services..
        isServicesOk();

    }

    public void login(View view){
        Toast.makeText(this, "Logging in..", Toast.LENGTH_SHORT).show();
        Intent openMap = new Intent(this, MapsActivity.class);
        startActivity(openMap);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public boolean isServicesOk(){
        Log.d(TAG, "isServicesOk: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(LoginActivity.this);

        if(available == ConnectionResult.SUCCESS){
            // everything is fine and user can make map requests
            Log.d(TAG, "isServicesOk: Google Play Services are working");
            Toast.makeText(this, "Google Play Services are working", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // an error occured but we can resolve it
            Log.d(TAG, "isServicesOk: an error happened but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(LoginActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else{
            Toast.makeText(this, "You cannot make map requests", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "**Google Play Services are not working**", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
