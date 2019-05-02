package com.example.travelmate.utility;

public class SaveRating {


    Float rating;
    String review;
    String displayName;

    public SaveRating(Float rating, String review, String displayName) {
        this.rating = rating;
        this.review = review;
        this.displayName = displayName;
    }

    public SaveRating() {

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }


}
