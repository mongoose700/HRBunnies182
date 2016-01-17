package com.example.michael.hrbunnies182.game;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Point;

import com.example.michael.hrbunnies182.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by Michael on 1/15/2016.
 */
public class GameMap implements Serializable {
    // Pairs city to city, without duplicates
    private Set<Edge> edges;

    // Pairs city to city, with duplicates
    private Map<City, Map<City, Integer>> shortestPathLengths;

    // Maps all city names to City objects
    private Map<String, City> cities;

    private String[] cityList;
    
    // Used for picking new cards
    private Random r = new Random();

    private String CITIES = "cities";
    private String CITY = "city";
    private String EDGES = "edges";
    private String EDGE = "edge";
    private String LENGTH = "length";
    private String WIDTH = "width";
    private String NAME = "name";
    private String COORDINATES = "coordinates";

    /**
     * Create a new map (initializing the cities and edges between them)
     */
    public GameMap(String mapName, Activity activity) {
        initMap(mapName, activity);
    }

    public static void main(String[] args) {
        GameMap test = new GameMap("usa.txt", null);

        Deck deck = test.getDeck(35);

        System.out.println("Deck: " + deck);
    }

    /**
     * Get a shuffled deck from this map
     */
    public Deck getDeck() {
        List<RouteCard> cards = getDeckCards();

        // Shuffle the cards
        Collections.shuffle(cards);

//        System.out.println("Deck: " + new Deck(cards));

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

//        System.out.println("Deck: " + new Deck(newCards));

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
                minDists[i][j] = cities.get(cityList[i]).getDistance(cities.get(cityList[j]));
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
                if (cities.get(cityList[i]).getDistance(cities.get(cityList[j])) < Integer.MAX_VALUE) {
                    // The two cities are neighbors
                    minDists[i][j] = 0;
                }
            }
        }
    }

    private void addEdge(Edge edge) {
        edges.add(edge);
        edge.getFirstCity().addEdge(edge);
        edge.getSecondCity().addEdge(edge);
    }

    /**
     * Initialize the map. Sets all the edge distances between cities.
     */
    private void initMap(String mapName, Activity activity) {
        cities = new HashMap<>();
        Resources res = activity.getResources();
        XmlResourceParser xpp = res.getXml(R.xml.usa);
        int eventType = 0;
        try {
            eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals(CITIES)) {
                        ArrayList<City> cityArrayList = extractCities(xpp);
                        cityList = new String[cityArrayList.size()];
                        for (int i = 0; i < cityList.length; i++) {
                            cityList[i] = cityArrayList.get(i).getName();
                        }
                    } else if (xpp.getName().equals(EDGES)) {
                        initEdges(xpp);
                    } else {
                        eventType = xpp.next();
                    }
                } else {
                    System.out.println("Moving on from non-start event " + XmlPullParser.TYPES[eventType]);
                    eventType = xpp.next();
                }
            }
        } catch (XmlPullParserException|IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<City> extractCities(XmlResourceParser xpp) throws XmlPullParserException, IOException {
        int eventType = xpp.next();
        ArrayList<City> newCities = new ArrayList<>(2);
        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals(CITIES))) {
            newCities.add(extractCity(xpp));
            eventType = xpp.getEventType();
        }
        xpp.next();
        return newCities;
    }

    private City extractCity(XmlResourceParser xpp) throws XmlPullParserException, IOException {
        int eventType = xpp.getEventType();
        Point coordinates = null;
        String name = null;
        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals(CITY))) {
            if (eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals(NAME)) {
                    name = extractString(xpp);
                } else if (xpp.getName().equals(COORDINATES)) {
                    coordinates = extractCoordinates(xpp);
                } else {
                    xpp.next();
                }
                eventType = xpp.getEventType();
            }
        }
        xpp.next();
        if (cities.containsKey(name)) {
            return cities.get(name);
        } else {
            return new City(name, coordinates);
        }
    }

    private Point extractCoordinates(XmlResourceParser xpp) throws XmlPullParserException, IOException {
        int eventType = xpp.getEventType();
        int x = 0;
        int y = 0;
        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals(COORDINATES))) {
            if (eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals("x")) {
                    x = extractInt(xpp);
                } else if (xpp.getName().equals("y")) {
                    y = extractInt(xpp);
                } else {
                    xpp.next();
                }
                eventType = xpp.getEventType();
            }
        }
        xpp.next();
        return new Point(x, y);
    }

    private void initEdges(XmlResourceParser xpp) throws XmlPullParserException, IOException {
        edges = new HashSet<>();
        int eventType = xpp.next();
        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals(EDGES))) {
            addEdge(extractEdge(xpp));
            eventType = xpp.getEventType();
        }
        xpp.next();
    }

    private Edge extractEdge(XmlResourceParser xpp) throws XmlPullParserException, IOException {
        int eventType = xpp.getEventType();
        ArrayList<City> cities = new ArrayList<>();
        int length = 0;
        int width = 0;
        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals(EDGE))) {
            if (eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals(CITIES)) {
                    cities = extractCities(xpp);
                } else if (xpp.getName().equals(LENGTH)) {
                    length = extractInt(xpp);
                } else if (xpp.getName().equals(WIDTH)) {
                    width = extractInt(xpp);
                } else {
                    xpp.next();
                }
                eventType = xpp.getEventType();
            }
        }
        xpp.next();
        return new Edge(cities.get(0), cities.get(1), length, width);
    }

    private String extractString(XmlResourceParser xpp) throws IOException, XmlPullParserException {
        xpp.next();
        String val = xpp.getText();
        xpp.next();
        xpp.next();
        return val;
    }

    private int extractInt(XmlResourceParser xpp) throws IOException, XmlPullParserException {
        return Integer.parseInt(extractString(xpp));
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public Edge findEdge(Point city1, Point city2) {
        for (Edge edge : edges) {

        }
        return null;
    }
}
