package com.itcraftsolution.raido.Models;

public class LoginDetails {
    private String userName, userImage, userEmail, userPhone, userGender;

    public LoginDetails(String userName, String userImage, String userEmail, String userPhone, String userGender) {
        this.userName = userName;
        this.userImage = userImage;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userGender = userGender;
    }

    public LoginDetails() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
}
