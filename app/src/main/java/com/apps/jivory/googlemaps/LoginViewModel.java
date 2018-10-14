package com.apps.jivory.googlemaps;

import android.app.Application;
import android.app.Dialog;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginViewModel extends AndroidViewModel {
    private String TAG;
    private static final int ERROR_DIALOG_REQUEST = 801;
    private GoogleSignInClient mGoogle;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        TAG = application.getClass().toString();

        //initializing Firebase authorization
        mAuth = FirebaseAuth.getInstance();

        //configuring google sign-in to request default user data and email address
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(application.getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogle = GoogleSignIn.getClient(application, gso);

        //check google services
        isServicesOk();
    }

    public void loginUser(String email, String password){
        if (isValid(email, "Email") && isValid(password, "Password")){
            try {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Sign in success, update UI with the signed-in user's information
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplication(), "Success!!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplication(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (IllegalArgumentException e){

            }
        }

    }

    public void registerUser(String email, String confirm_email, String password, String confirm_password){
        if(email.equals(confirm_email) && password.equals(confirm_password)){
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        //authUser = mAuth.getCurrentUser();
                        Toast.makeText(getApplication(), "Success!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), "Unsuccessful..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else{
            Toast.makeText(getApplication(), "Email/Passwords do not match" , Toast.LENGTH_SHORT).show();
        }


    }


    public boolean isServicesOk(){
        Log.d(TAG, "isServicesOk: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplication());

        if(available == ConnectionResult.SUCCESS){
            // everything is fine and user can make map requests
            Log.d(TAG, "isServicesOk: Google Play Services are working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // an error occured but we can resolve it
            Log.d(TAG, "isServicesOk: an error happened but we can fix it");
            Toast.makeText(getApplication(), "Fixable Error: Maps aren't working", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(getApplication(), "You cannot make map requests", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplication(), "**Google Play Services are not working**", Toast.LENGTH_SHORT).show();
        }
        return false;
    }





    public boolean isValid(String text, String name){
        if(text.trim().length() > 0){

            return true;
        }
        Toast.makeText(getApplication(), "Invalid " + name, Toast.LENGTH_SHORT).show();
        return false;
    }




}




