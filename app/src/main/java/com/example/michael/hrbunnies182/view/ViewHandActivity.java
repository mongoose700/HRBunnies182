package com.example.michael.hrbunnies182.view;

import android.app.Activity;
import android.content.Intent;
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
import com.example.michael.hrbunnies182.game.RouteCard;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Jenna on 1/16/2016.
 */
public class ViewHandActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_hand);

        onRestart();
    }

    protected void onRestart() {

        super.onRestart();

        final Controller gameController = ((MyApplication) this.getApplication()).getGame();
        final Player currentPlayer = gameController.getAdapter().getPlayer();

        ((TextView) findViewById(R.id.textViewPlayerName)).setText(currentPlayer.toString());

        String cards = "";
        for (RouteCard card : currentPlayer.getCards()) {
            cards += card.toString() + "\n";
        }
        ((TextView) findViewById(R.id.textViewPlayerHandCards)).setText(cards);

        Button back = (Button) findViewById(R.id.buttonBackPlayerHand);
        Button draw = (Button) findViewById(R.id.buttonDrawCards);

        final Intent makeDrawActivity = new Intent(this, com.example.michael.hrbunnies182.view.MakeDrawActivity.class);
        final Intent selectPlayerActivity = new Intent(this, com.example.michael.hrbunnies182.view.SelectPlayerActivity.class);
        final Activity me = this;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me.finish();
            }
        });

        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(makeDrawActivity);
            }
        });
    }
}
