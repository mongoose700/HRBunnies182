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

        Bundle appData = getIntent().getBundleExtra("APP_DATA");
        final Controller gameController = (Controller) appData.getSerializable("GAME_CONTROLLER");
        final Player currentPlayer = (Player) appData.getSerializable("CURRENT_PLAYER");

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

                Bundle newAppData = new Bundle();
                newAppData.putSerializable("GAME_CONTROLLER", gameController);
                selectPlayerActivity.putExtra("APP_DATA", newAppData);

                startActivity(selectPlayerActivity);

                me.finish();
            }
        });

        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle newAppData = new Bundle();
                newAppData.putSerializable("GAME_CONTROLLER", gameController);
                newAppData.putSerializable("CURRENT_PLAYER", currentPlayer);
                makeDrawActivity.putExtra("APP_DATA", newAppData);

                startActivity(makeDrawActivity);

                me.finish();
            }
        });
    }
}
