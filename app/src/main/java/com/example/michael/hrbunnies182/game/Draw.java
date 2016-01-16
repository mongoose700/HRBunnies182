package com.example.michael.hrbunnies182.game;

import java.util.ArrayList;
import java.util.Collections;
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

    public List<CheckedRouteCard> getCards() {
        return cards;
    }

    /** Gives the cards that the player did not decide to keep */
    public List<RouteCard> getReturnedCards() {
        List<RouteCard> returnedCards = new ArrayList<>(3 - mustKeep);
        for (CheckedRouteCard card : cards) {
            if (!card.isChecked()) {
                returnedCards.add(card.getCard());
            }
        }
        return returnedCards;
    }

    public Player getPlayer() {
        return player;
    }

    /** Returns true when the number of cards kept is at least mustKeep */
    public boolean isValid() {
        int kept = 0;
        for (CheckedRouteCard card : cards) {
            if (card.isChecked()) {
                kept++;
            }
        }
        return kept >= mustKeep;
    }

    public void giveCardsToPlayer() {
        for (CheckedRouteCard card : cards) {
            if (card.isChecked())
                player.addCards(Collections.singletonList(card.getCard()));
        }
    }
}
