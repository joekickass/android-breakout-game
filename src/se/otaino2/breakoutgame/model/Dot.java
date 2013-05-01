package se.otaino2.breakoutgame.model;

import android.graphics.Color;

public class Dot extends MovableEntity {

    private double vx;
    private double vy;

    public Dot(int posX, int posY, int width, int height, double vx, double vy) {
        super(posX, posY, width, height, Color.BLACK);
        this.vx = vx;
        this.vy = vy;
    }
    
    public void move(double dx, double dy, double vx, double vy) {
        super.move(dx, dy);
        this.vx = vx;
        this.vy = vy;
    }
    
    public double getVx() {
        return vx;
    }
    
    public double getVy() {
        return vy;
    }
}
