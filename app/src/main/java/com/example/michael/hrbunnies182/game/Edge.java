package com.example.michael.hrbunnies182.game;

import android.util.Pair;

/**
 * Created by Michael on 1/16/2016.
 */
public class Edge {
    private final int length;
    private final int width;
    private final Pair<City, City> cities;

    public Edge(City firstCity, City secondCity, int length, int width) {
        this.cities = new Pair<>(firstCity, secondCity);
        this.length = length;
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public Pair<City, City> getCities() {
        return cities;
    }
}
