package com.example.travelmate.utility;

import java.util.ArrayList;
import java.util.HashMap;

public class UserProfileData {

    HashMap<String, String> interests;
    private String Email, Gender, Name, Phone, Profile;

    public UserProfileData() {

    }

    public UserProfileData(String email, String gender, String name, String phone, String profile, HashMap<String, String> interests) {
        Email = email;
        Gender = gender;
        Name = name;
        Phone = phone;
        Profile = profile;
        this.interests = interests;
    }


    public String getEmail() {
        return Email;
    }

    void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    void setGender(String gender) {
        Gender = gender;
    }

    public String getName() {
        return Name;
    }

    void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    void setPhone(String phone) {
        Phone = phone;
    }

    public String getProfile() {
        return Profile;
    }

    void setProfile(String profile) {
        Profile = profile;
    }

    public HashMap<String,String> getInterests() {
        return interests;
    }

    void setInterests(HashMap<String,String> interests) {
        this.interests = interests;
    }


}
