package com.example.michael.hrbunnies182.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michael.hrbunnies182.MyApplication;
import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.controller.Controller;
import com.example.michael.hrbunnies182.game.CheckedRouteCard;
import com.example.michael.hrbunnies182.game.Draw;
import com.example.michael.hrbunnies182.game.Player;
import com.example.michael.hrbunnies182.game.RouteCard;

import java.io.Serializable;
import java.util.Map;

public class DrawCardsKeepSomeActivity extends AppCompatActivity {

    Draw currentDraw;
    Controller gameController;
    Button keep;
//    final LinearLayout[] cards = new LinearLayout[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_cards_keep_some);

        final int playerIndex = getIntent().getExtras().getInt("player_num");
        final int cardMin = getIntent().getExtras().getInt("card_min");
        final Map<String, ViewHandActivity.NextStep> nextStepMap =
                (Map<String, ViewHandActivity.NextStep>) getIntent().getExtras().getSerializable("next_step_buttons");


        gameController = ((MyApplication) this.getApplication()).getGame();

        Player player = gameController.getAdapter().getPlayer();
        String playerColorName = player.getColor().toString();
        String newColorName = Character.toUpperCase(playerColorName.charAt(0)) + playerColorName.substring(1).toLowerCase();

        TextView playerTitle = (TextView) findViewById(R.id.toolbar_title_player_name_draw);
        playerTitle.setText(newColorName + " Player");

        currentDraw = gameController.getAdapter().getNewDraw();
//        cards[0] = ((LinearLayout) findViewById(R.id.checkBoxInitialCard1));
//        cards[1] = ((LinearLayout) findViewById(R.id.checkBoxInitialCard2));
//        cards[2] = ((LinearLayout) findViewById(R.id.checkBoxInitialCard3));
        keep = (Button) findViewById(R.id.buttonKeepInitialDraw);
        keep.setEnabled(false);
        keep.setBackgroundResource(R.color.lightBrown);
        TextView instructions = (TextView) findViewById(R.id.textViewChoose2Cards);
        instructions.setText("Choose at least " + (cardMin == 1 ? "1 new card" : cardMin + " cards") + " to keep");
        LinearLayout scrolledLayout = (LinearLayout) findViewById(R.id.route_images_drawing);
        for (int i = 0; i < 3; i++) {
            final CheckedRouteCard card = currentDraw.getCards().get(i);
            // TODO: Switch back to RouteCardCreator
            final FrameLayout drawnCard = RouteCardCreator2.getInstance().getRouteCard(card.getCard(), this);
            drawnCard.setAlpha(0.5f);
//                    ((TextView) layout.getChildAt(0)).setText(card.getCard().getFirstCity().getName() + " - " + card.getCard().getSecondCity().getName());
//            ((TextView) layout.getChildAt(1)).setText(String.valueOf(card.getCard().getLength()));
            scrolledLayout.addView(drawnCard);
            drawnCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    card.setChecked(!card.isChecked());
                    int color = card.isChecked() ? R.color.darkBrown : R.color.lightBrown;
                    drawnCard.setAlpha(card.isChecked() ? 1f : 0.5f);
//                    layout.setBackgroundResource(color);
                    int savedCount = 0;
                    for (CheckedRouteCard card : currentDraw.getCards())
                        if (card.isChecked())
                            savedCount++;
                    keep.setEnabled(savedCount >= cardMin);
                    keep.setBackgroundResource(keep.isEnabled() ? R.color.darkBrown : R.color.lightBrown);
                }
            });
        }
        for (RouteCard card : player.getCards()) {
            scrolledLayout.addView(RouteCardCreator2.getInstance().getRouteCard(card, this));
        }

        keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameController.getAdapter().makeChoice(currentDraw);

                if (cardMin == 1) {
                    Intent viewHandActivity = new Intent(DrawCardsKeepSomeActivity.this, com.example.michael.hrbunnies182.view.ViewHandActivity.class);
                    viewHandActivity.putExtra("next_step_buttons", (Serializable) nextStepMap);
                    startActivity(viewHandActivity);
                } else {
                    nextStepMap.get("Continue").takeNextStep(DrawCardsKeepSomeActivity.this);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
    }
}
