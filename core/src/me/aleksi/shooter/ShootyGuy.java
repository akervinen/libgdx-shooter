package me.aleksi.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

final class ShootyGuy extends Entity {
    private static final String TEXTURE_FILE = "thonk.png";
    private static final float MOVE_SPEED = 6.0f;

    private Texture texture;

    private boolean movingUp = false;
    private boolean movingDown = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;

    private Vector2 moveVec = new Vector2();

    ShootyGuy(ShooterGame game) {
        super(game);
        texture = game.getAssets().get(TEXTURE_FILE, Texture.class);
        getRect().setSize(1.0f, (float) texture.getHeight() / texture.getWidth());
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
        moveVec.setZero();

        if (isMovingUp()) {
            moveVec.y += 1;
        }
        if (isMovingDown()) {
            moveVec.y -= 1;
        }
        if (isMovingLeft()) {
            moveVec.x -= 1;
        }
        if (isMovingRight()) {
            moveVec.x += 1;
        }
        moveVec.nor();
        moveVec.scl(deltaTime * MOVE_SPEED);

        setPos(getX() + moveVec.x, getY() + moveVec.y);
    }

    public void onCollision(EnemyGuy e) {
        Gdx.app.log("GSG", "crash");
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
