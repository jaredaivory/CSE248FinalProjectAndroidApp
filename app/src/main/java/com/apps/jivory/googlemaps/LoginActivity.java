package com.apps.jivory.googlemaps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int ERROR_DIALOG_REQUEST = 801;


    private FirebaseAuth mAuth;
    private FirebaseUser authUser;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        //check google services..
        isServicesOk();

        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        emailEditText.setText("exampleemail@provider.net");
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        passwordEditText.setText("123456");
    }

    public void login(View view){
        Toast.makeText(this, "Logging in..", Toast.LENGTH_SHORT).show();
        //Intent openMap = new Intent(this, MapsActivity.class);
        //startActivity(openMap);
        //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

        registerUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
    }




    public void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Sign in success, update UI with the signed-in user's information
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            Toast.makeText(LoginActivity.this, "Unsuccessful..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void registerUser(String email, String password){
        Toast.makeText(this,  email + " " + password, Toast.LENGTH_SHORT).show();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            authUser = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Success!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Unsuccessful..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean isServicesOk(){
        Log.d(TAG, "isServicesOk: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(LoginActivity.this);

        if(available == ConnectionResult.SUCCESS){
            // everything is fine and user can make map requests
            Log.d(TAG, "isServicesOk: Google Play Services are working");
            Toast.makeText(this, "Google Play Services are working", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // an error occured but we can resolve it
            Log.d(TAG, "isServicesOk: an error happened but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(LoginActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else{
            Toast.makeText(this, "You cannot make map requests", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "**Google Play Services are not working**", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
