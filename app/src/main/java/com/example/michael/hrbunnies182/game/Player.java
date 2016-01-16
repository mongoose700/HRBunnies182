package com.example.michael.hrbunnies182.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michael on 1/15/2016.
 */
public class Player {
    private PlayerColor color;
    private List<RouteCard> cards;
    private int score;
    private String name;
    private String pin;

    public Player(PlayerColor color) {
        this.score = 0;
        this.color = color;
        this.cards = new ArrayList<>();
        this.name = color.name();
        this.pin = null;
    }

    public void addCards(List<RouteCard> cards) {
        cards.addAll(cards);
    }

    public List<RouteCard> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean hasPin() {
        return pin != null;
    }

    public boolean verifyPin(String attempt) {
        return attempt.equals(name);
    }
}
