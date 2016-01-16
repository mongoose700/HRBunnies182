package com.example.michael.hrbunnies182.game;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Pair;

import com.example.michael.hrbunnies182.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Michael on 1/15/2016.
 */
public class GameMap {
    // Pairs city to city, with duplicates
    private Map<City, Map<City, Edge>> edges;

    // Pairs city to city, with duplicates
    private Map<City, Map<City, Integer>> shortestPathLengths;

    // Maps all city names to City objects
    private Map<String, City> cities;
    
    // Used for picking new cards
    private Random r = new Random();

    private String CITIES = "cities";
    private String EDGES = "edges";
    private String EDGE = "edge";
    private String LENGTH = "length";
    private String WIDTH = "width";

    // Used to index cities when seeking all-pairs shortest-path
    String[] cityList = new String[] {"Vancouver", "Seattle", "Portland", "San Francisco",
            "Los Angeles", "Calgary", "Helena", "Salt Lake City", "Las Vegas", "Phoenix",
            "Winnipeg", "Denver", "Santa Fe", "El Paso", "Duluth", "Omaha", "Kansas City",
            "Oklahoma City", "Dallas", "Houston", "Sault St. Marie", "Chicago","Saint Louis",
            "Little Rock", "New Orleans", "Toronto", "Pittsburgh", "Nashville", "Atlanta",
            "Montreal", "Boston", "New York", "Washington", "Raleigh", "Charleston", "Miami"};

    /**
     * Create a new map (initializing the cities and edges between them)
     */
    public GameMap(String mapName, Activity activity) {
        initMap(mapName, activity);
    }

    public static void main(String[] args) {
        GameMap test = new GameMap("usa.txt", null);

        Deck deck = test.getDeck(30);

        System.out.println("Deck: " + deck);
    }

    /**
     * Get a shuffled deck from this map
     */
    public Deck getDeck() {
        List<RouteCard> cards = getDeckCards();

        // Shuffle the cards
        Collections.shuffle(cards);

        return new Deck(cards);
    }

    /**
     * Get a deck with the given number of cards from this map;
     * should be fairly evenly distributed across the country
     *
     * @param numCards The number of cards to produce
     */
    public Deck getDeck(int numCards) {
        numCards = numCards - 1; // Hack to make the math easier

        // Get a list of cards, not shuffled for better card distribution
        List<RouteCard> initCards = getDeckCards();

        // Get the interval at which to draw cards (truncating)
        int interval = initCards.size() / numCards;

        // Pick a random starting card so we don't always include Vancouver to Portland
        int offset = initCards.size() % numCards;
        int start = r.nextInt(offset);

//        System.out.println("Picking " + (numCards + 1) + " cards from deck of " + initCards.size() +
//                " with start index " + start + " from offset " + offset + " and interval " + interval);

        ArrayList<RouteCard> newCards = new ArrayList<>();
        for (int i = start; i < initCards.size(); i = i + interval) {
//            System.out.println("Picking card at index " + i);
            newCards.add(initCards.get(i));
        }

        Collections.shuffle(newCards);

        return new Deck(newCards);
    }

