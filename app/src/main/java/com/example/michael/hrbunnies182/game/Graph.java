package com.example.michael.hrbunnies182.game;

import android.util.SparseIntArray;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 1/16/2016.
 */
public class Graph {
    private Map<Integer, SparseIntArray> nodes;
    private Map<String, Integer> ids;

    public Graph() {
        nodes = new HashMap<>();
        ids = new HashMap<>();
    }

    public void addEdge(Edge edge) {
        String[] cities = new String[]{edge.getFirstCity().getName(), edge.getSecondCity().getName()};
        for (String city : cities) {
            if (!ids.containsKey(city)) {
                int id = ids.size();
                ids.put(city, id);
                nodes.put(id, new SparseIntArray());
            }
        }
        for (int i = 0; i < 2; i++) {
            nodes.get(ids.get(cities[i])).put(ids.get(cities[1 - i]), edge.getLength());
        }
    }

    public void optimize() {
        Integer[] nodeArray = nodes.keySet().toArray(new Integer[nodes.size()]);
        for (int node : nodeArray) {
            if (nodes.get(node).size() == 2) {
                int[] neighbors = new int[2];
                int length = 0;
                for (int i = 0; i < 2; i++) {
                    neighbors[i] = nodes.get(node).keyAt(i);
                    length += nodes.get(node).get(neighbors[i]);
                }
                for (int i = 0; i < 2; i++) {
                    nodes.get(neighbors[i]).put(neighbors[1 - i], length);
                    SparseIntArray otherNeighbors = nodes.get(neighbors[i]);
                    otherNeighbors.removeAt(otherNeighbors.indexOfKey(node));
                }
                nodes.remove(node);
            }
        }
    }

    public int longestRouteLength() {
        SparseIntArray neighbors = new SparseIntArray(nodes.size());
        for (int node : nodes.keySet()) {
            neighbors.put(node, 0);
        }
        return longestRemainingRoute(new BitSet(ids.size()), neighbors);
    }

    private int longestRemainingRoute(BitSet used, SparseIntArray neighbors) {
        int best = 0;
        for (int i = 0; i < neighbors.size(); i++) {
            int neighbor = neighbors.keyAt(i);
            if (used.get(neighbor)) continue;
            used.set(neighbor);
            int score = neighbors.get(neighbor) + longestRemainingRoute(used, nodes.get(neighbor));
            used.clear(neighbor);
            if (score > best) best = score;
        }
        return best;
    }
}
