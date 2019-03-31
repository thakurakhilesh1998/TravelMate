package com.example.travelmate.FirebaseData;

public class FirebaseDataBean {

    String name,about,funfact;
 FirebaseDataBean(String name,String about,String funfact)
    {
        this.name=name;
        this.about=about;
        this.funfact=funfact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getFunfact() {
        return funfact;
    }

    public void setFunfact(String funfact) {
        this.funfact = funfact;
    }


}
