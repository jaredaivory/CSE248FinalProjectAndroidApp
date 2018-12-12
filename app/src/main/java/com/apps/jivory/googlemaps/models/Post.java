package com.apps.jivory.googlemaps.models;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.UUID;

@IgnoreExtraProperties
public class Post {
    private final String POST_ID = createPostID();
    private String title;
    private String description;
    private LatitudeLongitude latitudeLongitude;
    private String creator;

    private static String createPostID() {
        return UUID.randomUUID().toString();
    }

    public Post(){
    }

    public Post(String title, LatitudeLongitude latitudeLongitude){
        this.title = title;
        this.latitudeLongitude = latitudeLongitude;

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

    public LatitudeLongitude getLatitudeLongitude() {
        return latitudeLongitude;
    }

    public void setLatitudeLongitude(LatitudeLongitude latitudeLongitude) {
        this.latitudeLongitude = latitudeLongitude;
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
                ", latitudeLongitude=" + latitudeLongitude +
                ", creator='" + creator + '\'' +
                '}';
    }
}
