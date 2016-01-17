package com.example.michael.hrbunnies182.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.michael.hrbunnies182.MyApplication;
import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.controller.Controller;
import com.example.michael.hrbunnies182.game.Player;
import com.example.michael.hrbunnies182.game.PlayerColor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jenna on 1/16/2016.
 */
public class SelectPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_player_gameplay);

        final Intent viewHandActivity = new Intent(this, com.example.michael.hrbunnies182.view.ViewHandActivity.class);
        final Intent enterScoresActivity = new Intent(this, com.example.michael.hrbunnies182.view.EnterScoresActivity.class);
        final Controller gameController = ((MyApplication) this.getApplication()).getGame();

        HashMap<Integer, PlayerColor> colorButtons = new HashMap<>();
        colorButtons.put(R.id.buttonRedPlayer, PlayerColor.RED);
        colorButtons.put(R.id.buttonBluePlayer, PlayerColor.BLUE);
        colorButtons.put(R.id.buttonGreenPlayer, PlayerColor.GREEN);
        colorButtons.put(R.id.buttonYellowPlayer, PlayerColor.YELLOW);
        colorButtons.put(R.id.buttonBlackPlayer, PlayerColor.BLACK);

        final HashMap<PlayerColor, Player> activePlayers = new HashMap<>();
        for (Player player : gameController.getAdapter().getPlayers()) {
            activePlayers.put(player.getColor(), player);
        }

        final Activity me = this;

        for (int id : colorButtons.keySet()) {
            Button button = (Button) findViewById(id);
            if (!activePlayers.keySet().contains(colorButtons.get(id))) {
                button.setVisibility(View.GONE);
            }
            else {
                final Player curPlayer = activePlayers.get(colorButtons.get(id));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gameController.getAdapter().setPlayer(curPlayer);
                        Map<String, ViewHandActivity.NextStep> map = new HashMap<>();
                        map.put("Back", new ViewHandActivity.GoBack());
                        map.put("Draw", new ViewHandActivity.DrawMoreCards());
                        viewHandActivity.putExtra("next_step_buttons", (Serializable) map);
                        startActivity(viewHandActivity);
                    }
                });
            }
        }

        Button endButton = (Button) findViewById(R.id.buttonEndGame);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(enterScoresActivity);
            }
        });

    }


    @Override
    public void onBackPressed() {
    }
}