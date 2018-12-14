package com.apps.jivory.googlemaps.models;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@IgnoreExtraProperties
public class Post {
    private String POST_ID;
    private String title;
    private String description;
    private LatitudeLongitude latitudeLongitude;
    private String creator;
    private List<String> participants;
    private int maxParticipants;

    public static String createPostID() {
        return UUID.randomUUID().toString();
    }

    public Post(){
    }

    public Post(String title, String description, int maxParticipants){
        this.title = title;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.participants = new ArrayList<>(maxParticipants);
    }

    public void setPOST_ID(String POST_ID) {
        this.POST_ID = POST_ID;
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

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public boolean addParticipant(String user){
        if(participants.size()!=maxParticipants && !participants.contains(user)){
            participants.add(user);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Post{" +
                "POST_ID='" + POST_ID + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", latitudeLongitude=" + latitudeLongitude +
                ", creator='" + creator + '\'' +
                ", participants=" + participants +
                ", maxParticipants=" + maxParticipants +
                '}';
    }
}
