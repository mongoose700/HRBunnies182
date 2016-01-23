package com.example.michael.hrbunnies182.game;

import android.graphics.Point;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 1/15/2016.
 */
public class City implements Serializable{
    private String name;
    private Map<String, Integer> neighbors;
    private Point boardCoordinates;
    private Point cardCoordinates;

    /**
     * Create a city with the coordinates for the board game or a route card.
     * @param name The city name
     * @param boardCoordinates The coordinates of the city on the game
     * @param cardCoordinates The coordinates of the city on a route card
     */
    public City(String name, Point boardCoordinates, Point cardCoordinates) {
        this.name = name;
        neighbors = new HashMap<>();
        this.boardCoordinates = boardCoordinates;
        this.cardCoordinates = cardCoordinates;
    }

    public void addEdge(Edge edge) {
        if (this.equals(edge.getFirstCity())) {
            neighbors.put(edge.getSecondCity().getName(), edge.getLength());
        } else {
            neighbors.put(edge.getFirstCity().getName(), edge.getLength());
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
        if (neighbors.containsKey(city.getName())) {
            return neighbors.get(city.getName());
        }
        return Integer.MAX_VALUE;
    }

    public Point getBoardCoordinates() {
        return boardCoordinates;
    }

    public Point getCardCoordinates() {
        return cardCoordinates;
    }

    public String toString() {
        return name;
    }
}
