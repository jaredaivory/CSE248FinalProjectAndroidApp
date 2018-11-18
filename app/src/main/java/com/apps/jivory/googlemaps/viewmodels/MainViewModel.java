package com.apps.jivory.googlemaps.viewmodels;

import android.app.Application;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

public class MainViewModel extends AndroidViewModel {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

    }

    public String getEmail(){
        return mUser.getEmail();
    }




}
