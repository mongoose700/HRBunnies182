package com.example.michael.hrbunnies182.game;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 1/15/2016.
 */
public class City {
    private String name;
    private Map<City, Edge> neighbors;

    public City(String name) {
        this.name = name;
        neighbors = new HashMap<>();
    }

    public void addEdge(Edge edge) {
        if (this.equals(edge.getCities().first)) {
            neighbors.put(edge.getCities().second, edge);
        } else {
            neighbors.put(edge.getCities().first, edge);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        return !(name != null ? !name.equals(city.name) : city.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public int getDistance(City city) {
        if (this.equals(city)) {
            return 0;
        }
        if (neighbors.containsKey(city)) {
            return neighbors.get(city).getLength();
        }
        return Integer.MAX_VALUE;
    }
}
