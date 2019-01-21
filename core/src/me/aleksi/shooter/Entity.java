package me.aleksi.shooter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

abstract class Entity {
    public abstract void draw(SpriteBatch batch);

    public abstract void update(float deltaTime);
}
