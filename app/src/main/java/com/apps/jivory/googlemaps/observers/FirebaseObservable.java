package com.apps.jivory.googlemaps.observers;

public interface FirebaseObservable {
    void registerObserver(FirebaseObserver observer);
    void removeObserver(FirebaseObserver observer);
    void removeAllOvservers();
    void notifyObservers();

}
