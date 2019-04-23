
package com.example.travelmate.UberCab;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UberCab {

    @SerializedName("prices")
    @Expose
    private List<Price> prices = null;

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

}
