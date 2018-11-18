package models;

import java.util.UUID;

public class User {
    private final String USER_ID = createUserID();
    private String emailaddress;
    private String password;

    private static String createUserID() {
        return UUID.randomUUID().toString();
    }

    public User(String email, String pass){
        this.emailaddress = email;
        this.password = pass;
    }

    public String getEmailAddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUSERID() {
        return USER_ID;
    }
}
