package com.apps.jivory.googlemaps.observers;

import android.content.Context;
import android.util.Log;

import com.apps.jivory.googlemaps.models.Post;
import com.apps.jivory.googlemaps.models.PostHashMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;

import androidx.lifecycle.Observer;

public class PostsObserver implements Observer<DataSnapshot> {
    private PostsListener postsListener;
    private static PostHashMap postHashMap = PostHashMap.getInstance();

    public static final String TAG = "PostObserver";

    public PostsObserver(Context context) {
        if (context instanceof PostsListener) {
            postsListener = (PostsListener) context;
        }
    }

    public interface PostsListener {
        void onPostsChanged(PostHashMap postHashMap);
    }

    @Override
    public void onChanged(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<HashMap<String, Post>> t = new GenericTypeIndicator<HashMap<String, Post>>() {
        };

        HashMap<String, Post> posts = dataSnapshot.getValue(t);
        if(posts == null){
            posts = new HashMap<>();
        }
        postHashMap.clear();
        postHashMap.putAll(posts);
        postsListener.onPostsChanged(postHashMap);
        postHashMap.notifyObservers();


    }

}
