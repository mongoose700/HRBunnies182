package com.example.michael.hrbunnies182.game;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Michael on 1/15/2016.
 */
public class Deck implements Serializable {
    private Queue<RouteCard> cards;

    public Deck(List<RouteCard> cardList) {
        this.cards = new ArrayDeque<>(cardList);
    }

    /** Removes (up to) three cards from the deck, and puts them in a Draw */
    public Draw drawCards() {
        ArrayList<CheckedRouteCard> drawnCards = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            if (cards.size() == 0) break;
            drawnCards.add(new CheckedRouteCard(cards.poll()));
        }
        return new Draw(drawnCards);
    }

    public void returnCards(Draw draw) {
        cards.addAll(draw.getReturnedCards());
    }

    public int numCards() {
        return cards.size();
    }

    public String toString() {
        StringBuilder msg = new StringBuilder();
        for (RouteCard card: cards) {
            msg.append(card.toString() + "\n");
        }
        return msg.toString();
    }
}
