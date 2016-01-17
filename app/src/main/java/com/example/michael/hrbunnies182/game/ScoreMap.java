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
            System.out.println("ScoreMap: Unable to add edge");
//            System.out.println("Owners: " + owners);
            return false;
        }
        System.out.println("ScoreMap: Adding edge " + edge + " to player " + player);
        owners.get(edge).add(player);
//        System.out.println("OWNERS: " + owners);
        player.incrementTrainsRemaining(-edge.getLength());
        player.setLongestRoute(this);
        player.setTrainScore(this);
        player.setRouteScore(this);
        return true;
    }

    private boolean canAddOwner(Edge edge, Player player) {
        // 'contains' doesn't seem to be working very well
        boolean alreadyOwns = false;
        for (Player owner: owners.get(edge)) {
            if (owner.equals(player)) {
                alreadyOwns = true;
                break;
            }
        }

        return edge.getLength() <= player.getTrainsRemaining()
                && !alreadyOwns
                && owners.get(edge).size() < edge.getWidth();
    }

    public void clearEdge(Edge edge) {
        Set<Player> oldOwners = owners.get(edge);
        owners.get(edge).clear();
        for (Player player : oldOwners) {
            player.incrementTrainsRemaining(edge.getLength());
            player.setLongestRoute(this);
            player.setTrainScore(this);
            player.setRouteScore(this);
        }
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
        System.out.println("PLAYER EDGES: " + getEdges(player));
        for (Edge edge : getEdges(player)) {
            score += edge.getValue();
        }
        System.out.println("Returning train score " + score);
        return score;
    }

    public int getRouteScore(Player player) {
        int score = 0;
        Map<City, Set<City>> ccs = getCCs(player);
        System.out.println("Calculating route score with cards: " + player.getCards());
        for (RouteCard card : player.getCards()) {
            // Check whether the route succeeded, noting if they failed ccs might not contain the card
            int success = 0;
            if (ccs.get(card.getFirstCity()) == null || !ccs.get(card.getFirstCity()).contains(card.getSecondCity())) {
                success = -1;
            } else {
                success = 1;
            }

            score += card.getLength() * success;
        }
        System.out.println("Returning route score " + score);
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
//        System.out.println("OWNERS WHEN GETTING EDGES: " + owners);
//        System.out.println("Player being tested: " + player);
        List<Edge> edges = new ArrayList<Edge>();
        for (Edge edge : owners.keySet()) {
            for (Player owner: owners.get(edge)) {
//                System.out.println("Comparing player " + player + " with owner " + owner);
                if (player.equals(owner)) {
                    edges.add(edge);
                }
            }
//            System.out.println("Edge: " + edge + "; owners: " + owners.get(edge) +
//                    "; contains player? " + owners.get(edge).contains(player));
//            if (owners.get(edge).contains(player)) {
//                edges.add(edge);
//            }
        }
        return edges;
    }

    public int countOwners(Edge edge) {
        return owners.get(edge).size();
    }

    public Map<Player, Scores> getAllScores(List<Player> players) {
        Set<Player> longestRoutes = calculateLongestRoutes(players);
        Map<Player, Scores> scores = new HashMap<>();
        for (Player player : players) {
            scores.put(player, new Scores(getRouteScore(player), getTrainScore(player), longestRoutes.contains(player) ? 10 : 0));
        }
        return scores;
    }

    private Set<Player> calculateLongestRoutes(List<Player> players) {
        Set<Player> best = new HashSet<>();
        int length = 0;
        for (Player player : players) {
            int playerLength = getLongestRouteLength(player);
            if (playerLength > length) {
                best.clear();
            }
            if (playerLength >= length) {
                best.add(player);
            }
        }
        return best;
    }
}
