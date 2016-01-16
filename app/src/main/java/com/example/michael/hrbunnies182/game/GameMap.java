package com.example.michael.hrbunnies182.game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 1/15/2016.
 */
public class GameMap {
    // Pairs city to city, with duplicates
    private Map<City, Map<City, Integer>> edgeDistances;

    // Pairs city to city, with duplicates
    private Map<City, Map<City, Integer>> shortestPathLengths;

    // Maps all city names to City objects
    private Map<String, City> cityNames;

    // Used to index cities when seeking all-pairs shortest-path, and also for initCities.
    // Clearly, hardcodes for America
    String[] testCityList = new String[] {"Vancouver", "Seattle", "Portland", "San Francisco", "Salt Lake City"};

    String[] cityList = new String[] {"Vancouver", "Seattle", "Portland", "San Francisco",
            "Los Angeles", "Calgary", "Helena", "Salt Lake City", "Las Vegas", "Phoenix",
            "Winnipeg", "Denver", "Santa Fe", "El Paso", "Duluth", "Omaha", "Kansas City",
            "Oklahoma City", "Dallas", "Houston", "Sault St. Marie", "Chicago","Saint Louis",
            "Little Rock", "New Orleans", "Toronto", "Pittsburgh", "Nashville", "Atlanta",
            "Montreal", "Boston", "New York", "Washington", "Raleigh", "Charleston", "Miami"};

    /**
     * Create a new map (initializing the cities and edges between them)
     */
    public GameMap(boolean test) {
        initCities();
        if (!test) {
            initMap();
        } else {
            initTestMap();
        }
    }

    public static void main(String[] args) {
        GameMap test = new GameMap(true);

        Deck deck = test.getDeck();

        System.out.println("Deck: " + deck);
    }

    /**
     * Get a shuffled deck from this map
     */
    public Deck getDeck() {
        // Get all valid pairs of cities and the shortest distances between them
        Integer[][] minDists = getMinCityDist();
        System.out.println("Naive minimum distances:");
        prettyPrintDistances(minDists);

        // Mutate minDists, setting all invalid pairs of cities to have 0 distance
        validateCities(minDists);

        System.out.println("Validated cities:");
        prettyPrintDistances(minDists);

        ArrayList<RouteCard> cards = new ArrayList<>();
        // Create RouteCards for each city and add them to the deck.
        // Takes only the upper triangle for efficiency
        for (int i = 0; i < minDists.length; i++) {
            for (int j = i + 1; j < minDists[0].length; j++) {
                if (minDists[i][j] != 0 ) {
                    cards.add(new RouteCard(cityNames.get(cityList[i]), cityNames.get(cityList[j]),
                            minDists[i][j]));
                }
            }
        }

        // Shuffle the cards
        Collections.shuffle(cards);

        return new Deck(cards);
    }

    /**
     * Print the distance array nicely
     */
    private void prettyPrintDistances(Integer[][] dists) {
        StringBuilder msg = new StringBuilder();

        msg.append(String.format("%15s", " "));

        for (int i = 0; i < cityList.length; i++) {
            msg.append(String.format("%15s", cityList[i]));
        }

        msg.append("\n");

        for (int i = 0; i < dists.length; i++) {
            msg.append(String.format("%15s", cityList[i]));
            for (int j = 0; j < dists[i].length; j++) {
                msg.append(String.format("%15s", Integer.toString(dists[i][j])));
            }
            msg.append("\n");
        }

        System.out.println(msg.toString());
    }

