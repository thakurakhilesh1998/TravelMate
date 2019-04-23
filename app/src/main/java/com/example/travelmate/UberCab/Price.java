
package com.example.travelmate.UberCab;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Price {

    @SerializedName("localized_display_name")
    @Expose
    private String localizedDisplayName;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("high_estimate")
    @Expose
    private Integer highEstimate;
    @SerializedName("surge_multiplier")
    @Expose
    private Double surgeMultiplier;
    @SerializedName("minimum")
    @Expose
    private Integer minimum;
    @SerializedName("low_estimate")
    @Expose
    private Integer lowEstimate;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("estimate")
    @Expose
    private String estimate;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;

    public String getLocalizedDisplayName() {
        return localizedDisplayName;
    }

    public void setLocalizedDisplayName(String localizedDisplayName) {
        this.localizedDisplayName = localizedDisplayName;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getHighEstimate() {
        return highEstimate;
    }

    public void setHighEstimate(Integer highEstimate) {
        this.highEstimate = highEstimate;
    }

    public Double getSurgeMultiplier() {
        return surgeMultiplier;
    }

    public void setSurgeMultiplier(Double surgeMultiplier) {
        this.surgeMultiplier = surgeMultiplier;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public Integer getLowEstimate() {
        return lowEstimate;
    }

    public void setLowEstimate(Integer lowEstimate) {
        this.lowEstimate = lowEstimate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getEstimate() {
        return estimate;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}
