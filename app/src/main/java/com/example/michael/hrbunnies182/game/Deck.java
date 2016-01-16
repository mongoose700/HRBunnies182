package com.example.michael.hrbunnies182.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 1/15/2016.
 */
public class Deck {
    private List<RouteCard> cards;

    public Deck(List<RouteCard> cards) {
        this.cards = cards;
    }

    /** Removes (up to) three cards from the deck, and puts them in a Draw */
    public Draw drawCards(Player player, int mustKeep) {
        ArrayList<CheckedRouteCard> drawnCards = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            if (cards.size() == 0) break;
            drawnCards.add(new CheckedRouteCard(cards.remove(0)));
        }
        return new Draw(drawnCards, player, mustKeep);
    }

    public void returnCards(Draw draw) {
        cards.addAll(draw.getReturnedCards());
    }
}
