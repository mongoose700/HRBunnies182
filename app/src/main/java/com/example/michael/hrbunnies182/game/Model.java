package com.example.michael.hrbunnies182.game;

import com.example.michael.hrbunnies182.controller.IViewToModelAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Derek on 1/15/2016.
 */
public class Model implements Serializable {

    private Deck deck;
    private List<Player> players;
    private GameMap map;

    public Model(Set<PlayerColor> colors) {
        map = new GameMap();
        deck = map.getDeck();
        players = new ArrayList<>(colors.size());
        for (PlayerColor color : colors) {
            players.add(new Player(color));
        }
        Collections.shuffle(players);
    }

    public IViewToModelAdapter getAdapter() {
        return new IViewToModelAdapter() {
            @Override
            public List<Player> getPlayers() {
                return players;
            }

            @Override
            public Draw getNewDraw(Player player, int numKept) {
                return deck.drawCards(player, numKept);
            }

            @Override
            public void makeChoice(Draw choice) {
                deck.returnCards(choice);
                choice.giveCardsToPlayer();
            }
        };
    }


}