    /**
     * Use the Floyd-Warshall algorithm to find the minimu distance between all pairs of cities
     */
    private Integer[][] getMinCityDist() {
        Integer[][] minDists = new Integer[cityList.length][cityList.length];

        // Initialize all distances to infinity, except for neighbors and city-to-itself
        for (int i = 0; i < cityList.length; i++) {
            for (int j = 0; j < cityList.length; j++) {
                if (i != j) {
                    if (edgeDistances.get(cityNames.get(cityList[i])).containsKey(cityNames.get(cityList[j]))) {
                        // The two cities are neighbors
                        minDists[i][j] = edgeDistances.get(cityNames.get(cityList[i])).get(cityNames.get(cityList[j]));
                    } else {
                        minDists[i][j] = Integer.MAX_VALUE;
                    }
                } else {
                    // The cities are identical
                    minDists[i][j] = 0;
                }
            }
        }

        System.out.println("Initialized distance array (before minimum search):");
        prettyPrintDistances(minDists);

        // Search for minimum distances
        for (int k = 0; k < cityList.length; k++) {
            for (int j = 0; j < cityList.length; j++) {
                for (int i = 0; i < cityList.length; i++) {
                    if (minDists[i][j] > minDists[i][k] + minDists[k][j]) {
                        minDists[i][j] = minDists[i][k] + minDists[k][j];
                    }
                }
            }
        }

        return minDists;
    }

    /**
     * Set the distance between a city and itself or a city and any of its neighbors to 0
     * in the input array
     *
     * @param minDists An array of city distances to be validated
     */
    private void validateCities(Integer[][] minDists) {
        for (int i = 0; i < cityList.length; i++) {
            for (int j = 0; j < cityList.length; j++) {
                if (edgeDistances.get(cityNames.get(cityList[i])).containsKey(cityNames.get(cityList[j]))) {
                    // The two cities are neighbors
                    minDists[i][j] = 0;
                }
            }
        }
    }


    /**
     * Map all city names to City objects, so we only make the Cities once
     * (currently hardcoded for the American map)
     */
    private void initCities() {
        cityNames = new HashMap<>();

        for (String name: cityList) {
            cityNames.put(name, new City(name));
        }
    }

    /**
     * Initialize a test map
     */
    private void initTestMap() {
        edgeDistances = new HashMap<>();

        // Vancouver
        edgeDistances.put(cityNames.get("Vancouver"),
                fillMap(new String[]{"Seattle"}, new Integer[]{1}));

        // Seattle
        edgeDistances.put(cityNames.get("Seattle"),
                fillMap(new String[]{"Vancouver", "Portland"}, new Integer[]{1, 1}));

        // Portland
        edgeDistances.put(cityNames.get("Portland"),
                fillMap(new String[]{"Seattle", "Salt Lake City", "San Francisco"}, new Integer[]{1, 6, 5}));

        // San Francisco
        edgeDistances.put(cityNames.get("San Francisco"),
                fillMap(new String[]{"Portland", "Salt Lake City"}, new Integer[]{5, 5}));

        // Salt Lake City
        edgeDistances.put(cityNames.get("Salt Lake City"),
                fillMap(new String[]{"Portland", "San Francisco"},
                        new Integer[] {6, 5}));

    }

