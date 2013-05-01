package se.otaino2.breakoutgame.model;

import android.graphics.Color;
import android.graphics.Rect;

public class Paddle extends Entity {

    public Paddle(int posX, int posY, int width, int height) {
        super(posX, posY, width, height, Color.BLUE);
    }
    
    public void move(int position) {
        Rect rect = getRect();
        int dx = position - rect.centerX();
        rect.offset(dx, 0);
    }
}
