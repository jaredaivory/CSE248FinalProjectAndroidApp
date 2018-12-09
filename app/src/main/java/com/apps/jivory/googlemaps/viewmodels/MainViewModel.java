package com.apps.jivory.googlemaps.viewmodels;

import android.app.Application;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.apps.jivory.googlemaps.activities.MainActivity;
import com.apps.jivory.googlemaps.arch.FirebaseLiveDataHelper;
import com.apps.jivory.googlemaps.arch.Repository;
import com.apps.jivory.googlemaps.models.LatLng;
import com.apps.jivory.googlemaps.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.apps.jivory.googlemaps.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainViewModel extends AndroidViewModel {
    public static final String TAG  = "MainViewModel";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Repository repo;
    private FirebaseLiveDataHelper firebaseUserData;
    private FirebaseLiveDataHelper firebasePostsData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        //Grabs instance of the repo
        repo = Repository.getInstance();

        firebaseUserData = new FirebaseLiveDataHelper(repo.getUserReference());
        firebasePostsData = new FirebaseLiveDataHelper(repo.getPostsReference());
    }

    public void writeNewUser(String fullname, String emailaddress){
        repo.writeNewUser(fullname, emailaddress);
    }

    public void insertNewPost(Post post){
        repo.insertNewPost(post);
    }


    public LiveData<DataSnapshot> getUserData(){
        return firebaseUserData;
    }
    public LiveData<DataSnapshot> getPostData(){
        return firebasePostsData;
    }

}
