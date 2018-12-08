package com.apps.jivory.googlemaps.arch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import androidx.lifecycle.LiveData;

/**
 * Since the goal is to observe the datasnapshot I retrieve from the firebase database ref
 * when u add an listener I'll use a helper that helps remove a ton of code
 */
public class FirebaseLiveDataHelper extends LiveData<DataSnapshot> {

    public FirebaseLiveDataHelper(Query query){

    }

}
