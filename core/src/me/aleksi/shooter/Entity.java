package me.aleksi.shooter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

abstract class Entity {
    private ShooterGame game;
    private Rectangle rect = new Rectangle();

    Entity(ShooterGame game) {
        this.game = game;
    }

    public ShooterGame getGame() {
        return game;
    }

    public Rectangle getRect() {
        return rect;
    }

    public float getX() {
        return rect.x + rect.width / 2;
    }

    public float getY() {
        return rect.y + rect.height / 2;
    }

    public void setPos(float x, float y) {
        rect.setCenter(x, y);
    }

    public void moveX(float x) {
        rect.x += x;
    }

    public void moveY(float y) {
        rect.y += y;
    }

    public abstract void draw(SpriteBatch batch);

    public abstract void update(float deltaTime);

    public boolean collides(Entity e) {
        return this.getRect().overlaps(e.getRect());
    }
}
