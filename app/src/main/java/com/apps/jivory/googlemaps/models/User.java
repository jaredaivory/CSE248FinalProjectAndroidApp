package com.apps.jivory.googlemaps.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.Set;

@IgnoreExtraProperties
public class User {
    private String USER_ID;
    private String firstname;
    private String lastname;
    private String emailaddress;
    private Set<User> friends;
    private Date dateofbirth;

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

    public Set<User> getFriends() {
        return friends;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public Date getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(Date dateofbirth) {
        this.dateofbirth = dateofbirth;
    }


    @Override
    public String toString() {
        return "User{" +
                "USER_ID='" + USER_ID + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", emailaddress='" + emailaddress + '\'' +
                ", friends=" + friends +
                ", dateofbirth=" + dateofbirth +
                '}';
    }
}
