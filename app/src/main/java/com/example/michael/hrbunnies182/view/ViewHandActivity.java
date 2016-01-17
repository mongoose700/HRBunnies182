package com.example.michael.hrbunnies182.view;

import android.app.Activity;
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
import com.example.michael.hrbunnies182.game.Player;
import com.example.michael.hrbunnies182.game.RouteCard;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jenna on 1/16/2016.
 */
public class ViewHandActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_hand);

        final Controller gameController = ((MyApplication) this.getApplication()).getGame();
        final Player currentPlayer = gameController.getAdapter().getPlayer();

        String playerColorName = currentPlayer.getColor().toString();
        String newColorName = Character.toUpperCase(playerColorName.charAt(0)) + playerColorName.substring(1).toLowerCase();

        TextView playerTitle = (TextView) findViewById(R.id.toolbar_title_player_name_hand);
        playerTitle.setText(newColorName + " Player");

//        String cards = "";
//        for (RouteCard card : currentPlayer.getCards()) {
//            cards += card.toString() + "\n";
//        }
//        ((TextView) findViewById(R.id.textViewPlayerHandCards)).setText(cards);

        LinearLayout routeCardImages = (LinearLayout) findViewById(R.id.route_images);
        for (RouteCard card : currentPlayer.getCards()) {
            FrameLayout frame = RouteCardCreator.getInstance().getRouteCard(card.getFirstCity() + " - " + card.getSecondCity(),Arrays.asList(card.getFirstCity(), card.getSecondCity()), card.getLength(), getBaseContext(), getResources(), getTheme());
            routeCardImages.addView(frame);
        }
//        LinearLayout buttons = (LinearLayout) findViewById(R.id.buttonList);

        final Map<String, NextStep> nextSteps = (Map<String, NextStep>) getIntent().getExtras().getSerializable("next_step_buttons");

//        for (final Map.Entry<String, NextStep> e : nextSteps.entrySet()) {
//            Button button = new Button(getBaseContext());
//            button.setText(e.getKey());
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    e.getValue().takeNextStep(ViewHandActivity.this);
//                }
//            });
//            buttons.addView(button);
//        }

        Map<String, Button> buttons = new HashMap<>();
        buttons.put("Back", (Button) findViewById(R.id.buttonBackPlayerHand));
        buttons.put("Draw", (Button) findViewById(R.id.buttonDrawCards));
        buttons.put("Continue", (Button) findViewById(R.id.buttonContinuePlayerHand));

        final Intent makeDrawActivity = new Intent(this, com.example.michael.hrbunnies182.view.MakeDrawActivity.class);
        final Intent selectPlayerActivity = new Intent(this, com.example.michael.hrbunnies182.view.SelectPlayerActivity.class);
        final Activity me = this;

        for (final Map.Entry<String, Button> b : buttons.entrySet()) {
            if (nextSteps.containsKey(b.getKey())) {
                b.getValue().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextSteps.get(b.getKey()).takeNextStep(ViewHandActivity.this);
                    }
                });
            } else {
                b.getValue().setVisibility(View.GONE);
            }
        }
    }

    public static interface NextStep extends Serializable {
        void takeNextStep(ViewHandActivity activity);
    }

    public static class InitializeNextPlayer implements NextStep {

        private int nextPlayerIndex;

        public InitializeNextPlayer(int nextPlayerIndex) {
            System.out.println("next player index: " + nextPlayerIndex);
            this.nextPlayerIndex = nextPlayerIndex;
        }

        @Override
        public void takeNextStep(ViewHandActivity activity) {

            MyApplication app = ((MyApplication) activity.getApplication());
            if (nextPlayerIndex >= app.getGame().getAdapter().getPlayers().size()) {

                Intent selectPlayerActivity = new Intent(activity.getApplicationContext(), com.example.michael.hrbunnies182.view.SelectPlayerActivity.class);
                activity.startActivity(selectPlayerActivity);
            } else {
                Intent declarePlayerActivity = new Intent(activity.getApplicationContext(), com.example.michael.hrbunnies182.view.DeclarePlayerActivity.class);
                declarePlayerActivity.putExtra("player_num", nextPlayerIndex);
                activity.startActivity(declarePlayerActivity);
            }
        }
    }

    public static class GoBack implements NextStep {
        @Override
        public void takeNextStep(ViewHandActivity activity) {
            activity.finish();
        }
    }

    public static class DrawMoreCards implements NextStep {
        @Override
        public void takeNextStep(ViewHandActivity activity) {
            Intent drawCardsActivity = new Intent(activity.getApplicationContext(), DrawCardsKeepSomeActivity.class);
            drawCardsActivity.putExtra("card_min", 1);
            drawCardsActivity.putExtra("next_step_buttons", (Serializable) Collections.singletonMap("Continue", new ViewHandActivity.InitializeNextPlayer(Integer.MAX_VALUE)));
            activity.startActivity(drawCardsActivity);
        }
    }
}
