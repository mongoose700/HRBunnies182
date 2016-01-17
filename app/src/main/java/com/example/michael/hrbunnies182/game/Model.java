package com.example.michael.hrbunnies182.game;

import android.app.Activity;
import android.graphics.Point;

import com.example.michael.hrbunnies182.controller.IViewToModelAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Derek on 1/15/2016.
 */
public class Model implements Serializable {

    private Deck deck;
    private List<Player> players;
    private GameMap gameMap;
    private ScoreMap scoreMap;
    private GameMap map;
    private Player curPlayer;

    public Model(Set<PlayerColor> colors, Activity activity) {
        gameMap = new GameMap("usa.xml", activity);
        deck = gameMap.getDeck();
        players = new ArrayList<>(colors.size());
        for (PlayerColor color : colors) {
            players.add(new Player(color));
        }
        Collections.shuffle(players);
        scoreMap = new ScoreMap(gameMap, new HashSet<>(players));
    }

    public IViewToModelAdapter getAdapter() {
        return new IViewToModelAdapter() {
            @Override
            public List<Player> getPlayers() {
                return players;
            }

            @Override
            public Draw getNewDraw() {
                return deck.drawCards();
            }

            @Override
            public void makeChoice(Draw choice) {
                deck.returnCards(choice);
                choice.giveCardsToPlayer(curPlayer);
            }

            @Override
            public void setPlayer(Player player) {
                curPlayer = player;
            }

            @Override
            public Player getPlayer() {
                return curPlayer;
            }

            @Override
            public void addEdge(Player player, Point city1, Point city2) {
                Edge edge = gameMap.findEdge(city1, city2);
                System.out.println("Model: Got edge " + edge);
                if (edge != null) {
                    System.out.println("Model: Adding edge " + edge + " to player " + player);
                    scoreMap.addOwner(edge, player);
                }
            }

            @Override
            public void clearEdge(Point city1, Point city2) {
                Edge edge = gameMap.findEdge(city1, city2);
                System.out.println("Model: Clearing edge " + edge);
                if (edge != null) {
                    scoreMap.clearEdge(edge);
                }
            }
        };
    }


}
