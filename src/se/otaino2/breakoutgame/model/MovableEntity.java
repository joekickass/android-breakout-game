package se.otaino2.breakoutgame.model;

/**
 * This is a moving entity, which means something can update its position by calling the move method.
 * 
 * The main purpose of this class is to keep track of the actual position (with floating point precision) and correctly round it to non-floating point positions
 * when drawing.
 * 
 * @author otaino-2
 * 
 */
public class MovableEntity extends Entity {

    private double centerX;
    private double centerY;

    public MovableEntity(int posX, int posY, int width, int height, int color) {
        super(posX, posY, width, height, color);
        centerX = getRect().centerX();
        centerY = getRect().centerY();
    }

    public double getX() {
        return centerX;
    }

    public double getY() {
        return centerY;
    }

    public void move(double dx, double dy) {
        centerX += dx;
        centerY += dy;
        // Correctly round the position of the entity
        int moveX = (int) (centerX - getRect().centerX());
        int moveY = (int) (centerY - getRect().centerY());
        getRect().offset(moveX, moveY);
    }
}
