package com.example.michael.hrbunnies182.view;

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

import java.io.Serializable;
import java.util.Map;

public class DrawCardsKeepSomeActivity extends AppCompatActivity {

    Draw currentDraw;
    Controller gameController;
    Button keep;
    final LinearLayout[] cards = new LinearLayout[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_cards_keep_some);

        final int playerIndex = getIntent().getExtras().getInt("player_num");
        final int cardMin = getIntent().getExtras().getInt("card_min");
        final Map<String, ViewHandActivity.NextStep> nextStepMap =
                (Map<String, ViewHandActivity.NextStep>) getIntent().getExtras().getSerializable("next_step_buttons");


        gameController = ((MyApplication) this.getApplication()).getGame();
        currentDraw = gameController.getAdapter().getNewDraw();
        cards[0] = ((LinearLayout) findViewById(R.id.checkBoxInitialCard1));
        cards[1] = ((LinearLayout) findViewById(R.id.checkBoxInitialCard2));
        cards[2] = ((LinearLayout) findViewById(R.id.checkBoxInitialCard3));
        keep = (Button) findViewById(R.id.buttonKeepInitialDraw);
        keep.setEnabled(false);

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
                    keep.setEnabled(savedCount >= cardMin);
                    keep.setBackgroundResource(keep.isEnabled() ? R.color.darkBrown : R.color.lightBrown);
                }
            });
        }

        keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameController.getAdapter().makeChoice(currentDraw);

                Intent viewHandActivity = new Intent(DrawCardsKeepSomeActivity.this, com.example.michael.hrbunnies182.view.ViewHandActivity.class);
                viewHandActivity.putExtra("next_step_buttons", (Serializable) nextStepMap);
                startActivity(viewHandActivity);

            }
        });
    }
}
