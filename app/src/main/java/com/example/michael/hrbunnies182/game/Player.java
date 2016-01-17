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
    private String name;
    private String pin;
    private int trainsRemaining;
    private int longestRoute;
    private int trainScore;

    public Player(PlayerColor color) {
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

    public int getLongestRoute() {
        return longestRoute;
    }

    public void setLongestRoute(ScoreMap scoreMap) {
        this.longestRoute = scoreMap.getLongestRouteLength(this);
    }

    public int getTrainScore() {
        return trainScore;
    }

    public void setTrainScore(ScoreMap scoreMap) {
        this.trainScore = scoreMap.getTrainScore(this);
    }

    public int getRouteScore() {
        return trainScore;
    }

    public void getRouteScore(ScoreMap scoreMap) {
        this.trainScore = scoreMap.getRouteScore(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (trainsRemaining != player.trainsRemaining) return false;
        if (longestRoute != player.longestRoute) return false;
        if (trainScore != player.trainScore) return false;
        if (color != player.color) return false;
        if (cards != null ? !cards.equals(player.cards) : player.cards != null) return false;
        if (name != null ? !name.equals(player.name) : player.name != null) return false;
        return !(pin != null ? !pin.equals(player.pin) : player.pin != null);

    }

    @Override
    public int hashCode() {
        int result = color != null ? color.hashCode() : 0;
        result = 31 * result + (cards != null ? cards.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (pin != null ? pin.hashCode() : 0);
        result = 31 * result + trainsRemaining;
        result = 31 * result + longestRoute;
        result = 31 * result + trainScore;
        return result;
    }
}
