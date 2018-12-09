package com.apps.jivory.googlemaps.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.UUID;

@IgnoreExtraProperties
public class User {
    private String USER_ID;
    private String fullname;
    private String emailaddress;

    public User(){
    }

    public User(String fullname, String email){
        this.emailaddress = email;
        this.fullname = fullname;
    }
    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }


    public String getUSERID() {
        return USER_ID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setUSER_ID(String user_id){
        this.USER_ID = user_id;
    }


    @Override
    public String toString() {
        return "User{" +
                "USER_ID='" + USER_ID + '\'' +
                ", fullname='" + fullname + '\'' +
                ", emailaddress='" + emailaddress + '\'' +
                '}';
    }
}
