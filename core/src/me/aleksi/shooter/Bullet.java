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
        getRect().setSize(.5f * (float) texture.getWidth() / texture.getHeight(), .5f);
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

        return x < -1 || y < -1 || x + w > getGame().getWorldWidth() + 1 || y + h > getGame().getWorldHeight() + 1;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture,
                getX(),
                getY(),
                0f,
                0f,
                getRect().getWidth(),
                getRect().getHeight(),
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
