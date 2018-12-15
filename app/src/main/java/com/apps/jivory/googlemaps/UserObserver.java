package com.apps.jivory.googlemaps;

import android.content.Context;

import com.apps.jivory.googlemaps.models.User;
import com.google.firebase.database.DataSnapshot;

import androidx.lifecycle.Observer;

public class UserObserver implements Observer<DataSnapshot> {
    private UserListener userListener;

    public UserObserver(Context context){
        if(context instanceof UserListener){
            userListener = (UserListener) context;
        }
    }

    public interface UserListener{
        void onUserChanged(User user);
    }

    @Override
    public void onChanged(DataSnapshot dataSnapshot) {
        User currentUser = dataSnapshot.getValue(User.class);
        userListener.onUserChanged(currentUser);
    }
}
