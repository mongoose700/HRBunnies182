package com.example.michael.hrbunnies182.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.michael.hrbunnies182.R;
import com.example.michael.hrbunnies182.controller.Controller;
import com.example.michael.hrbunnies182.game.PlayerColor;

public class InitializePlayerActivity extends AppCompatActivity {

    private ViewSwitcher switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initialize_player);

        Bundle appData = getIntent().getBundleExtra("APP_DATA");
        Controller gameController = (Controller) appData.getSerializable("GAME_CONTROLLER");

        switcher = (ViewSwitcher) findViewById(R.id.profileSwitcher);

        TextView playerName = (TextView) findViewById(R.id.textViewPlayerName);
        playerName.setText("Red: " + gameController.getAdapter().getPlayers().size());

        Button thatsMe = (Button) findViewById(R.id.buttonMe);
        thatsMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switcher.showNext();
            }
        });

        Button keep = (Button) findViewById(R.id.buttonKeepInitialDraw);
        thatsMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switcher.showNext();
            }
        });
    }
}
