package com.example.michael.hrbunnies182.textsim;

import com.example.michael.hrbunnies182.game.Model;
import com.example.michael.hrbunnies182.game.PlayerColor;

import java.util.EnumSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Derek on 1/16/2016.
 */
public class TextSimulator {

    private Scanner scan = new Scanner(System.in);

    public static void main() {
        //new TextSimulator().start();
    }

    public void start() {
        Model model = createModel();
    }

    private Model createModel() {
        System.out.println("Choose colors [RGBYK]");
        String input = scan.nextLine().toLowerCase();
        Set<PlayerColor> colors = EnumSet.noneOf(PlayerColor.class);
        if (input.contains("r")) {
            colors.add(PlayerColor.RED);
        }
        if (input.contains("g")) {
            colors.add(PlayerColor.GREEN);
        }
        if (input.contains("b")) {
            colors.add(PlayerColor.BLUE);
        }
        if (input.contains("y")) {
            colors.add(PlayerColor.YELLOW);
        }
        if (input.contains("k")) {
            colors.add(PlayerColor.BLACK);
        }
        System.out.println("Colors: " + colors);
        //return new Model(colors);
        return null;
    }

}
