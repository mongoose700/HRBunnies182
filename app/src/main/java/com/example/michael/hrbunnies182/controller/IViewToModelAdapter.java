package com.example.michael.hrbunnies182.controller;

import com.example.michael.hrbunnies182.game.Draw;
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
    Draw getNewDraw();

    /** Accepts a Draw returned by getNewDraw, with certain cards now designated as kept */
    void makeChoice(Draw choice);

    /** Sets the current player */
    void setPlayer(Player player);

    /** Returns the current player */
    Player getPlayer();
}
