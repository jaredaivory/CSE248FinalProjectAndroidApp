package com.apps.jivory.googlemaps.models;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.UUID;

@IgnoreExtraProperties
public class Post {
    private final String POST_ID = createPostID();
    private String title;
    private String description;
    private LatLng latLng;
    private String creator;

    private static String createPostID() {
        return UUID.randomUUID().toString();
    }

    public Post(){
    }

    public Post(String title, LatLng latLng){
        this.title = title;
        this.latLng = latLng;

    }

    public String getPOST_ID() {
        return POST_ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Post{" +
                "POST_ID='" + POST_ID + '\'' +
                ", description='" + description + '\'' +
                ", latLng=" + latLng +
                ", creator='" + creator + '\'' +
                '}';
    }
}
