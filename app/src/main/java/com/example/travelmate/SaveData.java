package com.example.travelmate;

class SaveData {


    String Name, Phone, Email, Gender,Age;
    String Profile;
    String Name1;
    String interest1, interest6, interest2, interest3, interest4, interest5;

    SaveData(String Interest1, String Interest2, String Interest3, String Interest4, String Interest5, String Interest6) {
        this.interest1 = Interest1;
        this.interest2 = Interest2;
        this.interest3 = Interest3;
        this.interest4 = Interest4;
        this.interest5 = Interest5;
        this.interest6 = Interest6;
    }

    SaveData(String name, String phone, String email, String gender, String profile, String age, String name1) {
        Name = name;
        Phone = phone;
        Email = email;
        Gender = gender;
        Profile = profile;
        Age = age;
        Name1 = name1;
    }




    public String getInterest1() {
        return interest1;
    }

    public void setInterest1(String interest1) {
        this.interest1 = interest1;
    }

    public String getInterest6() {
        return interest6;
    }

    public void setInterest6(String interest6) {
        this.interest6 = interest6;
    }

    public String getInterest2() {
        return interest2;
    }

    public void setInterest2(String interest2) {
        this.interest2 = interest2;
    }

    public String getInterest3() {
        return interest3;
    }

    public void setInterest3(String interest3) {
        this.interest3 = interest3;
    }

    public String getInterest4() {
        return interest4;
    }

    public void setInterest4(String interest4) {
        this.interest4 = interest4;
    }

    public String getInterest5() {
        return interest5;
    }

    public void setInterest5(String interest5) {
        this.interest5 = interest5;
    }


}
