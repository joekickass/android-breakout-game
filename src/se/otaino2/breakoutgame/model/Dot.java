package se.otaino2.breakoutgame.model;

import android.graphics.Color;
import android.graphics.Rect;

public class Dot extends Entity {

    public Dot(int posX, int posY, int width, int height) {
        super(posX, posY, width, height, Color.BLACK);
    }
    
    public void offset(int dx, int dy) {
        Rect rect = getRect();
        rect.offset(dx, dy);
    }
}
