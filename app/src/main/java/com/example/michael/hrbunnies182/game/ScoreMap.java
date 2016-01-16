package com.example.michael.hrbunnies182.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Keeps track of which players have which edges
 */
public class ScoreMap {
    private Map<Edge, Set<Player>> owners;

    public ScoreMap(GameMap gameMap, Set<Player> players) {
        owners = new HashMap<>();
        for (Edge edge : gameMap.getEdges()) {
            owners.put(edge, new TreeSet<Player>());
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
        }
        owners.get(edge).clear();
    }

    public int getLongestRouteLength(Player player) {
        Graph g = createGraph(player);
        return 0;
    }

    public Graph createGraph(Player player) {
        return null;
    }
}
