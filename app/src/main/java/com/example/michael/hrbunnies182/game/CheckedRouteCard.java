package com.example.michael.hrbunnies182.game;

/**
 * Created by Michael on 1/15/2016.
 */
public class CheckedRouteCard {
    private boolean checked;
    private RouteCard card;

    public CheckedRouteCard(RouteCard card) {
        this.card = card;
        this.checked = false;
    }

    public boolean isChecked() {
        return checked;
    }

    public RouteCard getCard() {
        return card;
    }
}
