package com.example.travelmate.utility;

import android.net.Uri;

public class SaveRating {


    Float rating;
    String review;
    String displayName;
    String photoUrl;

    String reviewtitle;



    public SaveRating(Float rating, String review, String displayName,String photoUrl,String reviewtitle) {
        this.rating = rating;
        this.review = review;
        this.displayName = displayName;
        this.photoUrl=photoUrl;
        this.reviewtitle=reviewtitle;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public String getReviewtitle() {
        return reviewtitle;
    }

    public void setReviewtitle(String reviewtitle) {
        this.reviewtitle = reviewtitle;
    }


}
