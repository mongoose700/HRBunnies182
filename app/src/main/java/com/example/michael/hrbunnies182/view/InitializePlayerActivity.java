package com.example.michael.hrbunnies182.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michael.hrbunnies182.MyApplication;
import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.controller.Controller;
import com.example.michael.hrbunnies182.game.CheckedRouteCard;
import com.example.michael.hrbunnies182.game.Draw;
import com.example.michael.hrbunnies182.game.Player;
import com.example.michael.hrbunnies182.game.RouteCard;

import java.util.ArrayList;
import java.util.List;

public class InitializePlayerActivity extends AppCompatActivity {

    private Player currentPlayer;
    private List<Player> remainingPlayers;
    private Draw currentDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initialize_player);

        final Controller gameController = ((MyApplication) this.getApplication()).getGame();

        remainingPlayers = new ArrayList<>();
        remainingPlayers.addAll(gameController.getAdapter().getPlayers());
        final TextView playerName = (TextView) findViewById(R.id.textViewPlayerName);
        nextPlayer(playerName);
        gameController.getAdapter().setPlayer(currentPlayer);

        final Intent selectPlayerActivity = new Intent(this, com.example.michael.hrbunnies182.view.SelectPlayerActivity.class);

        final LinearLayout[] cards = new LinearLayout[3];
        cards[0] = ((LinearLayout) findViewById(R.id.checkBoxInitialCard1));
        final boolean[] checked = new boolean[3];

        Button thatsMe = (Button) findViewById(R.id.buttonMe);
        thatsMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Draw cards
                currentDraw = gameController.getAdapter().getNewDraw();
                for (int i = 0; i < 3; i++) {
                    ((TextView) cards[i].getChildAt(0)).setText(currentDraw.getCards().get(i).getCard().toString());
                }

                // Go to draw screen
                findViewById(R.id.layoutHandOffPhone).setVisibility(View.GONE);
                findViewById(R.id.layoutDrawInitialCards).setVisibility(View.VISIBLE);
            }
        });

        Button keep = (Button) findViewById(R.id.buttonKeepInitialDraw);
        keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int savedCount = 0;
                for (CheckedRouteCard card : currentDraw.getCards())
                    if (card.isChecked())
                        savedCount++;
                if (savedCount < 2) {
                    //TODO: alert that too few cards were taken
                    return;
                }
                gameController.getAdapter().makeChoice(currentDraw);

                // Display hand
                TextView hand = (TextView) findViewById(R.id.textViewHandCards);
                String cards = "";
                for (RouteCard card : gameController.getAdapter().getPlayer().getCards()) {
                    cards += card + "\n";
                }
                hand.setText(cards);

                // Go to hand screen
                findViewById(R.id.layoutDrawInitialCards).setVisibility(View.GONE);
                findViewById(R.id.layoutInitialHand).setVisibility(View.VISIBLE);
            }
        });

        final Activity me = this;

        Button ok = (Button) findViewById(R.id.buttonHandOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextPlayer(playerName)) {
                    gameController.getAdapter().setPlayer(currentPlayer);
                    findViewById(R.id.layoutInitialHand).setVisibility(View.GONE);
                    findViewById(R.id.layoutHandOffPhone).setVisibility(View.VISIBLE);
                }
                else {
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
