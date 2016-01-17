package com.example.michael.hrbunnies182.view;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;

import com.example.michael.hrbunnies182.MyApplication;
import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.controller.Controller;
import com.example.michael.hrbunnies182.game.Player;
import com.example.michael.hrbunnies182.game.PlayerColor;

import java.util.HashMap;

/**
 * Created by Jesse on 1/16/2016.
 *
 * An activity which displays the gameboard and sends clicks back
 * to the score map to calculate players' scores.
 */
public class EnterScoresActivity extends AppCompatActivity {

    private Player curPlayer = null;

    private final HashMap<PlayerColor, Player> activePlayers = new HashMap<>();

    private Controller gameController;

    // Set up a listener on the viewscreen for touches
    private final GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            System.out.println("LISTENER: Got an 'onDown' MotionEvent at location (" + e.getX() + ", " + e.getY() + ")");
            return true;
        }

        /**
         * Record the two locations of the endpoints of the swipe.  Ignore velocity.
         * @param e1 The start of the swipe
         * @param e2 The end of the swipe
         * @param velocityX Ignored
         * @param velocityY Ignored
         * @return True
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            System.out.println("LISTENER: Received an event moving from (" +
                    e1.getX() + ", " + e1.getY() + ") to (" + e2.getX() + ", " + e2.getY() + ")");

            if (curPlayer == null) {
                System.out.println("Asking the controller to clear an edge!");
                gameController.getAdapter().clearEdge(new Point((int) e1.getX(), (int) e1.getY()),
                        new Point((int) e2.getX(), (int) e2.getY()));
            } else {
                System.out.println("Asking the controller to add an edge!");
                gameController.getAdapter().addEdge(curPlayer, new Point((int) e1.getX(), (int) e1.getY()),
                        new Point((int) e2.getX(), (int) e2.getY()));
            }

            return true;
        }
    };

    private final GestureDetectorCompat wrapper = new GestureDetectorCompat(getBaseContext(), listener);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoring_map);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Assemble a list of all players paired with their colors
        gameController = ((MyApplication) this.getApplication()).getGame();

        for (Player player : gameController.getAdapter().getPlayers()) {
            activePlayers.put(player.getColor(), player);
        }
    }

    // Forward touches to the gestureDetector
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.wrapper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    // Update the current player when the button is pressed
    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

//        System.out.println("ENTERSCORES: Resetting current player based on ID " + view.getId() + " and checked " + checked);
//        System.out.println("ENTERSCORES: Colors: " + activePlayers);

        switch (view.getId()) {
            case R.id.radioBlack:
                if (checked)
                    curPlayer = activePlayers.get(PlayerColor.BLACK);
                break;

            case R.id.radioBlue:
                if (checked)
                    curPlayer = activePlayers.get(PlayerColor.BLUE);
                break;

            case R.id.radioRed:
                if (checked)
                    curPlayer = activePlayers.get(PlayerColor.RED);
                break;

            case R.id.radioYellow:
                if (checked)
                    curPlayer = activePlayers.get(PlayerColor.YELLOW);
                break;

            case R.id.radioGreen:
                if (checked)
                    curPlayer = activePlayers.get(PlayerColor.GREEN);
                break;

            case R.id.radioClear:
                if (checked)
                    curPlayer = null;
                break;
        }
    }

}
