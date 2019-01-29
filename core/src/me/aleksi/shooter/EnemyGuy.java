package me.aleksi.shooter;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

final class EnemyGuy extends Entity {
    private static final String TEXTURE_FILE = "enemy.png";
    private static final String DEATH_SOUND_FILE = "enemy-deathsound.ogg";
    private static final float MIN_SPEED = 2f;
    private static final float MAX_SPEED = 4f;

    private Texture texture;
    private Sound deathSound;

    private float ghostTimer = .75f;
    private boolean dead;
    private Vector2 speed;

    EnemyGuy(ShooterGame game) {
        super(game);
        texture = game.getAssets().get(TEXTURE_FILE, Texture.class);
        getRect().setSize(1.0f, (float) texture.getHeight() / texture.getWidth());

        deathSound = game.getAssets().get(DEATH_SOUND_FILE, Sound.class);

        // Set  direction by random
        speed = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
        speed.nor();
        speed.scl(MathUtils.random(MIN_SPEED, MAX_SPEED));
    }

    static void loadAssets(AssetManager assets) {
        assets.load(TEXTURE_FILE, Texture.class);
        assets.load(DEATH_SOUND_FILE, Sound.class);
    }

    public boolean isGhost() {
        return ghostTimer > 0;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        if (dead && !this.dead) {
            deathSound.play();
        }

        this.dead = dead;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (ghostTimer > 0) {
            batch.setColor(1f - ghostTimer, 1f - ghostTimer, 1, 1f - ghostTimer);
        }
        batch.draw(texture, getRect().x, getRect().y, getRect().getWidth(), getRect().getHeight());
        if (ghostTimer > 0) {
            batch.setColor(1, 1, 1, 1f);
        }
    }

    @Override
    public void update(float deltaTime) {
        if (ghostTimer > 0) {
            ghostTimer -= deltaTime;
            return;
        }

        float x = getRect().x;
        float y = getRect().y;
        float w = getRect().getWidth();
        float h = getRect().getHeight();

        // Check if we've hit or gone past a wall, reverse if we have

        if (x <= 0 && speed.x < 0) {
            speed.x *= -1;
        }
        if ((x + w) >= getGame().getWorldWidth() && speed.x > 0) {
            speed.x *= -1;
        }
        if (y <= 0 && speed.y < 0) {
            speed.y *= -1;
        }
        if ((y + h) >= getGame().getWorldHeight() && speed.y > 0) {
            speed.y *= -1;
        }

        float newX = x + deltaTime * speed.x;
        float newY = y + deltaTime * speed.y;

        newX = MathUtils.clamp(newX, 0, getGame().getWorldWidth() - w);
        newY = MathUtils.clamp(newY, 0, getGame().getWorldHeight() - h);

        getRect().setPosition(newX, newY);
    }
}
