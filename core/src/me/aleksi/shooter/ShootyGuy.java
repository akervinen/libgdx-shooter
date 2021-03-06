package me.aleksi.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

final class ShootyGuy extends Entity {
    private static final String TEXTURE_FILE = "shootyguy.png";
    private static final String THRUSTERS_TEXTURE_FILE = "shootyguy-thrusters.png";
    private static final String SHOOT_SOUND_FILE = "shootyguy-blaster.ogg";
    private static final String DEATH_SOUND_FILE = "shootyguy-death.mp3";
    private static final String THRUSTER_SOUND_FILE = "shootyguy-thruster.ogg";
    private static final float THRUSTER_SOUND_VOLUME = 0.5f;
    private static final float TEXTURE_ROTATION = (float) Math.PI / 2; // radians
    private static final float MOVE_SPEED = 4.0f; // m/s
    private static final float TURN_SPEED = (float) Math.PI * 2; // rad/s
    private static final float SHOOT_DELAY = 0.5f; // seconds

    private Texture texture;
    private Texture thrustersTexture;
    private Sound shootySound;
    private Sound deathSound;
    private Sound thrusterSound;

    private float rotation; // radians
    private Vector2 moveVec = new Vector2();
    private float shootElapsed;

    private boolean dead = false;
    private float deadTimer = 0.0f;

    private boolean movingForwards = false;
    private boolean movingBackwards = false;
    private boolean turningLeft = false;
    private boolean turningRight = false;
    private boolean shooting = false;

    private boolean hasTargetDir = false;
    private Vector2 targetDir = new Vector2();

    ShootyGuy(ShooterGame game) {
        super(game);

        texture = game.getAssets().get(TEXTURE_FILE, Texture.class);
        thrustersTexture = game.getAssets().get(THRUSTERS_TEXTURE_FILE, Texture.class);
        shootySound = game.getAssets().get(SHOOT_SOUND_FILE, Sound.class);
        deathSound = game.getAssets().get(DEATH_SOUND_FILE, Sound.class);
        thrusterSound = game.getAssets().get(THRUSTER_SOUND_FILE, Sound.class);

        float aspect = (float) texture.getWidth() / texture.getHeight();
        getRect().setSize(1 / aspect, 1 / aspect);
    }

    static void loadAssets(AssetManager assets) {
        assets.load(TEXTURE_FILE, Texture.class);
        assets.load(THRUSTERS_TEXTURE_FILE, Texture.class);
        assets.load(SHOOT_SOUND_FILE, Sound.class);
        assets.load(DEATH_SOUND_FILE, Sound.class);
        assets.load(THRUSTER_SOUND_FILE, Sound.class);
    }

    @Override
    public void draw(SpriteBatch batch) {
        Texture current = texture;
        if (movingForwards) {
            current = thrustersTexture;
        }
        if (dead) {
            deadTimer += Gdx.graphics.getDeltaTime();
        }
        float aspect = (float) texture.getWidth() / texture.getHeight();
        float w = getRect().getWidth();
        float realWidth = 1.0f;
        float deadRad = deadTimer * 5;
        batch.draw(current,
                getRect().x + (w - realWidth) / 2,
                getRect().y,
                realWidth / 2,
                getRect().getHeight() / 2,
                1f,
                1f / aspect,
                1f,
                1f,
                (float) Math.toDegrees(TEXTURE_ROTATION + rotation + deadRad),
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

        if (hasTargetDir) {
            rotation = -targetDir.angleRad(Vector2.Y) + (float) Math.PI;
        }

        if (isMovingForwards()) {
            moveVec.y += 1;
        }
        if (isMovingBackwards()) {
            moveVec.y -= 1;
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
        float w = getRect().getWidth();
        float h = getRect().getHeight();
        newX = MathUtils.clamp(newX, 0f + w / 2, getGame().getWorldWidth() - w / 2);
        newY = MathUtils.clamp(newY, 0f + h / 2, getGame().getWorldHeight() - h / 2);

        setPos(newX, newY);

        shootElapsed += deltaTime;
        if (isShooting()) {
            shoot();
        }
    }

    private void shoot() {
        if (shootElapsed < SHOOT_DELAY) {
            return;
        }

        shootySound.play();

        shootElapsed = 0;
        Vector2 dir = Vector2.Y.cpy().rotateRad(rotation);
        Bullet b = new Bullet(getGame(), dir);
        Vector2 spawnPos = getRect().getCenter(new Vector2()).add(dir.scl(0.6f));
        b.getRect().setCenter(spawnPos);
        getGame().addBullet(b);
    }

    public void onCollision(EnemyGuy e) {
        setDead(true);
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        if (dead && !this.dead) {
            thrusterSound.stop();
            deathSound.play();
        }
        this.dead = dead;
    }

    public boolean isMovingForwards() {
        return movingForwards;
    }

    public void setMovingForwards(boolean movingForwards) {
        this.movingForwards = movingForwards;

        if (movingForwards && !dead) {
            thrusterSound.loop(THRUSTER_SOUND_VOLUME);
        } else {
            thrusterSound.stop();
        }
    }

    public boolean isMovingBackwards() {
        return movingBackwards;
    }

    public void setMovingBackwards(boolean movingBackwards) {
        this.movingBackwards = movingBackwards;
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

    public void setHasTargetDir(boolean hasTargetDir) {
        this.hasTargetDir = hasTargetDir;
    }

    public void setTargetDir(Vector2 targetDir) {
        this.targetDir.set(targetDir);
    }
}
