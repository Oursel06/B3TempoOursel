package com.example.b3tempooursel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TempoDaysColor {

    @SerializedName("couleurJourJ")
    @Expose
    private String couleurJourJ;
    @SerializedName("couleurJourJ1")
    @Expose
    private String couleurJourJ1;

    public String getCouleurJourJ() {
        return couleurJourJ;
    }

    public void setCouleurJourJ(String couleurJourJ) {
        this.couleurJourJ = couleurJourJ;
    }

    public String getCouleurJourJ1() {
        return couleurJourJ1;
    }

    public void setCouleurJourJ1(String couleurJourJ1) {
        this.couleurJourJ1 = couleurJourJ1;
    }

}