package com.deepmodi.firebaselearningphoneauth.Model;

public class User {

    private String UserName;
    private String UserNumber;
    private String UserPassword;

    public User() {
    }

    public User(String userName, String userNumber, String userPassword) {
        UserName = userName;
        UserNumber = userNumber;
        UserPassword = userPassword;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserNumber() {
        return UserNumber;
    }

    public void setUserNumber(String userNumber) {
        UserNumber = userNumber;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }
}
