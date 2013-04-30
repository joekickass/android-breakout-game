package se.otaino2.breakoutgame.model;

import android.graphics.Color;

public class Block extends Entity {

    public Block(int posX, int posY, int width, int height) {
        super(posX, posY, width, height, Color.RED);
    }
}
