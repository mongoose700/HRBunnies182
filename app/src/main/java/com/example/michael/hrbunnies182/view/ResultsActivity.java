package com.example.michael.hrbunnies182.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.michael.hrbunnies182.MyApplication;
import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.controller.Controller;
import com.example.michael.hrbunnies182.game.Player;
import com.example.michael.hrbunnies182.game.PlayerColor;
import com.example.michael.hrbunnies182.game.Scores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity {

    private Controller gameController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        gameController = ((MyApplication) this.getApplication()).getGame();

        Map<Player, Scores> scores = gameController.getAdapter().getAllScores();

//        System.out.println("Scores: " + scores);

        List<Player> sortedPlayers = sorted(scores);

//        System.out.println("Sorted players: " + sortedPlayers);

        Button ok = (Button) findViewById(R.id.buttonOkToPlayers);
        List<TextView> rankings = new ArrayList<>();
        rankings.add((TextView) findViewById(R.id.first));
        rankings.add((TextView) findViewById(R.id.second));
        rankings.add((TextView) findViewById(R.id.third));
        rankings.add((TextView) findViewById(R.id.fourth));
        rankings.add((TextView) findViewById(R.id.fifth));

        for (int i = 0; i < 5; i++) {
            TextView view = rankings.get(i);
            if (sortedPlayers.size() <= i) {
                view.setVisibility(View.GONE);
            } else {
                view.setText((i + 1) + ": " + scores.get(sortedPlayers.get(i)));
                int color;
//                System.out.println("Player color: " + sortedPlayers.get(i).getColor());
                switch (sortedPlayers.get(i).getColor()) {
                    case RED: color = Color.RED; break;
                    case BLUE: color = Color.BLUE; break;
                    case GREEN: color = Color.GREEN; break;
                    case YELLOW: color = Color.YELLOW; break;
                    default: color = Color.BLACK;
                }
                view.setBackgroundColor(color);
            }
        }

        final Activity me = this;

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(me, com.example.michael.hrbunnies182.Start.class);
                startActivity(start);
            }
        });
    }

    private List<Player> sorted(Map<Player, Scores> scores) {
        List<Player> players = new ArrayList<>(scores.keySet());
        List<Player> sortedPlayers = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            int best = scores.get(players.get(i)).getTotalScore();
            int bestIndex = i;
            for (int j = i + 1; j < scores.size(); j++) {
                if (scores.get(players.get(j)).getTotalScore() > best) {
                    best = scores.get(players.get(j)).getTotalScore();
                    bestIndex = j;
                }
            }
            sortedPlayers.add(players.get(bestIndex));
            players.set(bestIndex, players.get(i));
        }
        return sortedPlayers;
    }

    @Override
    public void onBackPressed() {
    }
}
