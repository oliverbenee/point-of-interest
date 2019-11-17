package com.example.poi_drawer;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Contents of Points of Interest.
 * Source: Simplified Coding at: https://www.youtube.com/watch?reload=9&v=EM2x33g4syY - For guiding the process of implementing the class.
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

    /**
     * Constructor of new Points of Interest.
     * @param latitude Latitude of the point of interest
     * @param longitude Longitude of the point of interest
     * @param title Title of the point of interest
     * @param comments Comments to the point of interest
     * @param category Category of the point of interest
     */
    public PoInterest(double latitude, double longitude, String title, String comments, String category) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.comments = comments;
        this.category = category;
    }

    /**
     * Get the unique ID of the Point of Interest.
     * @return the id of the Point of Interest marker
     */
    public String getId() {return id;}

    /**
     * Get the latitude of the Point of Interest.
     * @return the latitude of the point of interest.
     */
    public double getLatitude() {return latitude;}

    /**
     * Get the longitude of the Point of Interest.
     * @return the longitude of the point of interest.
     */
    public double getLongitude() {return longitude;}

    /**
     * Get the comments to the Point of Interest.
     * @return the comments to the point of interest.
     */
    public String getComments(){
        return comments;
    }

    /**
     * Get the title of the Point of Interest.
     * @return the title of the point of interest.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the category of the Point of Interest.
     * @return the category of the point of interest.
     */
    public String getCategory() {
        return category;
    }
}
