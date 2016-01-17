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
import com.example.michael.hrbunnies182.game.RouteCard;

import java.util.ArrayList;

/**
 * Created by Jenna on 1/16/2016.
 */
public class MakeDrawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_cards);

        final Intent viewHandActivity = new Intent(this, com.example.michael.hrbunnies182.view.ViewHandActivity.class);

        Bundle appData = getIntent().getBundleExtra("APP_DATA");
        final Controller gameController = (Controller) appData.getSerializable("GAME_CONTROLLER");
        final Player currentPlayer = (Player) appData.getSerializable("CURRENT_PLAYER");

        ((TextView) findViewById(R.id.textViewPlayerName)).setText(currentPlayer.toString());

        final CheckBox[] checkboxes = new CheckBox[3];
        checkboxes[0] = ((CheckBox) findViewById(R.id.checkBoxInitialCard1));
        checkboxes[1] = ((CheckBox) findViewById(R.id.checkBoxInitialCard2));
        checkboxes[2] = ((CheckBox) findViewById(R.id.checkBoxInitialCard3));

        final Draw currentDraw = gameController.getAdapter().getNewDraw(currentPlayer, 1);
        for (int i = 0; i < 3; i++) {
            if (currentDraw.getCards().size() <= i) {
                checkboxes[i].setVisibility(View.GONE);
            }
            else {
                checkboxes[i].setVisibility(View.VISIBLE);
                checkboxes[i].setText(currentDraw.getCards().get(i).getCard().toString());
            }
        }

        Button keep = (Button) findViewById(R.id.buttonKeepDraw);

        final Activity me = this;

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
                if (saved.size() < 1) {
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

                System.out.println(currentPlayer.getCards().size() + " SIZE");

                // Exit

                Bundle newAppData = new Bundle();
                newAppData.putSerializable("GAME_CONTROLLER", gameController);
                newAppData.putSerializable("CURRENT_PLAYER", currentPlayer);
                viewHandActivity.putExtra("APP_DATA", newAppData);

                startActivity(viewHandActivity);

                me.finish();
            }
        });
    }
}