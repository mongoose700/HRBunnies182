package com.example.michael.hrbunnies182.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michael on 1/15/2016.
 */
public class Player implements Serializable {
    private final PlayerColor color;
    private final List<RouteCard> cards;
    private int score;
    private String name;
    private String pin;
    private int trainsRemaining;

    public Player(PlayerColor color) {
        this.score = 0;
        this.color = color;
        this.cards = new ArrayList<>();
        this.name = color.name();
        this.pin = null;
        this.trainsRemaining = 45;
    }

    public void addCards(List<RouteCard> addedCards) {
        cards.addAll(addedCards);
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

    public PlayerColor getColor() { return color; }

    public boolean hasPin() {
        return pin != null;
    }

    public boolean verifyPin(String attempt) {
        return attempt.equals(name);
    }

    public String toString() {
        return name;
    }

    public int getTrainsRemaining() {
        return trainsRemaining;
    }

    public void incrementTrainsRemaining(int delta) {
        trainsRemaining += delta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (score != player.score) return false;
        if (trainsRemaining != player.trainsRemaining) return false;
        if (color != player.color) return false;
        if (cards != null ? !cards.equals(player.cards) : player.cards != null) return false;
        if (name != null ? !name.equals(player.name) : player.name != null) return false;
        return !(pin != null ? !pin.equals(player.pin) : player.pin != null);

    }

    @Override
    public int hashCode() {
        int result = color != null ? color.hashCode() : 0;
        result = 31 * result + (cards != null ? cards.hashCode() : 0);
        result = 31 * result + score;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (pin != null ? pin.hashCode() : 0);
        result = 31 * result + trainsRemaining;
        return result;
    }
}
