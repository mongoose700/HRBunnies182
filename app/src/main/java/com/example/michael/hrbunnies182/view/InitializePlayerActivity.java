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
import com.example.michael.hrbunnies182.game.PlayerColor;
import com.example.michael.hrbunnies182.game.RouteCard;

import java.util.ArrayList;
import java.util.List;

public class InitializePlayerActivity extends AppCompatActivity {

    private Player currentPlayer;
    private List<Player> remainingPlayers;
    private Draw currentDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Creating InitializePlayerActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initialize_player);

        final Controller gameController = ((MyApplication) this.getApplication()).getGame();

        System.out.println("Obtained game controller");
        remainingPlayers = new ArrayList<>();
        remainingPlayers.addAll(gameController.getAdapter().getPlayers());
        final TextView playerName = (TextView) findViewById(R.id.textViewPlayerName);
        nextPlayer(playerName);
        gameController.getAdapter().setPlayer(currentPlayer);

        final Intent selectPlayerActivity = new Intent(this, com.example.michael.hrbunnies182.view.SelectPlayerActivity.class);

        final LinearLayout[] cards = new LinearLayout[3];
        cards[0] = ((LinearLayout) findViewById(R.id.checkBoxInitialCard1));
        cards[1] = ((LinearLayout) findViewById(R.id.checkBoxInitialCard2));
        cards[2] = ((LinearLayout) findViewById(R.id.checkBoxInitialCard3));

        Button thatsMe = (Button) findViewById(R.id.buttonMe);
        final Button keep = (Button) findViewById(R.id.buttonKeepInitialDraw);
        thatsMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Draw cards
                currentDraw = gameController.getAdapter().getNewDraw();
                for (int i = 0; i < 3; i++) {
                    final CheckedRouteCard card = currentDraw.getCards().get(i);
                    final LinearLayout layout = cards[i];
                    ((TextView) layout.getChildAt(0)).setText(card.getCard().toString());
                    ((TextView) layout.getChildAt(1)).setText(String.valueOf(card.getCard().getLength()));
                    cards[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            card.setChecked(!card.isChecked());
                            int color = card.isChecked() ? R.color.darkBrown : R.color.lightBrown;
                            layout.setBackgroundResource(color);
                            int savedCount = 0;
                            for (CheckedRouteCard card : currentDraw.getCards())
                                if (card.isChecked())
                                    savedCount++;
                            keep.setEnabled(savedCount >= 2);
                            keep.setBackgroundResource(keep.isEnabled() ? R.color.darkBrown : R.color.lightBrown);
                        }
                    });
                }

                // Go to draw screen
                findViewById(R.id.layoutHandOffPhone).setVisibility(View.GONE);
                findViewById(R.id.layoutDrawInitialCards).setVisibility(View.VISIBLE);
                for (int k = 0; k < 3; k++) cards[k].setBackgroundResource(R.color.lightBrown);
                keep.setBackgroundResource(R.color.lightBrown);
            }
        });
        keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameController.getAdapter().makeChoice(currentDraw);

                // Display hand
                TextView hand = (TextView) findViewById(R.id.textViewHandCards);
                String cardsText = "";
                for (RouteCard card : gameController.getAdapter().getPlayer().getCards()) {
                    cardsText += card + "\n";
                }
                hand.setText(cardsText);

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
            playerName.setBackgroundResource(getBackgroundColor(currentPlayer.getColor()));
            return true;
        }
        return false;
    }

    private int getBackgroundColor(PlayerColor color) {
        switch (color) {
            case RED: return R.color.red;
            case BLUE: return R.color.blue;
            case BLACK: return R.color.black;
            case YELLOW: return R.color.yellow;
            case GREEN: return R.color.green;
        }
        throw new NullPointerException();
    }


    @Override
    public void onBackPressed() {
    }
}
