package com.apps.jivory.googlemaps.models;

import com.apps.jivory.googlemaps.observers.FirebaseObservable;
import com.apps.jivory.googlemaps.observers.FirebaseObserver;

import java.util.ArrayList;
import java.util.HashMap;

public class UsersHashMap extends HashMap<String, User> implements FirebaseObservable {
    public static UsersHashMap INSTANCE;
    private ArrayList<FirebaseObserver> observers;

    private UsersHashMap(){
        observers = new ArrayList<>();
    }

    public static UsersHashMap getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UsersHashMap();
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
    public void removeAllOvservers() {
        observers.clear();
    }

    @Override
    public void notifyObservers() {
        for(FirebaseObserver o: observers){
            o.onChanged();
        }
    }
}
