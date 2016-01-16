package com.example.michael.hrbunnies182.game;

import java.io.Serializable;

/**
 * Created by Michael on 1/16/2016.
 */
public class Edge implements Serializable {
    private final int length;
    private final int width;
    private final City firstCity;
    private final City secondCity;

    public Edge(City firstCity, City secondCity, int length, int width) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.length = length;
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public City getFirstCity() { return firstCity; }

    public City getSecondCity() { return secondCity; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (length != edge.length) return false;
        if (width != edge.width) return false;
        if (firstCity != null ? !firstCity.equals(edge.firstCity) : edge.firstCity != null)
            return false;
        return !(secondCity != null ? !secondCity.equals(edge.secondCity) : edge.secondCity != null);

    }

    @Override
    public int hashCode() {
        int result = length;
        result = 31 * result + width;
        result = 31 * result + (firstCity != null ? firstCity.hashCode() : 0);
        result = 31 * result + (secondCity != null ? secondCity.hashCode() : 0);
        return result;
    }
}
