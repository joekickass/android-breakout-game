package se.otaino2.breakoutgame.model;

import android.graphics.Paint;
import android.graphics.Rect;

public class Entity {
    private Rect rect;
    private Paint paint;

    public Entity(int posX, int posY, int width, int height, int color) {
        rect = new Rect(posX, posY, posX + width, posY + height);
        paint = new Paint();
        paint.setColor(color);
    }

    public Rect getRect() {
        return rect;
    }

    public Paint getPaint() {
        return paint;
    }

    public boolean isColliding(Entity entity) {
        Rect other = entity.getRect();
        return rect.intersects(other.left, other.top, other.right, other.bottom);
    }
}
