package me.aleksi.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

final class ShootyGuy extends Entity {
    private static final String TEXTURE_FILE = "thonk.png";
    private static final float MOVE_SPEED = 6.0f; // m/s
    private static final float TURN_SPEED = (float) Math.PI; // rad/s

    private Texture texture;
    private float textureRotation = (float) Math.PI / 2; // radians

    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private float rotation; // radians
    private Vector2 moveVec = new Vector2();

    private boolean movingUp = false;
    private boolean movingDown = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean turningLeft = false;
    private boolean turningRight = false;
    private boolean shooting = false;

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
        for (Bullet b : bullets) {
            b.draw(batch);
        }

        batch.draw(texture,
                getX(),
                getY(),
                getRect().getWidth() / 2,
                getRect().getHeight() / 2,
                1f,
                1f / getRect().getAspectRatio(),
                1f,
                1f,
                (float) Math.toDegrees(textureRotation + rotation),
                0,
                0,
                texture.getWidth(),
                texture.getHeight(),
                false,
                false);
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
        if (isTurningLeft()) {
            rotation += deltaTime * TURN_SPEED;
        }
        if (isTurningRight()) {
            rotation -= deltaTime * TURN_SPEED;
        }
        moveVec.nor();
        moveVec.scl(deltaTime * MOVE_SPEED);
        moveVec.rotateRad(rotation);

        float newX = getX() + moveVec.x;
        float newY = getY() + moveVec.y;
        newX = MathUtils.clamp(newX, 0f, getGame().getWorldWidth() - getRect().getWidth());
        newY = MathUtils.clamp(newY, 0f, getGame().getWorldHeight() - getRect().getHeight());

        setPos(newX, newY);

        if (isShooting()) {
            shoot();
        }

        for (Bullet b : bullets) {
            b.update(deltaTime);
        }
    }

    private void shoot() {
        Bullet b = new Bullet(getGame(), Vector2.Y.cpy().rotateRad(rotation));
        b.getRect().setPosition(getRect().getCenter(new Vector2()));
        bullets.add(b);
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

    public boolean isTurningLeft() {
        return turningLeft;
    }

    public void setTurningLeft(boolean turningLeft) {
        this.turningLeft = turningLeft;
    }

    public boolean isTurningRight() {
        return turningRight;
    }

    public void setTurningRight(boolean turningRight) {
        this.turningRight = turningRight;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }
}
