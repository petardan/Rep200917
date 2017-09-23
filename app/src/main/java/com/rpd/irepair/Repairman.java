package com.rpd.irepair;

import java.util.ArrayList;

/**
 * Created by neotv on 9/22/17.
 */

public class Repairman {

    int Id;
    String firstName;
    String lastName;
    String email;
    String address;
    String mobilePhone1;
    String mobilePhone2;
    String description;
    double averageRating;
    ArrayList<Region> regions;
    ArrayList<Profession> professions;
    String imageUrl;

    public Repairman(int id, String firstName, String lastName, String email, String address, String mobilePhone1, String mobilePhone2, String description, double averageRating, ArrayList<Region> regions, ArrayList<Profession> professions, String imageUrl) {
        Id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.mobilePhone1 = mobilePhone1;
        this.mobilePhone2 = mobilePhone2;
        this.description = description;
        this.averageRating = averageRating;
        this.regions = regions;
        this.professions = professions;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public ArrayList<Region> getRegions() {
        return regions;
    }

    public void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }

    public ArrayList<Profession> getProfessions() {
        return professions;
    }

    public void setProfessions(ArrayList<Profession> professions) {
        this.professions = professions;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    @Override
    public String toString() {
        return "Repairman{" +
                "Id=" + Id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", mobilePhone1='" + mobilePhone1 + '\'' +
                ", mobilePhone2='" + mobilePhone2 + '\'' +
                ", description='" + description + '\'' +
                ", averageRating=" + averageRating +
                ", regions=" + regions +
                ", professions=" + professions +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
