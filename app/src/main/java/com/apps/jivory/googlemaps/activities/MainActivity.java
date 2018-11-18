package com.apps.jivory.googlemaps.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.content.*;
import android.widget.TextView;

import com.apps.jivory.googlemaps.MapsActivity;
import com.apps.jivory.googlemaps.R;
import com.apps.jivory.googlemaps.viewmodels.LoginViewModel;
import com.apps.jivory.googlemaps.viewmodels.MainViewModel;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        greeting();
    }

    public void greeting(){
        TextView textView = findViewById(R.id.textview_hello);
        textView.setText(mainViewModel.getEmail());
    }

}
