package com.example.poi_drawer;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/*
 * Contents of Points of Interest.
 * Sauce: https://www.youtube.com/watch?reload=9&v=EM2x33g4syY
 */

public class PoInterest {
    String id;
    public double latitude;
    public double longitude;
    public String title;
    String comments;
    String category;

    // This constructor is used while reading the values. DON'T DELETE.
    public PoInterest(){

    }

    public PoInterest(double latitude, double longitude, String title, String comments, String category) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.comments = comments;
        this.category = category;
    }

    public String getId() {return id;}

    public double getLatitude() {return latitude;}

    public double getLongitude() {return longitude;}

    public String getComments(){
        return comments;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }
}
