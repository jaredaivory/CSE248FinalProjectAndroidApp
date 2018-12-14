package com.apps.jivory.googlemaps.viewmodels;

import android.app.Application;

import com.apps.jivory.googlemaps.arch.FirebaseLiveDataHelper;
import com.apps.jivory.googlemaps.arch.Repository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.apps.jivory.googlemaps.models.Post;
import com.google.firebase.database.DataSnapshot;

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

    public void writeNewUser(String firstname, String lastname, String emailaddress){
        repo.writeNewUser(firstname, lastname, emailaddress);
    }

    public void insertNewPost(Post post){
        repo.insertNewPost(post);
    }

    public void updateNewPost(Post post){
        repo.updatePost(post);
    }


    public LiveData<DataSnapshot> getUserData(){
        return firebaseUserData;
    }
    public LiveData<DataSnapshot> getPostData(){
        return firebasePostsData;
    }


    public void deletePost(String postID){
        repo.deletePost(postID);
    }
}
