package com.skrebe.titas.grabble.notUsed;

/**
 * Created by titas on 16.9.28.
 */
public class PlaceMark {

    final String name;
    final String description;
    final Point point;

    @Override
    public String toString() {
        return "PlaceMark{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", point=" + point +
                '}';
    }

    public PlaceMark(String name, String description, Point point) {
        this.name = name;
        this.description = description;
        this.point = point;
    }

    public static class Point{
        final double latitude;
        final double longitude;
        public Point(double latitude, double longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
