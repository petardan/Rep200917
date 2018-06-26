package com.rpd.customClasses;

import java.io.Serializable;

/**
 * Created by Petar on 11/15/2017.
 */

public class User implements Serializable {
    String userID;
    String userName;
    String userSurname;
    String email;
    String address;
    String mobilePhone1;
    String mobilePhone2;
    String status;  // 1-active; 2-suspended; 3-terminated
    String profilePictureURL;

    public User() {
    }

    public User(String userID, String userName, String userSurname, String email, String address, String mobilePhone1, String mobilePhone2, String status, String profilePictureURL) {
        this.userID = userID;
        this.userName = userName;
        this.userSurname = userSurname;
        this.email = email;
        this.address = address;
        this.mobilePhone1 = mobilePhone1;
        this.mobilePhone2 = mobilePhone2;
        this.status = status;
        this.profilePictureURL = profilePictureURL;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobilePhone1() {
        return mobilePhone1;
    }

    public void setMobilePhone1(String mobilePhone1) {
        this.mobilePhone1 = mobilePhone1;
    }

    public String getMobilePhone2() {
        return mobilePhone2;
    }

    public void setMobilePhone2(String mobilePhone2) {
        this.mobilePhone2 = mobilePhone2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }
}
