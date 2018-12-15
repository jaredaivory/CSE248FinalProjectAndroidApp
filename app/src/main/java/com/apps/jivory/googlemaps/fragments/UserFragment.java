package com.apps.jivory.googlemaps.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.apps.jivory.googlemaps.R;
import com.apps.jivory.googlemaps.models.CurrentUser;
import com.apps.jivory.googlemaps.models.User;
import com.apps.jivory.googlemaps.observers.FirebaseObservable;
import com.apps.jivory.googlemaps.observers.FirebaseObserver;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserFragment extends Fragment implements DatePickerDialog.OnDateSetListener, FirebaseObserver {
    public static final String TAG = "UserFragment";

    private UserFragmentListener listener;
    private View view;

    private CurrentUser currentUserHolder;
    private User currentUser;

    private EditText editTextDOB, editTextFirstName, editTextLastName, editTextEmailAddress;
    private Date date;


    public interface UserFragmentListener{
        void onUserSave(User user);
    }

    public UserFragment(){
    }


    @SuppressLint("ValidFragment")
    public UserFragment(CurrentUser currentUserHolder){
        this.currentUserHolder = currentUserHolder;
        this.currentUser = currentUserHolder.getUser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_user, container, false);
        init();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof UserFragmentListener){
            listener = (UserFragmentListener) context;
        }
        currentUserHolder.registerObserver(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refresh();
    }

    private void init(){
        editTextDOB = view.findViewById(R.id.editText_DateOfBirth);
        editTextFirstName = view.findViewById(R.id.editText_FirstName);
        editTextLastName = view.findViewById(R.id.editText_LastName);
        editTextEmailAddress = view.findViewById(R.id.editText_EmailAddress);
        Button btnSaveUser = view.findViewById(R.id.button_SaveUser);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePicker = new DatePickerDialog(view.getContext(), this,year,month,day);
        editTextDOB.setOnClickListener(v1 -> {
            datePicker.show();
        });

        btnSaveUser.setOnClickListener(e -> {
            Log.d(TAG, "Save User: ");
            listener.onUserSave(updateUser());
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        currentUserHolder.removeObserver(this);
    }

    private void refresh(){
        editTextFirstName.setText(currentUser.getFirstname());
        editTextLastName.setText(currentUser.getLastname());
        editTextEmailAddress.setText(currentUser.getEmailaddress());
    }

    private User updateUser(){
        String firtname = editTextFirstName.getText().toString();
        String lastname = editTextLastName.getText().toString();

        currentUser.setFirstname(firtname);
        currentUser.setLastname(lastname);
        currentUser.setDateofbirth(date);

        return currentUser;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String date = DateFormat.getDateInstance().format(calendar.getTime());
        this.date = calendar.getTime();
        editTextDOB.setText(date);
    }


    @Override
    public void onChanged() {
        this.currentUser = currentUserHolder.getUser();
        Log.d(TAG, " onChanged:" + currentUser.toString());
        refresh();
    }
}
