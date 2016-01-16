package com.example.michael.hrbunnies182.game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 1/15/2016.
 */
public class GameMap {
    // Pairs city to city, west to east
    private Map<City, Map<City, Integer>> edgeDistances;
    private Map<City, Map<City, Integer>> shortestPathLengths;

    /**
     * Create a new map (initializing the cities and edges between them)
     */
    public GameMap() {
        initMap();
    }

    /**
     * Initialize the cities (currently hardcoded for the American map)
     * Sets all the edge distances between cities.
     */
    public void initMap() {
        edgeDistances = new HashMap<>();

        // Vancouver
        edgeDistances.put(new City("Vancouver"),
                fillMap(new String[]{"Calgary"}, new Integer[]{3}));

        // Seattle
        edgeDistances.put(new City("Seattle"),
                fillMap(new String[]{"Calgary", "Vancouver", "Helena"}, new Integer[]{4, 1, 6}));

        // Portland
        edgeDistances.put(new City("Portland"),
                fillMap(new String[]{"Seattle", "Salt Lake City", "San Francisco"}, new Integer[]{1, 6, 5}));

        // San Francisco
        edgeDistances.put(new City("San Francisco"),
                fillMap(new String[]{"Salt Lake City", "Los Angeles"}, new Integer[]{5, 3}));

        // Los Angeles
        edgeDistances.put(new City("Los Angeles"),
                fillMap(new String[]{"Las Vegas", "Phoenix", "El Paso"}, new Integer[]{2, 3, 6}));


        // Calgary
        edgeDistances.put(new City("Calgary"),
                fillMap(new String[]{ "Helena", "Winnipeg"}, new Integer[] {4, 6}));

        // Helena
        edgeDistances.put(new City("Helena"),
                fillMap(new String[] {"Denver", "Omaha", "Duluth", "Winnipeg"}, new Integer[] {4, 5, 6, 4}));

        // Salt Lake City
        edgeDistances.put(new City("Salt Lake City"),
                fillMap(new String[]{"Denver", "Helena"}, new Integer[] {3, 3}));

        // Las Vegas
        edgeDistances.put(new City("Las Vegas"),
                fillMap(new String[]{"Salt Lake City"}, new Integer[] {3}));

        // Phoenix
        edgeDistances.put(new City("Phoenix"),
                fillMap(new String[]{"El Paso", "Santa Fe", "Denver"}, new Integer[] {3, 3, 5}));


        // Winnipeg
        edgeDistances.put(new City("Winnipeg"),
                fillMap(new String[]{"Sault St. Marie", "Duluth"}, new Integer[] {6, 4}));

        // Denver
        edgeDistances.put(new City("Denver"),
                fillMap(new String[]{"Omaha", "Kansas City", "Oklahoma City"}, new Integer[] {4, 4, 4}));

        // Santa Fe
        edgeDistances.put(new City("Santa Fe"),
                fillMap(new String[]{"Denver", "Oklahoma City"}, new Integer[] {2, 3}));

        // El Paso
        edgeDistances.put(new City("El Paso"),
                fillMap(new String[]{"Santa Fe", "Oklahoma City", "Dallas", "Houston"}, new Integer[] {2, 5, 4, 6}));


        // Duluth
        edgeDistances.put(new City("Duluth"),
                fillMap(new String[]{"Sault St. Marie", "Toronto", "Chicago"}, new Integer[] {3, 6, 3}));

        // Omaha
        edgeDistances.put(new City("Omaha"),
                fillMap(new String[]{"Duluth", "Chicago", "Kansas City"}, new Integer[] {2, 4, 1}));

        // Kansas City
        edgeDistances.put(new City("Kansas City"),
                fillMap(new String[]{"Saint Louis"}, new Integer[] {2}));

        // Oklahoma City
        edgeDistances.put(new City("Oklahoma City"),
                fillMap(new String[]{"Kansas City", "Little Rock", "Dallas"}, new Integer[] {2, 2, 2}));

        // Dallas
        edgeDistances.put(new City("Dallas"),
                fillMap(new String[]{"Little Rock", "Houston"}, new Integer[] {2, 1}));

        // Houston
        edgeDistances.put(new City("Houston"),
                fillMap(new String[]{"New Orleans"}, new Integer[] {2}));


        // Sault St. Marie
        edgeDistances.put(new City("Sault St. Marie"),
                fillMap(new String[]{"Montreal", "Toronto"}, new Integer[] {5, 2}));

        // Chicago
        edgeDistances.put(new City("Chicago"),
                fillMap(new String[]{"Toronto", "Pittscurgh"}, new Integer[] {4, 3}));

        // Saint Louis
        edgeDistances.put(new City("Saint Louis"),
                fillMap(new String[]{"Chicago", "Pittsburgh", "Nashville"}, new Integer[] {2, 5, 2}));

        // Little Rock
        edgeDistances.put(new City("Little Rock"),
                fillMap(new String[]{"Saint Louis", "Nashville", "New Orleans"}, new Integer[] {2, 3, 3}));

        // New Orleans
        edgeDistances.put(new City("New Orleans"),
                fillMap(new String[]{"Atlanta", "Miami"}, new Integer[] {4, 6}));


        // Toronto
        edgeDistances.put(new City("Toronto"),
                fillMap(new String[]{"Montreal"}, new Integer[] {3}));

        // Pittsburgh
        edgeDistances.put(new City("Pittsburgh"),
                fillMap(new String[]{"Toronto", "New York", "Washington", "Raleigh"}, new Integer[] {2, 2, 2, 2}));

        // Nashville
        edgeDistances.put(new City("Nashville"),
                fillMap(new String[]{"Pittsburgh", "Raleigh", "Atlanta"}, new Integer[] {4, 3, 1}));

        // Atlanta
        edgeDistances.put(new City("Atlanta"),
                fillMap(new String[]{"Raleigh", "Charleston", "Miami"}, new Integer[] {2, 2, 5}));


        // Montreal
        edgeDistances.put(new City("Montreal"),
                fillMap(new String[]{"Boston", "New York"}, new Integer[] {2, 3}));

        // Boston

        // New York
        edgeDistances.put(new City("New York"),
                fillMap(new String[]{"Boston", "Washington"}, new Integer[] {2, 2}));

        // Washington

        // Raleigh
        edgeDistances.put(new City("Raleigh"),
                fillMap(new String[]{"Washington", "Charleston"}, new Integer[] {2, 2}));

        // Charleston
        edgeDistances.put(new City("Charleston"),
                fillMap(new String[]{"Miami"}, new Integer[] {4}));

        // Miami

    }


    /**
     * Add the given city names and distances to the given cityMap
     *
     * @param cityNames A list of all neighboring cities
     * @param cityDistances A list of the distances to the names in cityNames (indexed as in cityNames)
     * @return A Map pairing those names and distances, which will be cleared and modified
     */
    private Map<City, Integer> fillMap(String[] cityNames, Integer[] cityDistances) {
        Map<City, Integer> cityMap = new HashMap<>();
        if (cityNames.length != cityDistances.length) {
            System.err.println("FATAL ERROR!  Mismatched cityNames and cityDistances when initializing map: " +
                    Arrays.toString(cityNames) + ", " + Arrays.toString(cityDistances));
            return new HashMap<>();
        }

        for (int i = 0; i < cityNames.length; i++) {
            cityMap.put(new City(cityNames[i]), cityDistances[i]);
        }
        return cityMap;
    }
}
