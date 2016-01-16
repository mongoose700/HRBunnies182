package com.example.michael.hrbunnies182.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Michael on 1/15/2016.
 */
public class Draw {

    private final int mustKeep;
    private final Player player;
    private final List<CheckedRouteCard> cards;

    public Draw(List<CheckedRouteCard> cards, Player player, int mustKeep) {
        this.cards = cards;
        this.player = player;
        this.mustKeep = mustKeep;
    }

    /** Gives the cards that the player did not decide to keep */
    public List<RouteCard> getReturnedCards() {
        List<RouteCard> returnedCards = new ArrayList<>(3 - mustKeep);
        for (CheckedRouteCard card : cards) {
            if (card.isChecked()) {
                returnedCards.add(card.getCard());
            }
        }
        return returnedCards;
    }

    public Player getPlayer() {
        return player;
    }
}
