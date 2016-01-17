package com.example.michael.hrbunnies182.view;

import android.content.Context;
import android.graphics.Point;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.game.City;

import java.util.Collection;

/**
 * Created by Derek on 1/17/2016.
 */
public class RouteCardCreator {
    private static RouteCardCreator ourInstance = new RouteCardCreator();

    public static RouteCardCreator getInstance() {
        return ourInstance;
    }

    private RouteCardCreator() {
    }

    public FrameLayout getRouteCard(String name, Collection<City> cities, int value, Context context) {
        FrameLayout frame = new FrameLayout(context);
        ImageView image = new ImageView(context);
        image.setBackgroundResource(R.drawable.destination_card);
        frame.addView(image);
        for (City city : cities)
            frame.addView(getCityDot(city, context));
        RelativeLayout rl2 = new RelativeLayout(context);
        RelativeLayout.LayoutParams rl2params = new RelativeLayout.LayoutParams(350, 50);
//        rl2params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        rl2params.leftMargin = 140;
        rl2params.topMargin = 50;

        TextView cardTitle = new TextView(context);
        cardTitle.setText(name);
        rl2.addView(cardTitle, rl2params);
        frame.addView(rl2);

        TextView cardValue = new TextView(context);
        cardValue.setText(String.valueOf(value));
        cardValue.setTextSize(30);
        RelativeLayout rl3 = new RelativeLayout(context);
        RelativeLayout.LayoutParams rl3params = new RelativeLayout.LayoutParams(100, 100);
        rl3params.leftMargin = 460;
        rl3params.topMargin = 250;
        rl3.addView(cardValue, rl3params);
        frame.addView(rl3);

        return frame;

    }

    public RelativeLayout getCityDot(City city, Context context) {
        ImageView icon = new ImageView(context);
        // Some existing RelativeLayout from your layout xml
        RelativeLayout rl1 = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(20, 20);
        Point coords = city.getCoordinates();
        params.leftMargin = coords.x * 27 / 40 + 20;
        params.topMargin = coords.y * 27 / 40 + 60;
        rl1.addView(icon, params);
        icon.setBackgroundResource(R.drawable.destination_icon);
        return rl1;
    }
}
