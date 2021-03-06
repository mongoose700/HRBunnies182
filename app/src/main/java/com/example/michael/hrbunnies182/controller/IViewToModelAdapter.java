package com.example.michael.hrbunnies182.controller;

import android.graphics.Point;

import com.example.michael.hrbunnies182.game.City;
import com.example.michael.hrbunnies182.game.Draw;
import com.example.michael.hrbunnies182.game.Edge;
import com.example.michael.hrbunnies182.game.Player;
import com.example.michael.hrbunnies182.game.Scores;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Adapter from View to Model
 */
public interface IViewToModelAdapter extends Serializable {

    /** Returns the players in the game */
    List<Player> getPlayers();

    /** Returns a new Draw of up to three cards from the deck of available cards
     * for the given Player to choose */
    Draw getNewDraw();

    /** Accepts a Draw returned by getNewDraw, with certain cards now designated as kept */
    void makeChoice(Draw choice);

    /** Sets the current player */
    void setPlayer(Player player);

    /** Returns the current player */
    Player getPlayer();

    /** Adds the trains of a certain player to the edge on the board, and returns the edge created */
    Edge addEdge(Player player, Point city1, Point city2);

    /** Removes all trains from this edge on the board, returning the cleared edge */
    Edge clearEdge(Point city1, Point city2);

    /** Checks how many people own this edge */
    int countOwners(Edge edge);

    Collection<City> getAllCities();

    Map<Player, Scores> getAllScores();
}
