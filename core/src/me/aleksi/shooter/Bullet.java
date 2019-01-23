package me.aleksi.shooter;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

final class Bullet extends Entity {
    private static final String TEXTURE_FILE = "bullet.png";

    private static final float SPEED = 0.2f;

    private Texture texture;

    private Vector2 direction;

    Bullet(ShooterGame game, Vector2 direction) {
        super(game);
        this.direction = direction.cpy().nor();
        texture = game.getAssets().get(TEXTURE_FILE, Texture.class);
        getRect().setSize((float) texture.getWidth() / texture.getHeight(), 1.0f);
    }

    static void loadAssets(AssetManager assets) {
        assets.load(TEXTURE_FILE, Texture.class);
    }

    private float getRotation() {
        return -direction.angle(Vector2.Y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture,
                getX(),
                getY(),
                0f,
                0f,
                1f * getRect().getAspectRatio(),
                1f,
                1f,
                1f,
                getRotation(),
                0,
                0,
                texture.getWidth(),
                texture.getHeight(),
                false,
                false);
    }

    @Override
    public void update(float deltaTime) {
        moveX(deltaTime * direction.x);
        moveY(deltaTime * direction.y);
    }
}
