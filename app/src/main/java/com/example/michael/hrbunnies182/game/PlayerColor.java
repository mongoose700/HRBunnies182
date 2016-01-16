package com.example.michael.hrbunnies182.game;

import java.awt.Color;

/**
 * Created by Derek on 1/15/2016.
 */
public enum PlayerColor {
    RED(Color.RED), BLUE(Color.BLUE), YELLOW(Color.YELLOW), GREEN(Color.GREEN), BLACK(Color.BLACK);

    private final Color color;

    private PlayerColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

}
