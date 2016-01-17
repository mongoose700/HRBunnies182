package com.example.michael.hrbunnies182.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.michael.hrbunnies182.MyApplication;
import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.game.PlayerColor;

import java.util.HashMap;
import java.util.HashSet;

public class ChoosePlayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_players);

        Button ok = (Button) findViewById(R.id.buttonOkToPlayers);

        final Activity me = this;

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

                for (int id : colorBoxes.keySet()) {
                    if (((CheckBox) findViewById(id)).isChecked()) {
                        activeColors.add(colorBoxes.get(id));
                    }
                }

                if (activeColors.size() == 0) {
                    //TODO: Create alert (must have at least one player)
                    return;
                }

                MyApplication app = ((MyApplication) me.getApplication());
                app.initializeGame(activeColors, me);
                Intent declarePlayerActivity = new Intent(me, com.example.michael.hrbunnies182.view.DeclarePlayerActivity.class);
                declarePlayerActivity.putExtra("player_num", 0);
                System.out.println("Put extra player_num 0");
                startActivity(declarePlayerActivity);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
