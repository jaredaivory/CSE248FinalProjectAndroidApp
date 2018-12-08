package com.apps.jivory.googlemaps.viewmodels;

import android.app.Application;
import android.widget.TextView;

import com.apps.jivory.googlemaps.arch.Repository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import models.Post;

public class MainViewModel extends AndroidViewModel {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Repository repo;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        //Grabs instance of the repo
        repo = Repository.getInstance();
    }

    public void writeNewUser(String fullname, String emailaddress){
        repo.writeNewUser(fullname, emailaddress);
    }

    public void insertNewPost(Post post){
        repo.insertNewPost(post);
    }

}
