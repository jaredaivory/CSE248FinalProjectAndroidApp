package com.apps.jivory.googlemaps.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String USER_ID;
    private String firstname;
    private String lastname;
    private String emailaddress;

    public User(){
    }

    public User(String firstname, String lastname, String email){
        this.emailaddress = email;
        this.firstname = firstname;
        this.lastname = lastname;
    }
    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }


    public String getFullname() {
        return firstname + " " + lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "User{" +
                "USER_ID='" + USER_ID + '\'' +
                ", fullname='" + firstname + " " + lastname + '\'' +
                ", emailaddress='" + emailaddress + '\'' +
                '}';
    }
}