    /**
     * Get a list of RouteCards
     */
    private List<RouteCard> getDeckCards() {
        // Get all valid pairs of cities and the shortest distances between them
        Integer[][] minDists = getMinCityDist();
//        System.out.println("Naive minimum distances:");
//        prettyPrintDistances(minDists);

        // Mutate minDists, setting all invalid pairs of cities to have 0 distance
        validateCities(minDists);

//        System.out.println("Validated cities:");
//        prettyPrintDistances(minDists);

        ArrayList<RouteCard> cards = new ArrayList<>();
        // Create RouteCards for each city and add them to the deck.
        // Takes only the upper triangle for efficiency
        for (int i = 0; i < minDists.length; i++) {
            for (int j = i + 1; j < minDists[0].length; j++) {
                if (minDists[i][j] != 0 ) {
                    cards.add(new RouteCard(cities.get(cityList[i]), cities.get(cityList[j]),
                            minDists[i][j]));
                }
            }
        }
        return cards;
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
     * Use the Floyd-Warshall algorithm to find the minimum distance between all pairs of cities
     */
    private Integer[][] getMinCityDist() {
        Integer[][] minDists = new Integer[cityList.length][cityList.length];

        // Initialize all distances to infinity, except for neighbors and city-to-itself
        for (int i = 0; i < cityList.length; i++) {
            for (int j = 0; j < cityList.length; j++) {
                if (i != j) {
                    if (edges.get(cities.get(cityList[i])).containsKey(cities.get(cityList[j]))) {
                        // The two cities are neighbors
                        minDists[i][j] = edges.get(cities.get(cityList[i])).get(cities.get(cityList[j])).getLength();
                    } else {
                        minDists[i][j] = Integer.MAX_VALUE;
                    }
                } else {
                    // The cities are identical
                    minDists[i][j] = 0;
                }
            }
        }

//        System.out.println("Initialized distance array (before minimum search):");
//        prettyPrintDistances(minDists);

        // Search for minimum distances
        for (int k = 0; k < cityList.length; k++) {
            for (int j = 0; j < cityList.length; j++) {
                for (int i = 0; i < cityList.length; i++) {
                    if (minDists[i][j] > safeInfAdd(minDists[i][k], minDists[k][j])) {
                        minDists[i][j] = minDists[i][k] + minDists[k][j];
                    }
                }
            }
        }

        return minDists;
    }

    /**
     * If num1 + num2 would be greater than Integer.MAX_VALUE (and hence overflow), returns
     * Integer.MAX_VALUE instead.
     *
     * @param num1 The first number being added
     * @param num2 The second number being added
     * @return The smaller of
     */
    private Integer safeInfAdd(int num1, int num2) {
        if (num1 == Integer.MAX_VALUE || num2 == Integer.MAX_VALUE) {
            // Clearly going to overflow
            return Integer.MAX_VALUE;
        } else if (num1 > 0 && num2 > 0 && ((num1 + num2 < num1) || (num1 + num2 < num2))) {
            // It overflowed
            return Integer.MAX_VALUE;
        } else {
            return num1 + num2;
        }
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
                if (edges.get(cities.get(cityList[i])).containsKey(cities.get(cityList[j]))) {
                    // The two cities are neighbors
                    minDists[i][j] = 0;
                }
            }
        }
    }

    private void addCity(String line) {
        cities.put(line, new City(line));
    }

    private void addEdge(String line) {
        Pattern pattern = Pattern.compile("(.*)\\t+(.*)\\t+([0-9]*)\\t+([0-9]*)");
        Matcher matcher = pattern.matcher(line);
        String firstCityName = matcher.group(1);
        String secondCityName = matcher.group(2);
        int length = Integer.parseInt(matcher.group(3));
        int width = Integer.parseInt(matcher.group(4));
        addEdge(firstCityName, secondCityName, length, width);
    }

    private void addEdge(String firstCityName, String secondCityName, int length, int width) {
        City firstCity = cities.get(firstCityName);
        City secondCity = cities.get(secondCityName);
        Edge edge = new Edge(firstCity, secondCity, length, width);
        if (!edges.containsKey(firstCity)) {
            edges.put(firstCity, new HashMap<City, Edge>());
        }
        edges.get(firstCity).put(secondCity, edge);
    }

    /**
     * Initialize the map (currently hardcoded for the American map)
     * Sets all the edge distances between cities.
     */
    private void initMap(String mapName, Activity activity) {
        Resources res = activity.getResources();
        XmlResourceParser xpp = res.getXml(R.xml.usa);
        int eventType = 0;
        try {
            eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals(CITIES)) {
                        initCities(xpp);
                    } else if (xpp.getName().equals(EDGES)) {
                        initEdges(xpp);
                    }
                }
            }
        } catch (XmlPullParserException|IOException e) {
            e.printStackTrace();
        }
    }

    private void initCities(XmlResourceParser xpp) throws XmlPullParserException, IOException {
        cityList = extractCities(xpp);
        for (String cityName : cityList) {
            cities.put(cityName, new City(cityName));
        }
    }

    private void initEdges(XmlResourceParser xpp) throws XmlPullParserException, IOException {
        int eventType = xpp.getEventType();
        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals(EDGES))) {
            Edge edge = extractEdge(xpp);
            Pair<City, City> edgeCities = edge.getCities();
            if (!edges.containsKey(edgeCities.first)) {
                edges.put(edgeCities.first, new HashMap<City, Edge>());
            }
            edges.get(edgeCities.first).put(edgeCities.second, edge);
            eventType = xpp.getEventType();
        }
        xpp.next();
    }

    private Edge extractEdge(XmlResourceParser xpp) throws XmlPullParserException, IOException {
        int eventType = xpp.getEventType();
        String[] cityNames = new String[2];
        int length = 0;
        int width = 0;
        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals(EDGE))) {
            if (eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals(CITIES)) {
                    cityNames = extractCities(xpp);
                } else if (xpp.getName().equals(LENGTH)) {
                    length = extractInt(xpp);
                } else if (xpp.getName().equals(WIDTH)) {
                    width = extractInt(xpp);
                }
            }
        }
        xpp.next();
        return new Edge(cities.get(cityNames[0]), cities.get(cityNames[1]), length, width);
    }

    private String[] extractCities(XmlResourceParser xpp) throws XmlPullParserException, IOException {
        ArrayList<String> newCities = new ArrayList<>();
        int eventType = xpp.getEventType();
        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals(CITIES))) {
            if (eventType == XmlPullParser.TEXT) {
                newCities.add(xpp.getName());
            }
            eventType = xpp.next();
        }
        xpp.next();
        return newCities.toArray(new String[cities.size()]);
    }

    private int extractInt(XmlResourceParser xpp) throws IOException, XmlPullParserException {
        xpp.next();
        int val = Integer.parseInt(xpp.getName());
        xpp.next();
        xpp.next();
        return val;
    }
}
