package com.example.michael.hrbunnies182.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Keeps track of which players have which edges
 */
public class ScoreMap {
    private Map<Edge, Set<Player>> owners;

    public ScoreMap(GameMap gameMap, Set<Player> players) {
        owners = new HashMap<>();
        for (Edge edge : gameMap.getEdges()) {
            owners.put(edge, new HashSet<Player>());
        }
    }

    /**
     * Tries to add the player as an owner for the edge. Returns false if it is not allowed.
     */
    public boolean addOwner(Edge edge, Player player) {
        if (!canAddOwner(edge, player)) {
            return false;
        }
        owners.get(edge).add(player);
        player.incrementTrainsRemaining(-edge.getLength());
        player.setLongestRoute(this);
        return true;
    }

    private boolean canAddOwner(Edge edge, Player player) {
        return edge.getLength() <= player.getTrainsRemaining()
                && !owners.get(edge).contains(player)
                && owners.values().size() < edge.getWidth();
    }

    public void clearEdge(Edge edge) {
        for (Player player : owners.get(edge)) {
            player.incrementTrainsRemaining(edge.getLength());
            player.setLongestRoute(this);
        }
        owners.get(edge).clear();
    }

    public int getLongestRouteLength(Player player) {
        return createGraph(player).longestRouteLength();
    }

    private Graph createGraph(Player player) {
        Graph graph = new Graph();
        for (Edge edge : getEdges(player)) {
                graph.addEdge(edge);
        }
        graph.optimize();
        return graph;
    }

    public int getTrainScore(Player player) {
        int score = 0;
        for (Edge edge : getEdges(player)) {
            score += edge.getValue();
        }
        return score;
    }

    public int getRouteScore(Player player) {
        int score = 0;
        Map<City, Set<City>> ccs = getCCs(player);
        for (RouteCard card : player.getCards()) {
            score += card.getLength() * (ccs.get(card.getFirstCity()).contains(card.getSecondCity()) ? 1 : -1);
        }
        return score;
    }

    private Map<City, Set<City>> getCCs(Player player) {
        Map<City, Set<City>> ccs = new HashMap<>();
        for (Edge edge : getEdges(player)) {
            City city1 = edge.getFirstCity();
            City city2 = edge.getSecondCity();
            for (City city : new City[]{city1, city2}) {
                if (!ccs.containsKey(city)) {
                    HashSet<City> citySet = new HashSet<>();
                    citySet.add(city);
                    ccs.put(city, citySet);
                }
            }
            if (ccs.get(city1) != ccs.get(city2)) {
                ccs.get(city1).addAll(ccs.get(city2));
                for (City city : ccs.get(city2)) {
                    ccs.put(city, ccs.get(city1));
                }
            }
        }
        return ccs;
    }

    private List<Edge> getEdges(Player player) {
        List<Edge> edges = new ArrayList<Edge>();
        for (Edge edge : owners.keySet()) {
            if (owners.get(edge).contains(player)) {
                edges.add(edge);
            }
        }
        return edges;
    }
}
