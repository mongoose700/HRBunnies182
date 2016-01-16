package com.example.michael.hrbunnies182.game;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Keeps track of which players have which edges
 */
public class ScoreMap {
    private Map<Edge, Set<Player>> owners;

    public ScoreMap(GameMap gameMap) {
        owners = new HashMap<>();
    }
}
