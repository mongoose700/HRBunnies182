package com.example.michael.hrbunnies182.game;

/**
 * Created by Michael on 1/15/2016.
 */
public final class RouteCard {
    private final City firstCity;
    private final City secondCity;
    private final int length;

    public RouteCard(City firstCity, City secondCity, int length) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.length = length;
    }

    public City getFirstCity() {
        return firstCity;
    }

    public City getSecondCity() {
        return secondCity;
    }

    public int getLength() {
        return length;
    }
}
