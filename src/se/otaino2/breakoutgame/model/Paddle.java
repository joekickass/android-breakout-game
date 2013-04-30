package se.otaino2.breakoutgame.model;

import android.graphics.Color;
import android.graphics.Rect;

public class Paddle extends Entity {

    public Paddle(int posX, int posY, int width, int height) {
        super(posX, posY, width, height, Color.BLUE);
    }
    
    public void offset(int dx) {
        Rect rect = getRect();
        rect.offset(dx, 0);
    }
}
