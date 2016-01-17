package com.example.michael.hrbunnies182.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michael on 1/15/2016.
 */
public class Draw implements Serializable {

    private final List<CheckedRouteCard> cards;

    public Draw(List<CheckedRouteCard> cards) {
        this.cards = cards;
    }

    public List<CheckedRouteCard> getCards() {
        return cards;
    }

    /** Gives the cards that the player did not decide to keep */
    public List<RouteCard> getReturnedCards() {
        List<RouteCard> returnedCards = new ArrayList<>();
        for (CheckedRouteCard card : cards) {
            if (!card.isChecked()) {
                returnedCards.add(card.getCard());
            }
        }
        return returnedCards;
    }

    public void giveCardsToPlayer(Player player) {
        for (CheckedRouteCard card : cards) {
            if (card.isChecked())
                player.addCards(Collections.singletonList(card.getCard()));
        }
    }
}
