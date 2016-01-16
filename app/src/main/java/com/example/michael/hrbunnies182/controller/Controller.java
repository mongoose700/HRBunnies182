package com.example.michael.hrbunnies182.controller;

import android.app.Activity;

import com.example.michael.hrbunnies182.game.Model;
import com.example.michael.hrbunnies182.game.PlayerColor;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Derek on 1/16/2016.
 */
public class Controller implements Serializable {

    private final IViewToModelAdapter adapter;

    public Controller(Set<PlayerColor> playerColors, Activity activity) {
        Model model = new Model(playerColors, activity);
        adapter = model.getAdapter();
    }

    public IViewToModelAdapter getAdapter() {
        return adapter;
    }

}
