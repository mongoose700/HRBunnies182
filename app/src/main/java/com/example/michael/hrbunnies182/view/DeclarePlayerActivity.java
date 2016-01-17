package com.example.michael.hrbunnies182.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.michael.hrbunnies182.MyApplication;
import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.controller.Controller;
import com.example.michael.hrbunnies182.game.Player;
import com.example.michael.hrbunnies182.game.PlayerColor;

import java.io.Serializable;
import java.util.Collections;

public class DeclarePlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare_player);

        Bundle extras = getIntent().getExtras();
        final int playerIndex = extras.getInt("player_num");

        final Controller gameController = ((MyApplication) this.getApplication()).getGame();
        gameController.getAdapter().setPlayer(gameController.getAdapter().getPlayers().get(playerIndex));

        Player player = gameController.getAdapter().getPlayers().get(playerIndex);
        String playerColorName = player.getColor().toString();
        String newColorName = Character.toUpperCase(playerColorName.charAt(0)) + playerColorName.substring(1).toLowerCase();

        TextView playerTitle = (TextView) findViewById(R.id.toolbar_title_player_name);
        playerTitle.setText(newColorName + " Player");

        TextView playerName = (TextView) findViewById(R.id.textViewPlayerName);
        playerName.setBackgroundResource(getBackgroundColor(player.getColor()));
        playerName.setText(newColorName);


        Button thatsMe = (Button) findViewById(R.id.buttonMe);
        thatsMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent drawCardsActivity = new Intent(DeclarePlayerActivity.this, com.example.michael.hrbunnies182.view.DrawCardsKeepSomeActivity.class);
                drawCardsActivity.putExtra("player_num", playerIndex);
                drawCardsActivity.putExtra("card_min", 2);
                drawCardsActivity.putExtra("next_step_buttons", (Serializable) Collections.singletonMap("Continue", new ViewHandActivity.InitializeNextPlayer((playerIndex + 1))));
                startActivity(drawCardsActivity);
            }
        });
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
