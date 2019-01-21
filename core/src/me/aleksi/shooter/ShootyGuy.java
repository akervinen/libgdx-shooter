package me.aleksi.shooter;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

final class ShootyGuy extends Entity {
    private static final float MOVE_SPEED = 2.0f;
    private static final String TEXTURE_FILE = "thonk.png";
    private Texture texture;
    private boolean movingUp = false;
    private boolean movingDown = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;

    ShootyGuy(AssetManager assets) {
        texture = assets.get(TEXTURE_FILE, Texture.class);
    }

    static void loadAssets(AssetManager assets) {
        assets.load(TEXTURE_FILE, Texture.class);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX(), getY(), 1.0f, (float) texture.getHeight() / texture.getWidth());
    }

    @Override
    public void update(float deltaTime) {
        if (isMovingUp()) {
            setY(getY() + MOVE_SPEED * deltaTime);
        }
        if (isMovingDown()) {
            setY(getY() - MOVE_SPEED * deltaTime);
        }
        if (isMovingLeft()) {
            setX(getX() - MOVE_SPEED * deltaTime);
        }
        if (isMovingRight()) {
            setX(getX() + MOVE_SPEED * deltaTime);
        }
    }

    public boolean isMovingUp() {
        return movingUp;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public boolean isMovingDown() {
        return movingDown;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
}
