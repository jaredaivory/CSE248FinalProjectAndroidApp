package com.apps.jivory.googlemaps.arch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Repository {
    static Repository INSTANCE = null;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private Repository(){
        mAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> loginEmailAndPassword(String email, String password){
        return mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {

            }
        });
    }

    public Task<AuthResult> registerEmailAndPassword(String email, String password){
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public void setFirebaseUser(FirebaseUser user){
        this.mUser = user;
    }

    public static Repository getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Repository();
        }
        return INSTANCE;
    }
}
