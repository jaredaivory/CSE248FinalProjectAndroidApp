package models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.UUID;

@IgnoreExtraProperties
public class Post {
    private final String POST_ID = createPostID();
    private String description;
    private LatLng latLng;

    private static String createPostID() {
        return UUID.randomUUID().toString();
    }

    public Post(){

    }

    public Post(String description, LatLng latLng){
        this.description = description;
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
}
