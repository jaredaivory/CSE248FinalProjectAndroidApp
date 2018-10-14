package com.apps.jivory.googlemaps;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class oldLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private LoginViewModel loginViewModel;

    public static final String TAG = "oldLoginActivity";
    private static final int ERROR_DIALOG_REQUEST = 801;
    private static int SIGN_IN =1004;

    private GoogleSignInClient mGoogle;

    private FirebaseAuth mAuth;

    private com.google.android.gms.common.SignInButton googleButton;
    private EditText emailEditText, passwordEditText, confirm_email, confirm_password;
    private Button btnLogin;
    private TextView txtRegister, txtLogin;
    private CheckBox showPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login_layout);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        //initializes all view elements used in oldLoginActivity.Class
        initializeViews();
    }


    @Override
    protected void onStart(){
        super.onStart();
        isServicesOk();
    }


    public void createRegisterForm(){
        switch(confirm_email.getVisibility()){
            case View.VISIBLE:
                confirm_password.setVisibility(View.GONE);
                confirm_email.setVisibility(View.GONE);
                txtLogin.setVisibility(View.GONE);

                btnLogin.setText(R.string.login);
                txtRegister.setVisibility(View.VISIBLE);
                break;
            case View.GONE:
                confirm_password.setVisibility(View.VISIBLE);
                confirm_email.setVisibility(View.VISIBLE);
                txtLogin.setVisibility(View.VISIBLE);


                btnLogin.setText(R.string.register);
                txtRegister.setVisibility(View.GONE);
                break;
        }

    }


    public void loadMain(){
        Intent openMap = new Intent(this, MapsActivity.class);
        startActivity(openMap);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }


    public void loginUser(String email, String password){
        loginViewModel.loginUser(email,password);
    }


    //Methods used for google sign in manipulate and firebase
    private void googleSignIn() {
        Intent signInIntent = mGoogle.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(oldLoginActivity.this, "Logged in with google!", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.login_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public boolean isServicesOk(){
        Log.d(TAG, "isServicesOk: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(oldLoginActivity.this);

        if(available == ConnectionResult.SUCCESS){
            // everything is fine and user can make map requests
            Log.d(TAG, "isServicesOk: Google Play Services are working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // an error occured but we can resolve it
            Log.d(TAG, "isServicesOk: an error happened but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(oldLoginActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else{
            Toast.makeText(this, "You cannot make map requests", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "**Google Play Services are not working**", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void customButtonUI() {
        // TODO Auto-generated method stub

        SignInButton googleButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        for (int i = 0; i < googleButton.getChildCount(); i++) {
            View v = googleButton.getChildAt(i);

            if (v instanceof TextView)
            {
                TextView tv = (TextView) v;
                tv.setPadding(0, 0, 0, 0);

                return;
            }
        }
    }



    //
    // initialization methods
    //

    public void initializeViews(){

        btnLogin = findViewById(R.id.login_register_Btn);
        googleButton = findViewById(R.id.google_sign_in_button);
        txtRegister = findViewById(R.id.createAccount);
        txtLogin = findViewById(R.id.already_user);
        showPassword = findViewById(R.id.show_hide_password);

        confirm_email = findViewById(R.id.confirm_email_edit_text);
        confirm_password = findViewById(R.id.confirm_password_edit_text);



        //background animation
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.login_layout_background);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //stylizing
        //google sign in button styling
        SignInButton signInButton = findViewById(R.id.google_sign_in_button);
        signInButton.setSize(SignInButton.SIZE_ICON_ONLY);

        // onClick listener
        setOnClickListeners();

    }

    public void setOnClickListeners(){
        btnLogin.setOnClickListener(this);
        googleButton.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        txtLogin.setOnClickListener(this);
        showPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.already_user:
                createRegisterForm();
                break;
            case R.id.createAccount:
                createRegisterForm();
                break;
            case R.id.login_register_Btn:
                break;
            case R.id.google_sign_in_button:
                break;
            case R.id.show_hide_password:
                emailEditText = findViewById(R.id.email_edit_text);
                emailEditText.setText("Hello");
                break;
        }
    }

    // shows and hides password fields
    public void showPassword(View v) {
        if (((CheckBox)v).isChecked()) {
            confirm_password.setTransformationMethod(null);
            passwordEditText.setTransformationMethod(null);
        }else{
            passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
            confirm_password.setTransformationMethod(new PasswordTransformationMethod());
        }
    }

}
