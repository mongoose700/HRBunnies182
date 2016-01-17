package com.example.michael.hrbunnies182.game;

/**
 * Created by Michael on 1/17/2016.
 */
public class Scores {
    private int totalScore;
    private int routeScore;
    private int trainScore;
    private int longestScore;

    public Scores(int routeScore, int trainScore, int longestScore) {
        this.routeScore = routeScore;
        this.trainScore = trainScore;
        this.longestScore = longestScore;
        this.totalScore = routeScore + trainScore + longestScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    @Override
    public String toString() {
        return trainScore + " + " + longestScore + " + " + routeScore + " = " + totalScore;
    }
}
