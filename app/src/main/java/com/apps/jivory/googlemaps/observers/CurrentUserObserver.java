package com.apps.jivory.googlemaps.observers;

import android.content.Context;
import android.util.Log;

import com.apps.jivory.googlemaps.models.CurrentUser;
import com.apps.jivory.googlemaps.models.User;
import com.google.firebase.database.DataSnapshot;

import androidx.lifecycle.Observer;

public class CurrentUserObserver implements Observer<DataSnapshot> {
    private UserListener userListener;
    private static CurrentUser currentUser = CurrentUser.getInstance();

    public CurrentUserObserver(Context context){
        if(context instanceof UserListener){
            userListener = (UserListener) context;
        }
    }

    public interface UserListener{
        void onCurrentUserChanged(CurrentUser user);
    }

    @Override
    public void onChanged(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        currentUser.setUser(user);
        userListener.onCurrentUserChanged(currentUser);
        currentUser.notifyObservers();
    }
}
