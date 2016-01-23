package com.example.michael.hrbunnies182.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.game.City;
import com.example.michael.hrbunnies182.game.Draw;
import com.example.michael.hrbunnies182.game.RouteCard;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Jesse on 1/20/16 to experiment with better scaling.
 */
public class RouteCardCreator2 {
    private static RouteCardCreator2 ourInstance = new RouteCardCreator2();

    public static RouteCardCreator2 getInstance() {
        return ourInstance;
    }

    private RouteCardCreator2() {
    }

    public FrameLayout getRouteCard(String name, Collection<City> cities,
                                    int value, Context context, Resources resources, Resources.Theme theme) {
        FrameLayout frame = new FrameLayout(context);
        ImageView image = new ImageView(context);

        Drawable myDrawable = context.getResources().getDrawable(R.drawable.destination_card_2);
        image.setImageBitmap(((BitmapDrawable) myDrawable).getBitmap());

        // Get the card's dimensions for scaling
        int cardWidth = ((BitmapDrawable) image.getDrawable()).getBitmap().getWidth();
        int cardHeight = ((BitmapDrawable) image.getDrawable()).getBitmap().getHeight();

        // Add the cities
        frame.addView(image);
        for (City city : cities)
            frame.addView(getCityDot(city, context, image));

        // Create the title box
        RelativeLayout rl2 = new RelativeLayout(context);
        RelativeLayout.LayoutParams rl2params = new RelativeLayout.LayoutParams(cardWidth - 80 * (cardWidth / 292), 60);
        rl2params.leftMargin = 80 * (cardWidth / 292);
        rl2params.topMargin = 25 * (cardHeight / 196);

        TextView cardTitle = new TextView(context);
        cardTitle.setText(name);
        cardTitle.setTextColor(resources.getColor(R.color.redFont));
        rl2.addView(cardTitle, rl2params);
        frame.addView(rl2);

        // Add the card value
        TextView cardValue = new TextView(context);
        cardValue.setText(String.valueOf(value));
        cardValue.setTextSize(30);
        cardValue.setTextColor(resources.getColor(R.color.redFont));

        System.out.println("TextView size: " + cardValue.getWidth() + ", " + cardValue.getHeight());

        RelativeLayout rl3 = new RelativeLayout(context);
        RelativeLayout.LayoutParams rl3params = new RelativeLayout.LayoutParams(125, 100);
        rl3params.leftMargin = value >= 10 ? 223 * (cardWidth / 292) : 233 * (cardWidth / 292);
        rl3params.topMargin = 125 * (cardHeight / 196);
        rl3.addView(cardValue, rl3params);
        frame.addView(rl3);

        return frame;

    }

    public RelativeLayout getCityDot(City city, Context context, ImageView cardView) {
        ImageView icon = new ImageView(context);
        // Some existing RelativeLayout from your layout xml
        RelativeLayout rl1 = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 30);

        // Get the card's dimensions for scaling
        int cardWidth = ((BitmapDrawable) cardView.getDrawable()).getBitmap().getWidth();
        int cardHeight = ((BitmapDrawable) cardView.getDrawable()).getBitmap().getHeight();

        Point coords = city.getCardCoordinates();

        params.leftMargin = coords.x * cardWidth / 292 - 15;
        params.topMargin = coords.y * cardHeight / 196 - 15;

        rl1.addView(icon, params);
        icon.setBackgroundResource(R.drawable.destination_icon);
        return rl1;
    }

    public FrameLayout getRouteCard(RouteCard card, Activity activity) {
        return getRouteCard(card.getFirstCity() + " - " + card.getSecondCity(), Arrays.asList(card.getFirstCity(), card.getSecondCity()),
                card.getLength(), activity.getBaseContext(), activity.getResources(), activity.getTheme());
    }
}