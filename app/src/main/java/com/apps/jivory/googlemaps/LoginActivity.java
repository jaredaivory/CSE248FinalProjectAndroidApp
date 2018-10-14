package com.apps.jivory.googlemaps;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private LoginViewModel loginViewModel;


    private com.google.android.gms.common.SignInButton googleButton;
    private EditText email_edit_text, password_edit_text, confirm_email_edit_text, confirm_password_edit_text;
    private Button btnLogin;
    private TextView txtRegister, txtLogin;
    private CheckBox showPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        initializeViews();
    }

    public void register(){
        loginViewModel.registerUser(email_edit_text.getText().toString(),
                confirm_email_edit_text.getText().toString(),
                password_edit_text.getText().toString(),
                confirm_password_edit_text.getText().toString());
    }

    public void login(){
        loginViewModel.loginUser(email_edit_text.getText().toString(),password_edit_text.getText().toString());
    }



    public void initializeViews(){

        btnLogin = findViewById(R.id.login_register_Btn);
        googleButton = findViewById(R.id.google_sign_in_button);
        txtRegister = findViewById(R.id.createAccount);
        txtLogin = findViewById(R.id.already_user);
        showPassword = findViewById(R.id.show_hide_password);
        email_edit_text = findViewById(R.id.email_edit_text);
        password_edit_text = findViewById(R.id.password_edit_text);

        confirm_email_edit_text = findViewById(R.id.confirm_email_edit_text);
        confirm_password_edit_text = findViewById(R.id.confirm_password_edit_text);



        //background animation
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.login_layout_background);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //stylizing
        //google sign in button styling
        googleButton.setSize(SignInButton.SIZE_ICON_ONLY);

        // onClick listener
        setOnClickListeners();

    }

    public void setOnClickListeners(){
        btnLogin.setOnClickListener(this);
        googleButton.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        txtLogin.setOnClickListener(this);
        showPassword.setOnClickListener(this);

        // shows and hides password fields
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    confirm_password_edit_text.setTransformationMethod(null);
                    password_edit_text.setTransformationMethod(null);
                } else{
                    password_edit_text.setTransformationMethod(new PasswordTransformationMethod());
                    confirm_password_edit_text.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

    }

    public void createRegisterForm(){
        switch(confirm_email_edit_text.getVisibility()){
            case View.VISIBLE:
                confirm_password_edit_text.setVisibility(View.GONE);
                confirm_email_edit_text.setVisibility(View.GONE);
                txtLogin.setVisibility(View.GONE);

                btnLogin.setText(R.string.login);
                txtRegister.setVisibility(View.VISIBLE);
                break;
            case View.GONE:
                confirm_password_edit_text.setVisibility(View.VISIBLE);
                confirm_email_edit_text.setVisibility(View.VISIBLE);
                txtLogin.setVisibility(View.VISIBLE);


                btnLogin.setText(R.string.register);
                txtRegister.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_register_Btn:
                if (confirm_email_edit_text.getVisibility() == View.VISIBLE){
                    register();
                } else {
                    login();
                }
                break;
            case R.id.createAccount:
                createRegisterForm();
                break;
            case R.id.already_user:
                createRegisterForm();
                break;
        }
    }
}
