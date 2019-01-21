package me.aleksi.shooter;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

final class EnemyGuy extends Entity {
    private static final String TEXTURE_FILE = "ghost.png";
    private static final float MAX_SPEED = 3.0f;

    private Texture texture;

    private float speedX, speedY;

    EnemyGuy(ShooterGame game) {
        super(game);
        texture = game.getAssets().get(TEXTURE_FILE, Texture.class);
        getRect().setSize(1.0f, (float) texture.getHeight() / texture.getWidth());

        // Set speed and direction by random
        speedX = (float) (Math.random() * MAX_SPEED - MAX_SPEED / 2);
        speedY = (float) (Math.random() * MAX_SPEED - MAX_SPEED / 2);
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

        // Check if we've hit a wall, reverse if we have
        if (x < 0 || (x + w) > getGame().getWorldWidth()) {
            speedX *= -1;
        }
        if (y < 0 || (y + h) > getGame().getWorldHeight()) {
            speedY *= -1;
        }

        moveX(deltaTime * speedX);
        moveY(deltaTime * speedY);
    }
}
