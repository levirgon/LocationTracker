package com.levirgon.tutexplocation.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Geometry {

    @SerializedName("location")
    private PlaceLocation mPlaceLocation;

    public void setPlaceLocation(PlaceLocation placeLocation) {
        this.mPlaceLocation = placeLocation;
    }

    public PlaceLocation getPlaceLocation() {
        return mPlaceLocation;
    }

    @Override
    public String toString() {
        return
                "Geometry{" +
                        "mPlaceLocation = '" + mPlaceLocation + '\'' +
                        "}";
    }
}