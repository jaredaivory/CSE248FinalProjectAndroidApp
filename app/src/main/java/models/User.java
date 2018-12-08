package models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.UUID;

@IgnoreExtraProperties
public class User {
    private final String USER_ID = createUserID();
    private String fullname;
    private String emailaddress;
    private String password;

    private static String createUserID() {
        return UUID.randomUUID().toString();
    }

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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUSERID() {
        return USER_ID;
    }
}
