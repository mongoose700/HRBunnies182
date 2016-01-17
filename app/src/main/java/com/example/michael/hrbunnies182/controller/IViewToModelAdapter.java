package com.example.michael.hrbunnies182.controller;

import android.graphics.Point;

import com.example.michael.hrbunnies182.game.Draw;
import com.example.michael.hrbunnies182.game.Edge;
import com.example.michael.hrbunnies182.game.Player;

import java.io.Serializable;
import java.util.List;

/**
 * Adapter from View to Model
 */
public interface IViewToModelAdapter extends Serializable {

    /** Returns the players in the game */
    List<Player> getPlayers();

    /** Returns a new Draw of up to three cards from the deck of available cards
     * for the given Player to choose */
    Draw getNewDraw(Player player, int numKept);

    /** Accepts a Draw returned by getNewDraw, with certain cards now designated as kept */
    void makeChoice(Draw choice);

    /** Adds the trains of a certain player to the edge on the board */
    void addEdge(Player player, Point city1, Point city2);

    /** Removes all trains from this edge on the board */
    void clearEdge(Point city1, Point city2);

    /** Gets the length of the longest route of the player */
    int getLengthOfLongestRoute(Player player);

    int getTrainScore(Player player);


}
