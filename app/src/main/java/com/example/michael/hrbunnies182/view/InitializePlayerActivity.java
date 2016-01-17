package com.example.michael.hrbunnies182.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.controller.Controller;
import com.example.michael.hrbunnies182.game.CheckedRouteCard;
import com.example.michael.hrbunnies182.game.Draw;
import com.example.michael.hrbunnies182.game.Player;
import com.example.michael.hrbunnies182.game.PlayerColor;
import com.example.michael.hrbunnies182.game.RouteCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InitializePlayerActivity extends AppCompatActivity {

    private Player currentPlayer;
    private List<Player> remainingPlayers;
    private Draw currentDraw;

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

        final CheckBox [] checkboxes = new CheckBox[3];
        checkboxes[0] = ((CheckBox) findViewById(R.id.checkBoxInitialCard1));
        checkboxes[1] = ((CheckBox) findViewById(R.id.checkBoxInitialCard2));
        checkboxes[2] = ((CheckBox) findViewById(R.id.checkBoxInitialCard3));

        Button thatsMe = (Button) findViewById(R.id.buttonMe);
        thatsMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Draw cards
                currentDraw = gameController.getAdapter().getNewDraw(currentPlayer, 2);
                for (int i = 0; i < 3; i++) {
                    checkboxes[i].setText(currentDraw.getCards().get(i).getCard().toString());
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

                // Check for 2 saved cards
                ArrayList<String> saved = new ArrayList<String>();
                for (CheckBox box : checkboxes) {
                    if (box.isChecked()) {
                        saved.add(box.getText().toString());
                    }
                }
                if (saved.size() < 2) {
                    //TODO: alert that too few cards were taken
                    return;
                }
                for (int i = 0; i < 3; i++) {
                    checkboxes[i].setChecked(false);
                }
                for (String cardName : saved) {
                    for (CheckedRouteCard card : currentDraw.getCards()) {
                        if (cardName.equals(card.getCard().toString())) {
                            card.setChecked(true);
                        }
                    }
                }
                gameController.getAdapter().makeChoice(currentDraw);

                // Display hand
                TextView hand = (TextView) findViewById(R.id.textViewHandCards);
                String cards = "";
                for (RouteCard card : currentPlayer.getCards()) {
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
                    findViewById(R.id.layoutInitialHand).setVisibility(View.GONE);
                    findViewById(R.id.layoutHandOffPhone).setVisibility(View.VISIBLE);
                }
                else {
                    Bundle newAppData = new Bundle();
                    newAppData.putSerializable("GAME_CONTROLLER", gameController);
                    selectPlayerActivity.putExtra("APP_DATA", newAppData);

                    startActivity(selectPlayerActivity);

                    me.finish();
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
