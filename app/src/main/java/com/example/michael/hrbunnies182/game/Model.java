package com.example.michael.hrbunnies182.game;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Derek on 1/15/2016.
 */
public class Model {

    private Deck deck;
    private List<Player> players;
    private GameMap map;

    public Model(Set<PlayerColor> colors) {
        deck = new Deck();
        players = colors.stream().map(Player::new).collect(Collectors.toList());
        Collections.shuffle(players);
        map = new GameMap();
    }

    public IViewToModelAdapter getAdapter() {
        return new IViewToModelAdapter() {
            @Override
            public List<Player> getPlayers() {
                return players;
            }

            @Override
            public Draw getNewDraw(Player player) {
                return null;
            }

            @Override
            public void makeChoice(Draw choice) {

            }
        };
    }


}