    /**
     * Initialize the map (currently hardcoded for the American map)
     * Sets all the edge distances between cities.
     */
    private void initMap() {
        edgeDistances = new HashMap<>();

        // Vancouver
        edgeDistances.put(cityNames.get("Vancouver"),
                fillMap(new String[]{"Calgary", "Seattle"}, new Integer[]{3, 1}));

        // Seattle
        edgeDistances.put(cityNames.get("Seattle"),
                fillMap(new String[]{"Calgary", "Vancouver", "Helena", "Portland"}, new Integer[]{4, 1, 6, 1}));

        // Portland
        edgeDistances.put(cityNames.get("Portland"),
                fillMap(new String[]{"Seattle", "Salt Lake City", "San Francisco"}, new Integer[]{1, 6, 5}));

        // San Francisco
        edgeDistances.put(cityNames.get("San Francisco"),
                fillMap(new String[]{"Portland", "Salt Lake City", "Los Angeles"}, new Integer[]{5, 5, 3}));

        // Los Angeles
        edgeDistances.put(cityNames.get("Los Angeles"),
                fillMap(new String[]{"San Francisco", "Las Vegas", "Phoenix", "El Paso"}, new Integer[]{3, 2, 3, 6}));


        // Calgary
        edgeDistances.put(cityNames.get("Calgary"),
                fillMap(new String[]{"Vancouver", "Seattle", "Helena", "Winnipeg"}, new Integer[] {3, 4, 4, 6}));

        // Helena
        edgeDistances.put(cityNames.get("Helena"),
                fillMap(new String[] {"Calgary", "Seattle", "Salt Lake City", "Denver", "Omaha", "Duluth", "Winnipeg"},
                        new Integer[] {4, 6, 3, 4, 5, 6, 4}));

        // Salt Lake City
        edgeDistances.put(cityNames.get("Salt Lake City"),
                fillMap(new String[]{"Portland", "San Francisco", "Las Vegas", "Denver", "Helena"},
                        new Integer[] {6, 5, 3, 3, 3}));

        // Las Vegas
        edgeDistances.put(cityNames.get("Las Vegas"),
                fillMap(new String[]{"Salt Lake City", "Los Angeles"}, new Integer[] {3, 2}));

        // Phoenix
        edgeDistances.put(cityNames.get("Phoenix"),
                fillMap(new String[]{"Los Angeles", "El Paso", "Santa Fe", "Denver"},
                        new Integer[] {3, 3, 3, 5}));


        // Winnipeg
        edgeDistances.put(cityNames.get("Winnipeg"),
                fillMap(new String[]{"Calgary", "Helena", "Sault St. Marie", "Duluth"},
                        new Integer[] {6, 4, 6, 4}));

        // Denver
        edgeDistances.put(cityNames.get("Denver"),
                fillMap(new String[]{"Helena", "Salt Lake City", "Phoenix", "Santa Fe", "Omaha", "Kansas City", "Oklahoma City"},
                        new Integer[] {4, 3, 5, 2, 4, 4, 4}));

        // Santa Fe
        edgeDistances.put(cityNames.get("Santa Fe"),
                fillMap(new String[]{"Phoenix", "El Paso", "Denver", "Oklahoma City"},
                        new Integer[] {3, 2, 2, 3}));

        // El Paso
        edgeDistances.put(cityNames.get("El Paso"),
                fillMap(new String[]{"Los Angeles", "Phoenix", "Santa Fe", "Oklahoma City", "Dallas", "Houston"},
                        new Integer[] {6, 3, 2, 5, 4, 6}));


        // Duluth
        edgeDistances.put(cityNames.get("Duluth"),
                fillMap(new String[]{"Winnipeg", "Helena", "Omaha", "Sault St. Marie", "Toronto", "Chicago"},
                        new Integer[] {4, 6, 2, 3, 6, 3}));

        // Omaha
        edgeDistances.put(cityNames.get("Omaha"),
                fillMap(new String[]{"Helena", "Denver", "Duluth", "Chicago", "Kansas City"},
                        new Integer[] {5, 4, 2, 4, 1}));

        // Kansas City
        edgeDistances.put(cityNames.get("Kansas City"),
                fillMap(new String[]{"Omaha", "Denver", "Oklahoma City", "Saint Louis"},
                        new Integer[] {1, 4, 2, 2}));

        // Oklahoma City
        edgeDistances.put(cityNames.get("Oklahoma City"),
                fillMap(new String[]{"Denver", "Santa Fe", "El Paso", "Kansas City", "Little Rock", "Dallas"},
                        new Integer[] {4, 3, 5, 2, 2, 2}));

        // Dallas
        edgeDistances.put(cityNames.get("Dallas"),
                fillMap(new String[]{"Oklahoma City", "El Paso", "Little Rock", "Houston"},
                        new Integer[] {2, 4, 2, 1}));

        // Houston
        edgeDistances.put(cityNames.get("Houston"),
                fillMap(new String[]{"Dallas", "El Paso", "New Orleans"}, new Integer[] {1, 6, 2}));


        // Sault St. Marie
        edgeDistances.put(cityNames.get("Sault St. Marie"),
                fillMap(new String[]{"Winnipeg", "Duluth", "Montreal", "Toronto"},
                        new Integer[] {6, 3, 5, 2}));

        // Chicago
        edgeDistances.put(cityNames.get("Chicago"),
                fillMap(new String[]{"Duluth", "Omaha", "Sainnt Louis", "Toronto", "Pittscurgh"},
                        new Integer[] {3, 4, 2, 4, 3}));

        // Saint Louis
        edgeDistances.put(cityNames.get("Saint Louis"),
                fillMap(new String[]{"Kansas City", "Little Rock", "Chicago", "Pittsburgh", "Nashville"},
                        new Integer[] {2, 2, 2, 5, 2}));

        // Little Rock
        edgeDistances.put(cityNames.get("Little Rock"),
                fillMap(new String[]{"Oklahoma City", "Dallas", "Saint Louis", "Nashville", "New Orleans"},
                        new Integer[] {2, 2, 2, 3, 3}));

        // New Orleans
        edgeDistances.put(cityNames.get("New Orleans"),
                fillMap(new String[]{"Little Rock", "Houston", "Atlanta", "Miami"},
                        new Integer[] {3, 2, 4, 6}));


        // Toronto
        edgeDistances.put(cityNames.get("Toronto"),
                fillMap(new String[]{"Sault St. Marie", "Duluth", "Chicago", "Pittsburgh", "Montreal"},
                        new Integer[] {2, 6, 4, 2, 3}));

        // Pittsburgh
        edgeDistances.put(cityNames.get("Pittsburgh"),
                fillMap(new String[]{"Chicago", "Saint Louis", "Nashville", "Toronto", "New York", "Washington", "Raleigh"},
                        new Integer[] {3, 5, 4, 2, 2, 2, 2}));

        // Nashville
        edgeDistances.put(cityNames.get("Nashville"),
                fillMap(new String[]{"Saint Louis", "Little Rock", "Pittsburgh", "Raleigh", "Atlanta"},
                        new Integer[] {2, 3, 4, 3, 1}));

        // Atlanta
        edgeDistances.put(cityNames.get("Atlanta"),
                fillMap(new String[]{"Nashville", "New Orleans", "Raleigh", "Charleston", "Miami"},
                        new Integer[] {1, 4, 2, 2, 5}));


        // Montreal
        edgeDistances.put(cityNames.get("Montreal"),
                fillMap(new String[]{"Sault St. Marie", "Toronto", "Boston", "New York"},
                        new Integer[] {5, 3, 2, 3}));

        // Boston
        edgeDistances.put(cityNames.get("Boston"),
                fillMap(new String[]{"Montreal", "New York"},
                        new Integer[] {2, 2}));

        // New York
        edgeDistances.put(cityNames.get("New York"),
                fillMap(new String[]{"Montreal", "Pittsburgh", "Boston", "Washington"},
                        new Integer[] {3, 2, 2, 2}));

        // Washington
        edgeDistances.put(cityNames.get("Washington"),
                fillMap(new String[]{"New York", "Pittsburgh", "Raleigh"},
                        new Integer[] {2, 2, 2}));

        // Raleigh
        edgeDistances.put(cityNames.get("Raleigh"),
                fillMap(new String[]{"Pittsburgh", "Nashville", "Atlanta", "Washington", "Charleston"},
                        new Integer[] {2, 3, 2, 2, 2}));

        // Charleston
        edgeDistances.put(cityNames.get("Charleston"),
                fillMap(new String[]{"Raleigh", "Atlanta", "Miami"}, new Integer[] {2, 2, 4}));

        // Miami
        edgeDistances.put(cityNames.get("Miami"),
                fillMap(new String[]{"New Orleans", "Atlanta", "Charleston"},
                        new Integer[] {6, 5, 4}));

    }


    /**
     * Add the given city names and distances to the given cityMap
     *
     * @param cityNameArray A list of all neighboring cities
     * @param cityDistances A list of the distances to the names in cityNames (indexed as in cityNames)
     * @return A Map pairing those names and distances, which will be cleared and modified
     */
    private Map<City, Integer> fillMap(String[] cityNameArray, Integer[] cityDistances) {
        Map<City, Integer> cityMap = new HashMap<>();
        if (cityNameArray.length != cityDistances.length) {
            System.err.println("FATAL ERROR!  Mismatched cityNames and cityDistances when initializing map: " +
                    Arrays.toString(cityNameArray) + ", " + Arrays.toString(cityDistances));
            return new HashMap<>();
        }

        for (int i = 0; i < cityNameArray.length; i++) {
            cityMap.put(cityNames.get(cityNameArray[i]), cityDistances[i]);
        }
        return cityMap;
    }

    public Deck createDeck() {
        //TODO: create deck from map
        return null;
    }
}
