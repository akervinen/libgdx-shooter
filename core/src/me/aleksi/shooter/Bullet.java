package me.aleksi.shooter;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

final class Bullet extends Entity {
    private static final String TEXTURE_FILE = "bullet.png";

    private static final float SPEED = 10f;

    private Texture texture;

    private boolean dead;
    private Vector2 direction;

    Bullet(ShooterGame game, Vector2 direction) {
        super(game);
        this.direction = direction.cpy().nor();
        texture = game.getAssets().get(TEXTURE_FILE, Texture.class);
        getRect().setSize(.25f, .25f);
    }

    static void loadAssets(AssetManager assets) {
        assets.load(TEXTURE_FILE, Texture.class);
    }

    private float getRotation() {
        return -direction.angle(Vector2.Y);
    }

    public boolean isDead() {
        return dead || isOutOfBounds();
    }

    private boolean isOutOfBounds() {
        float x = getX();
        float y = getY();
        float w = getRect().getWidth();
        float h = getRect().getHeight();

        float tolerance = 1.0f;

        if (x < 0 - tolerance || x + w > getGame().getWorldWidth() + tolerance) {
            return true;
        } else if (y < 0 - tolerance || y + h > getGame().getWorldHeight() + tolerance) {
            return true;
        }

        return false;
    }

    @Override
    public void draw(SpriteBatch batch) {
        float aspect = (float) texture.getWidth() / texture.getHeight();
        float renderHeight = getRect().getHeight() / aspect;
        batch.draw(texture,
                getRect().x,
                getRect().y - renderHeight * 0.75f,
                getRect().getWidth() / 2,
                renderHeight * .75f + getRect().getHeight() / 2,
                getRect().getWidth(),
                renderHeight,
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
        moveX(deltaTime * SPEED * direction.x);
        moveY(deltaTime * SPEED * direction.y);
    }
}
