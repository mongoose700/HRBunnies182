package com.example.michael.hrbunnies182.view;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
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
import com.example.michael.hrbunnies182.game.Edge;
import com.example.michael.hrbunnies182.game.Player;
import com.example.michael.hrbunnies182.game.PlayerColor;
import com.example.michael.hrbunnies182.game.Train;

import java.util.HashMap;
import java.util.List;

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

            Point loc1 = getAdjustedPoint(e1);
            Point loc2 = getAdjustedPoint(e2);

//            System.out.println("LISTENER: Got points " + loc1 + ", " + loc2);

            if (curPlayer == null) {
                System.out.println("Asking the controller to clear an edge!");
                gameController.getAdapter().clearEdge(loc1, loc2);
                for (Player player: activePlayers.values()) {
                    resetScore(player);
                }
            } else {
                System.out.println("Asking the controller to add an edge!");
                Edge newEdge;
                if ((newEdge = gameController.getAdapter().addEdge(curPlayer, loc1, loc2)) != null) {
                    addEdgeToScreen(newEdge);
                }

                // The current player is the only one whose points could change
                resetScore(curPlayer);

            }

            return true;
        }
    };

    /**
     * Add an edge to the screen
     */
    private void addEdgeToScreen(Edge edge) {
        System.out.println("Adding an edge to the screen: " + edge);
        ImageView mapView = (ImageView) findViewById(R.id.imageView);

        Bitmap bitmap = Bitmap.createBitmap(((BitmapDrawable) mapView.getDrawable()).getBitmap().getWidth(),
                ((BitmapDrawable) mapView.getDrawable()).getBitmap().getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED); // TODO

        canvas.drawBitmap(((BitmapDrawable) mapView.getDrawable()).getBitmap(), 0, 0, new Paint());

        for (List<Train> trainList: edge.getTrains()) {
            for (Train train: trainList) {
                float centerX = (float) (train.getCoordinates().x * 3);
                float centerY = (float) (train.getCoordinates().y * 3);
//                System.out.println("Drawing a rectangle at " + centerX + ", " + centerY);
                RectF trainCar = new RectF(centerX - 10, centerY - 25, centerX + 10, centerY + 25);
                canvas.save(Canvas.MATRIX_SAVE_FLAG);
                canvas.rotate((float) train.getTheta(), centerX, centerY);
//                Matrix m = new Matrix();
//                m.setRotate((float) train.getTheta(), centerX, centerY);
//                m.mapRect(trainCar);
                canvas.drawRect(trainCar, paint);
                canvas.restore();
            }
        }

        mapView.setImageBitmap(bitmap);
//        mapView.setWillNotDraw(false);

    }

    /**
     * Reset this player's score and trains remaining
     */
    private void resetScore(Player player) {
//        System.out.println("Resetting score for player " + player);
        switch (player.getColor()) {
            case BLACK:
                ((TextView) findViewById(R.id.scoreBlack)).setText(player.getTotalScore() +
                        "    " + player.getTrainsRemaining());
                break;
            case BLUE:
//                System.out.println("Actually resetting score!");
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
