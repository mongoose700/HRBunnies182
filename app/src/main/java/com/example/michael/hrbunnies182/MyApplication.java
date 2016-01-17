package com.example.michael.hrbunnies182;

import android.app.Activity;
import android.app.Application;

import com.example.michael.hrbunnies182.controller.Controller;
import com.example.michael.hrbunnies182.game.PlayerColor;

import java.util.Set;

/**
 * Created by Jenna on 1/16/2016.
 */
public class MyApplication extends Application {

    private Controller gameController;

    public void initializeGame(Set<PlayerColor> colors, Activity act) {
        System.out.println("Initializing game");
        gameController = new Controller(colors, act);
        System.out.println("Initialized game");
    }

    public Controller getGame() {
        return gameController;
    }
}