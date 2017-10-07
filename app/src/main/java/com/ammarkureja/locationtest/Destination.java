package com.ammarkureja.locationtest;

import java.io.Serializable;

/**
 * Created by ammar on 7/10/2017.
 */

public class Destination implements Serializable{

    private static final long serialVersionUID  = 1;

private String duration;
    private String distance;

    public Destination (String duration, String distance) {
        this.duration = duration;
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public String getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Destination{" +
                "duration='" + duration + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}
