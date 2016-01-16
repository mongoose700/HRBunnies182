package com.example.michael.hrbunnies182.game;

import android.util.Pair;

import java.util.Map;
import java.util.Set;

/**
 * Keeps track of which players have which edges
 */
public class ScoreMap {
    private Map<Pair<City, City>, Set<Player>> owners;
    private Map<Pair<City, City>, Integer> width;


}
