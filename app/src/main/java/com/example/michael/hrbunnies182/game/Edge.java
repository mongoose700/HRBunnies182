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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (length != edge.length) return false;
        if (width != edge.width) return false;
        return !(cities != null ? !cities.equals(edge.cities) : edge.cities != null);

    }

    @Override
    public int hashCode() {
        int result = length;
        result = 31 * result + width;
        result = 31 * result + (cities != null ? cities.hashCode() : 0);
        return result;
    }
}
