package se.otaino2.breakoutgame.model;

import android.graphics.Color;

public class Paddle extends MovableEntity {

    public Paddle(int posX, int posY, int width, int height) {
        super(posX, posY, width, height, Color.BLUE);
    }
    
    public void move(double position) {
        double dx = position - getX();
        super.move(dx, 0);
    }
}
