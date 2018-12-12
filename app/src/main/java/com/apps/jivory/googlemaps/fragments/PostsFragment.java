package com.apps.jivory.googlemaps.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.jivory.googlemaps.R;
import com.apps.jivory.googlemaps.models.Post;
import com.apps.jivory.googlemaps.models.User;
import com.apps.jivory.googlemaps.viewmodels.MainViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class PostsFragment extends Fragment {
    public static final String TAG = "PostsFragment";

    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        initializeViews();

        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LiveData<DataSnapshot> postsData = mainViewModel.getPostData();
        postsData.observe(getActivity(), dataSnapshot -> {
            GenericTypeIndicator<HashMap<String, Post>> t = new GenericTypeIndicator<HashMap<String,Post>>() {};
            HashMap<String,Post> posts = dataSnapshot.getValue(t);
            Iterator it = posts.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry) it.next();
                Log.d(TAG, "onViewCreated: " + pair.getKey());
                it.remove();
            }
        });

    }

    private void initializeViews(){

    }
}
