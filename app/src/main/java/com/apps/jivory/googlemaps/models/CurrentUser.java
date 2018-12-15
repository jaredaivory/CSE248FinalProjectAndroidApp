package com.apps.jivory.googlemaps.models;

import com.apps.jivory.googlemaps.observers.FirebaseObservable;
import com.apps.jivory.googlemaps.observers.FirebaseObserver;

import java.util.ArrayList;

public class CurrentUser implements FirebaseObservable {
    private static CurrentUser INSTANCE;
    private ArrayList<FirebaseObserver> observers;
    private static User user;

    private CurrentUser(){
        observers = new ArrayList<>();
    }

    public User getUser(){
        return CurrentUser.user;
    }

    public void setUser(User user){
        CurrentUser.user = user;
    }

    public static CurrentUser getInstance(){
        if(INSTANCE==null){
            INSTANCE = new CurrentUser();
        }
        return INSTANCE;
    }

    @Override
    public void registerObserver(FirebaseObserver observer) {
        if(!observers.contains(observer)){
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(FirebaseObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(FirebaseObserver o: observers){
            o.onChanged();
        }
    }


}
