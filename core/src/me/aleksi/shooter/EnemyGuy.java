package me.aleksi.shooter;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

final class EnemyGuy extends Entity {
    private static final String TEXTURE_FILE = "ghost.png";
    private static final float MAX_SPEED = 10.0f;

    private Texture texture;

    private Vector2 speed;

    EnemyGuy(ShooterGame game) {
        super(game);
        texture = game.getAssets().get(TEXTURE_FILE, Texture.class);
        getRect().setSize(1.0f, (float) texture.getHeight() / texture.getWidth());

        // Set  direction by random
        speed = new Vector2((float) (Math.random() * MAX_SPEED - MAX_SPEED / 2),
                (float) (Math.random() * MAX_SPEED - MAX_SPEED / 2));
        speed.nor();
        speed.scl(MAX_SPEED);
    }

    static void loadAssets(AssetManager assets) {
        assets.load(TEXTURE_FILE, Texture.class);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX(), getY(), getRect().getWidth(), getRect().getHeight());
    }

    @Override
    public void update(float deltaTime) {
        float x = getX();
        float y = getY();
        float w = getRect().getWidth();
        float h = getRect().getHeight();

        // Check if we've hit or gone past a wall, reverse if we have

        if (x < 0 || (x + w) > getGame().getWorldWidth()) {
            speed.x *= -1;
        }
        if (y < 0 || (y + h) > getGame().getWorldHeight()) {
            speed.y *= -1;
        }

        moveX(deltaTime * speed.x);
        moveY(deltaTime * speed.y);
    }
}
