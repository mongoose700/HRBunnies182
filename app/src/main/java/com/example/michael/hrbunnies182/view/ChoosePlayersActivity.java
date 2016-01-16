package com.example.michael.hrbunnies182.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.controller.Controller;
import com.example.michael.hrbunnies182.game.PlayerColor;

import java.util.HashMap;
import java.util.HashSet;

public class ChoosePlayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_players);

        Button ok = (Button) findViewById(R.id.buttonOkToPlayers);

        final Intent initializePlayerActivity = new Intent(this, com.example.michael.hrbunnies182.view.InitializePlayerActivity.class);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<Integer, PlayerColor> colorBoxes = new HashMap<>();
                colorBoxes.put(R.id.checkBoxRed, PlayerColor.RED);
                colorBoxes.put(R.id.checkBoxBlue, PlayerColor.BLUE);
                colorBoxes.put(R.id.checkBoxGreen, PlayerColor.GREEN);
                colorBoxes.put(R.id.checkBoxYellow, PlayerColor.YELLOW);
                colorBoxes.put(R.id.checkBoxBlack, PlayerColor.BLACK);

                HashSet<PlayerColor> activeColors = new HashSet<>();

                for (Integer id : colorBoxes.keySet()) {
                    if (((CheckBox) findViewById(id)).isChecked()) {
                        activeColors.add(colorBoxes.get(id));
                    }
                }

                Controller gameController = new Controller(activeColors, ChoosePlayersActivity.this);

                Bundle appData = new Bundle();
                appData.putSerializable("GAME_CONTROLLER", gameController);
                initializePlayerActivity.putExtra("APP_DATA", appData);

                startActivity(initializePlayerActivity);
            }
        });
    }
}
