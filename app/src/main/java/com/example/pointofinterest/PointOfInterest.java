package com.example.pointofinterest;

import com.google.android.gms.maps.model.LatLng;

public class PointOfInterest {
    LatLng location;
    String locationName;

    public PointOfInterest(LatLng location, String locationName){
        this.location = location;
        this.locationName = locationName;
    }

    public String toString(){
        return "Point of Interest: " + locationName + " at location: " + location;
    }
}
