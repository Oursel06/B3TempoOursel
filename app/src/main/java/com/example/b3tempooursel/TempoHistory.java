package com.example.b3tempooursel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TempoHistory {

    @SerializedName("dates")
    @Expose
    private List<TempoDate> dates = null;

    public List<TempoDate> getTempoDates() {
        return dates;
    }

    public void setDates(List<TempoDate> dates) {
        this.dates = dates;
    }
}
