package com.example.michael.hrbunnies182.view;

import android.annotation.TargetApi;
import android.content.Intent;
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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.michael.hrbunnies182.MyApplication;
import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.controller.Controller;
import com.example.michael.hrbunnies182.game.City;
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

    private final HashMap<PlayerColor, Integer> colorMap = new HashMap<>();

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

            Point loc1 = getAdjustedPoint(e1);
            Point loc2 = getAdjustedPoint(e2);

//            System.out.println("LISTENER: Got points " + loc1 + ", " + loc2);

            if (curPlayer != null) {
//                System.out.println("Asking the controller to clear an edge!");
//                Edge clearedEdge = gameController.getAdapter().clearEdge(loc1, loc2);
//                if (clearedEdge != null) {
//                    System.out.println("Clearing edge " + clearedEdge);
//                    removeEdgeFromScreen(clearedEdge);
//                    for (Player player : activePlayers.values()) {
//                        resetScore(player);
//                    }
//                }
//            } else {
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
//        System.out.println("Adding an edge to the screen: " + edge);
        ImageView mapView = (ImageView) findViewById(R.id.imageView);
//        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        int mapWidth = ((BitmapDrawable) mapView.getDrawable()).getBitmap().getWidth();
        int mapHeight = ((BitmapDrawable) mapView.getDrawable()).getBitmap().getHeight();

        Bitmap bitmap = Bitmap.createBitmap(mapWidth, mapHeight,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(colorMap.get(curPlayer.getColor()));

        canvas.drawBitmap(((BitmapDrawable) mapView.getDrawable()).getBitmap(), 0, 0, new Paint());

        // Check which train to use from this edge
        int numOwners = gameController.getAdapter().countOwners(edge);
        List<Train> trainList = edge.getTrains().get(numOwners - 1);

        for (Train train: trainList) {
            float centerX = (float) (train.getCoordinates().x * mapWidth / 624.0);
            float centerY = (float) (train.getCoordinates().y * mapHeight / 417.0);
//            System.out.println("Drawing a rectangle at " + centerX + ", " + centerY);
//            System.out.println("Scaling by " + mapWidth / 624.0 + " and " + mapHeight / 417.0);
            RectF trainCar = new RectF(centerX - 10, centerY - 25, centerX + 10, centerY + 25);
            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.rotate((float) train.getTheta(), centerX, centerY);
            canvas.drawRect(trainCar, paint);
            canvas.restore();
        }

        mapView.setImageBitmap(bitmap);
//        LayerDrawable drawable = new LayerDrawable(new Drawable[] { });

//        surfaceView.draw(canvas);
    }

    /**
     * Remove the given edge from the view
     * @param edge
     */
    private void removeEdgeFromScreen(Edge edge) {
        ImageView mapView = (ImageView) findViewById(R.id.imageView);

        int mapWidth = ((BitmapDrawable) mapView.getDrawable()).getBitmap().getWidth();
        int mapHeight = ((BitmapDrawable) mapView.getDrawable()).getBitmap().getHeight();

        Bitmap bitmap = Bitmap.createBitmap(mapWidth, mapHeight,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.TRANSPARENT);

        canvas.drawBitmap(((BitmapDrawable) mapView.getDrawable()).getBitmap(), 0, 0, new Paint());

        for (List<Train> trainList: edge.getTrains()) {
            for (Train train : trainList) {
                float centerX = (float) (train.getCoordinates().x * mapWidth / 624.0);
                float centerY = (float) (train.getCoordinates().y * mapHeight / 417.0);
//                System.out.println("Drawing a rectangle at " + centerX + ", " + centerY);
//                System.out.println("Scaling by " + mapWidth / 624.0 + " and " + mapHeight / 417.0);
                RectF trainCar = new RectF(centerX - 10, centerY - 25, centerX + 10, centerY + 25);
                canvas.save(Canvas.MATRIX_SAVE_FLAG);
                canvas.rotate((float) train.getTheta(), centerX, centerY);
                canvas.drawRect(trainCar, paint);
                canvas.restore();
            }
        }

        mapView.setImageBitmap(bitmap);
    }


    /**
     * Reset this player's score and trains remaining
     */
    private void resetScore(Player player) {
//        System.out.println("Resetting score for player " + player);
        switch (player.getColor()) {
            case BLACK:
                ((TextView) findViewById(R.id.scoreBlack)).setText("Trains left: " + player.getTrainsRemaining() + "  ");
                break;
            case BLUE:
//                System.out.println("Actually resetting score!");
                ((TextView) findViewById(R.id.scoreBlue)).setText("Trains left: " + player.getTrainsRemaining() + "  ");
                break;
            case GREEN:
                ((TextView) findViewById(R.id.scoreGreen)).setText("Trains left: " + player.getTrainsRemaining() + "  ");
                break;
            case RED:
                ((TextView) findViewById(R.id.scoreRed)).setText("Trains left: " + player.getTrainsRemaining() + "  ");
                break;
            case YELLOW:
                ((TextView) findViewById(R.id.scoreYellow)).setText("Trains left: " + player.getTrainsRemaining() + "  ");
                break;
        }
    }

    /**
     * Adjust for scaling and the map corner
     * @param e A MotionEvent
     * @return The location of the event relative to the map
     */
    private Point getAdjustedPoint(MotionEvent e) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Rect r = new Rect();
        Point offset = new Point(0, 0);
        ImageView mapView = (ImageView) findViewById(R.id.imageView);
        mapView.getGlobalVisibleRect(r, offset);
//        System.out.println("Adjusted height scale: " + ((size.y - offset.y) / 417.0));

        return new Point((int) (e.getX() / ((size.y - offset.y) / 417.0)), (int) ((e.getY() - offset.y) / ((size.y - offset.y) / 417.0)));
    }

    private final GestureDetectorCompat wrapper = new GestureDetectorCompat(getBaseContext(), listener);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoring_map);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Pair player colors with Android color IDs
        colorMap.put(PlayerColor.RED, Color.RED);
        colorMap.put(PlayerColor.BLUE, Color.BLUE);
        colorMap.put(PlayerColor.GREEN, Color.GREEN);
        colorMap.put(PlayerColor.YELLOW, Color.YELLOW);
        colorMap.put(PlayerColor.BLACK, Color.BLACK);

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

        Button ok = (Button) findViewById(R.id.buttonDone);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent results = new Intent(EnterScoresActivity.this, com.example.michael.hrbunnies182.view.ResultsActivity.class);
                startActivity(results);
            }
        });
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

//            case R.id.radioClear:
//                if (checked)
//                    curPlayer = null;
//                break;
        }
    }

}
