package com.apps.jivory.googlemaps.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class LatLng{
    private Double latitude;
    private Double longitude;

    public LatLng(){
    }

    public LatLng(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LatLng{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
