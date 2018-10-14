package models;

import java.util.UUID;

public class User {
    private final String USER_ID = getUserID();
    private String emailaddress;
    private String password;

    public static String getUserID() {
        return UUID.randomUUID().toString();
    }

    public User(String email, String pass){
        this.emailaddress = email;
        this.password = pass;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public String getPassword() {
        return password;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUSER_ID() {
        return USER_ID;
    }
}
