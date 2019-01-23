package me.aleksi.shooter;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

final class EnemyGuy extends Entity {
    private static final String TEXTURE_FILE = "ghost.png";
    private static final float MIN_SPEED = 3f;
    private static final float MAX_SPEED = 6f;

    private Texture texture;

    private Vector2 speed;

    EnemyGuy(ShooterGame game) {
        super(game);
        texture = game.getAssets().get(TEXTURE_FILE, Texture.class);
        getRect().setSize(1.0f, (float) texture.getHeight() / texture.getWidth());

        // Set  direction by random
        speed = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
        speed.nor();
        speed.scl(MathUtils.random(MIN_SPEED, MAX_SPEED));
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

        if (x < 0 && speed.x < 0) {
            speed.x *= -1;
        }
        if ((x + w) > getGame().getWorldWidth() && speed.x > 0) {
            speed.x *= -1;
        }
        if (y < 0 && speed.y < 0) {
            speed.y *= -1;
        }
        if ((y + h) > getGame().getWorldHeight() && speed.y > 0) {
            speed.y *= -1;
        }

        moveX(deltaTime * speed.x);
        moveY(deltaTime * speed.y);
    }
}
