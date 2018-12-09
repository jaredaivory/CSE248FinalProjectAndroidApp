package com.apps.jivory.googlemaps.arch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.lifecycle.LiveData;

/**
 * Since the goal is to observe the data snapshot  I retrieve from the firebase database ref
 * when u add an listener I'll use a helper that helps remove a ton of code
 *
 *
 */
public class FirebaseLiveDataHelper extends LiveData<DataSnapshot> {
    private Query query;

    public FirebaseLiveDataHelper(DatabaseReference reference){
        this.query = reference;
    }


    /**
     * These methods overrides from livedata components
     * They are responsible for maintaining components are focused on only when activity's view model
     * is on screen.
     */
    @Override
    protected void onActive() {
        super.onActive();
        query.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        query.addValueEventListener(listener);
    }

    /**
     * Sets the value of the livedata as the data snapshot
     */
    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


}
