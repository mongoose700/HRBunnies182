package com.example.michael.hrbunnies182.game;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TabStopSpan;
import android.widget.TextView;

import com.example.michael.hrbunnies182.R;

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
        return "\tTrains: " + trainScore + "\n\tLongest Road: " + longestScore + "\n\tRoutes: " +
                routeScore + "\n\tTotal: " + totalScore;
    }

    public SpannableStringBuilder toSpan() {
        SpannableStringBuilder span = new SpannableStringBuilder("\tTrains: " + trainScore +
                "\n\tLongest Road: " + longestScore + "\n\tRoutes: " +
                routeScore + "\n\tTotal: " + totalScore);
        span.setSpan(new TabStopSpan.Standard(70), 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        return span;
    }


}
