package com.example.michael.hrbunnies182.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.game.City;
import com.example.michael.hrbunnies182.game.RouteCard;

import java.util.Arrays;
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

    public FrameLayout getRouteCard(String name, Collection<City> cities, int value, Context context, Resources resources, Resources.Theme theme) {
        FrameLayout frame = new FrameLayout(context);
        ImageView image = new ImageView(context);
        image.setBackgroundResource(R.drawable.destination_card_2);
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
        cardTitle.setTextColor(resources.getColor(R.color.redFont));
        rl2.addView(cardTitle, rl2params);
        frame.addView(rl2);

        TextView cardValue = new TextView(context);
        cardValue.setText(String.valueOf(value));
        cardValue.setTextSize(30);
        cardValue.setTextColor(resources.getColor(R.color.redFont));
        RelativeLayout rl3 = new RelativeLayout(context);
        RelativeLayout.LayoutParams rl3params = new RelativeLayout.LayoutParams(100, 100);
        System.out.println("Size: " + image.getWidth() + " " + image.getHeight());
        rl3params.leftMargin = value >= 10 ? 450 : 467;
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

    public FrameLayout getRouteCard(RouteCard card, Activity activity) {
        return getRouteCard(card.getFirstCity() + " - " + card.getSecondCity(), Arrays.asList(card.getFirstCity(), card.getSecondCity()),
                card.getLength(), activity.getBaseContext(), activity.getResources(), activity.getTheme());
    }
}
