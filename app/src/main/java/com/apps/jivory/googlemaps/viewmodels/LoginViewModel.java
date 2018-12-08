package com.apps.jivory.googlemaps.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.apps.jivory.googlemaps.activities.MainActivity;
import com.apps.jivory.googlemaps.R;
import com.apps.jivory.googlemaps.arch.Repository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginViewModel extends AndroidViewModel {
    private String TAG;
    private static final int ERROR_DIALOG_REQUEST = 801;
    private GoogleSignInClient mGoogle;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private Repository repo;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        TAG = application.getClass().toString();

        //initializing Firebase authorization
        mAuth = FirebaseAuth.getInstance();

        //configuring google sign-in to request default user data and email address
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(application.getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogle = GoogleSignIn.getClient(application, gso);

        // Grab Instance of Repository
        repo = Repository.getInstance();

        //check google services
        isServicesOk();
    }

    public void loginUser(String email, String password){
        if (isValid(email, "Email") && isValid(password, "Password")){
            try {
                repo.loginEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Sign in success, update UI with the signed-in user's information
                        if (task.isSuccessful()) {
                            repo.setFirebaseUser(mAuth.getCurrentUser());
                            Toast.makeText(getApplication(), "Success!!", Toast.LENGTH_SHORT).show();
                            getApplication().startActivity(new Intent(getApplication(), MainActivity.class));

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
            repo.registerEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        repo.setFirebaseUser(mAuth.getCurrentUser());
                        Toast.makeText(getApplication(), "Success!!", Toast.LENGTH_SHORT).show();
                    } else {
                        toastError("Registration Failed.");
                    }
                }
            });
        } else{
            toastError("Email/Passwords do not match");
        }
    }

    private boolean isServicesOk(){
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

    private void toastError(String error){
        Toast.makeText(getApplication(), "Error: " + error, Toast.LENGTH_SHORT).show();
    }

    private boolean isValid(String text, String name){
        if(text.trim().length() > 0){
            return true;
        }
        Toast.makeText(getApplication(), "Invalid " + name, Toast.LENGTH_SHORT).show();
        return false;
    }




}




