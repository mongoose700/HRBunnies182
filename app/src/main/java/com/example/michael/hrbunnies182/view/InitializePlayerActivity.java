package com.example.michael.hrbunnies182.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.controller.Controller;
import com.example.michael.hrbunnies182.game.Player;
import com.example.michael.hrbunnies182.game.PlayerColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InitializePlayerActivity extends AppCompatActivity {

    private Player currentPlayer;
    private List<Player> remainingPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initialize_player);

        Bundle appData = getIntent().getBundleExtra("APP_DATA");
        final Controller gameController = (Controller) appData.getSerializable("GAME_CONTROLLER");

        remainingPlayers = new ArrayList<>();
        remainingPlayers.addAll(gameController.getAdapter().getPlayers());
        final TextView playerName = (TextView) findViewById(R.id.textViewPlayerName);
        nextPlayer(playerName);

        final Intent selectPlayerActivity = new Intent(this, com.example.michael.hrbunnies182.view.SelectPlayerActivity.class);


        Button thatsMe = (Button) findViewById(R.id.buttonMe);
        thatsMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.layoutHandOffPhone).setVisibility(View.GONE);
                findViewById(R.id.layoutDrawInitialCards).setVisibility(View.VISIBLE);
            }
        });

        Button keep = (Button) findViewById(R.id.buttonKeepInitialDraw);
        keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.layoutDrawInitialCards).setVisibility(View.GONE);
                findViewById(R.id.layoutInitialHand).setVisibility(View.VISIBLE);
            }
        });

        Button ok = (Button) findViewById(R.id.buttonHandOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextPlayer(playerName)) {
                    findViewById(R.id.layoutInitialHand).setVisibility(View.GONE);
                    findViewById(R.id.layoutHandOffPhone).setVisibility(View.VISIBLE);
                }
                else {
                    Bundle newAppData = new Bundle();
                    newAppData.putSerializable("GAME_CONTROLLER", gameController);
                    selectPlayerActivity.putExtra("APP_DATA", newAppData);

                    startActivity(selectPlayerActivity);
                }
            }
        });
    }

    private boolean nextPlayer(TextView playerName) {
        if (remainingPlayers.size() > 0) {
            currentPlayer = remainingPlayers.remove(0);
            playerName.setText(currentPlayer.toString());
            return true;
        }
        return false;
    }
}
