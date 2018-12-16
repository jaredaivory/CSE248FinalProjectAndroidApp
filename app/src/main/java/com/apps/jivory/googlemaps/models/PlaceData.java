package com.apps.jivory.googlemaps.models;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class PlaceData {

    private String name = "No Name";
    private String address = "No Address";
    private String phonenumber = "No Phonenumber";
    private String placeID;
    private LatitudeLongitude latLng;

    public PlaceData(){

    }

    public PlaceData(String name, String address, String phonenumber, String placeID, LatLng latLng) {
        this.name = name;
        this.address = address;
        this.phonenumber = phonenumber;
        this.placeID = placeID;
        this.latLng = new LatitudeLongitude(latLng.latitude, latLng.longitude);
    }

    public PlaceData(Place place){
        this.name = place.getName().toString();
        this.address = Objects.requireNonNull(place.getAddress()).toString();
        this.phonenumber = Objects.requireNonNull(place.getPhoneNumber()).toString();
        this.placeID = place.getId();
        this.latLng = Objects.requireNonNull(new LatitudeLongitude(place.getLatLng().latitude,place.getLatLng().longitude));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public LatitudeLongitude getLatLng() {
        return latLng;
    }

    public void setLatLng(LatitudeLongitude latLng) {
        this.latLng = latLng;
    }

    @Override
    public String toString() {
        return "PlaceData{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", placeID='" + placeID + '\'' +
                ", latLng=" + latLng +
                '}';
    }
}
