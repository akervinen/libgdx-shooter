package me.aleksi.shooter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

abstract class Entity {
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public abstract void draw(SpriteBatch batch);

    public abstract void update(float deltaTime);
}
