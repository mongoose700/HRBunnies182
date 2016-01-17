package com.example.michael.hrbunnies182.view;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

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
        @TargetApi(17)
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            System.out.println("LISTENER: Received an event moving from (" +
                    e1.getX() + ", " + e1.getY() + ") to (" + e2.getX() + ", " + e2.getY() + ")");

            FrameLayout frame = (FrameLayout) findViewById(R.id.layoutEnterScores);
            boolean foundFrame = false;
            View imgFrame = null;
            for (int i=0; i<(frame).getChildCount(); i++) {
                imgFrame = frame.getChildAt(i);
                if (imgFrame instanceof ImageView) {
                    foundFrame = true;
                    break;
                }
            }
            if (!foundFrame) {
                System.err.println("Unable to find map when entering scores!");
                return true;
            }

            System.out.println("LISTENER: Current image bounds " +
                    ((ImageView) imgFrame).getDrawable().getBounds());

            Point loc1 = getAdjustedPoint(e1);
            Point loc2 = getAdjustedPoint(e2);

            System.out.println("LISTENER: Got points " + loc1 + ", " + loc2);

            if (curPlayer == null) {
                System.out.println("Asking the controller to clear an edge!");
                gameController.getAdapter().clearEdge(loc1, loc2);
                for (Player player: activePlayers.values()) {
                    resetScore(player);
                }
            } else {
                System.out.println("Asking the controller to add an edge!");
                gameController.getAdapter().addEdge(curPlayer, loc1, loc2);

                // The current player is the only one whose points could change
                resetScore(curPlayer);

            }

            return true;
        }
    };

    /**
     * Reset this player's score and trains remaining
     */
    private void resetScore(Player player) {
        System.out.println("Resetting score for player " + player);
        switch (player.getColor()) {
            case BLACK:
                ((TextView) findViewById(R.id.scoreBlack)).setText(player.getTotalScore() +
                        "    " + player.getTrainsRemaining());
                break;
            case BLUE:
                System.out.println("Actually resetting score!");
                ((TextView) findViewById(R.id.scoreBlue)).setText(player.getTotalScore() +
                        "    " + player.getTrainsRemaining());
                break;
            case GREEN:
                ((TextView) findViewById(R.id.scoreGreen)).setText(player.getTotalScore() +
                        "    " + player.getTrainsRemaining());
                break;
            case RED:
                ((TextView) findViewById(R.id.scoreRed)).setText(player.getTotalScore() +
                        "    " + player.getTrainsRemaining());
                break;
            case YELLOW:
                ((TextView) findViewById(R.id.scoreYellow)).setText(player.getTotalScore() +
                        "    " + player.getTrainsRemaining());
                break;
        }
    }

    /**
     * Adjust for scaling and the map corner
     * @param e A MotionEvent
     * @return The location of the event relative to the map
     */
    private Point getAdjustedPoint(MotionEvent e) {
        // Offset the y-axis
        float newY = e.getY() - 70;

        // Offset the scaling
        double scale = 2.4;
        return new Point((int) (e.getX() / scale), (int) (newY / scale));
    }

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

        // Remove invalid buttons
        for (PlayerColor color: PlayerColor.values()) {
            if (!activePlayers.containsKey(color)) {
                switch (color) {
                    case BLACK:
                        findViewById(R.id.radioBlack).setVisibility(View.GONE);
                        findViewById(R.id.scoreBlack).setVisibility(View.GONE);
                        break;
                    case BLUE:
                        findViewById(R.id.radioBlue).setVisibility(View.GONE);
                        findViewById(R.id.scoreBlue).setVisibility(View.GONE);
                        break;
                    case GREEN:
                        findViewById(R.id.radioGreen).setVisibility(View.GONE);
                        findViewById(R.id.scoreGreen).setVisibility(View.GONE);
                        break;
                    case RED:
                        findViewById(R.id.radioRed).setVisibility(View.GONE);
                        findViewById(R.id.scoreRed).setVisibility(View.GONE);
                        break;
                    case YELLOW:
                        findViewById(R.id.radioYellow).setVisibility(View.GONE);
                        findViewById(R.id.scoreYellow).setVisibility(View.GONE);
                        break;
                }
            }
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
