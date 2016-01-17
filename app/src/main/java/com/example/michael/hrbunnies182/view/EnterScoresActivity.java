package com.example.michael.hrbunnies182.view;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.michael.hrbunnies182.R;

/**
 * Created by Jesse on 1/16/2016.
 *
 * An activity which displays the gameboard and sends clicks back
 * to the score map to calculate players' scores.
 */
public class EnterScoresActivity extends AppCompatActivity {

    private GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {

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

            return true;
        }
    };

    private GestureDetectorCompat wrapper = new GestureDetectorCompat(getBaseContext(), listener);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scoring_map);

    }

    // Forward touches to the gestureDetector
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.wrapper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

}
