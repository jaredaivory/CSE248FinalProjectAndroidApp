package com.apps.jivory.googlemaps.observers;

import android.content.Context;

import com.apps.jivory.googlemaps.models.User;
import com.apps.jivory.googlemaps.models.UsersHashMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;

import androidx.lifecycle.Observer;

public class UsersObserver implements Observer<DataSnapshot> {
    private UsersListener usersListener;
    private static UsersHashMap usersHashMap = UsersHashMap.getInstance();

    public UsersObserver(Context context){
        if(context instanceof UsersListener){
            usersListener = (UsersListener) context;
        }
    }

    public interface UsersListener{
        void onUsersChanged(UsersHashMap users);
    }

    @Override
    public void onChanged(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<HashMap<String, User>> t = new GenericTypeIndicator<HashMap<String,User>>() {};
        HashMap<String, User> users = dataSnapshot.getValue(t);
        if(users == null){
            users = new HashMap<>();
        }
        usersHashMap.clear();
        usersHashMap.putAll(users);
        usersListener.onUsersChanged(usersHashMap);
        usersHashMap.notifyObservers();
    }
}
