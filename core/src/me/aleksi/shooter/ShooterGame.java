package me.aleksi.shooter;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class ShooterGame implements ApplicationListener {
    // Some constants
    private static final String MUSIC_FILE = "music.mp3";
    private static final String BACKGROUND_FILE = "space-bg.png";
    private static final float WORLD_WIDTH = 16;
    private static final float WORLD_HEIGHT = 9;
    private static final int START_ENEMY_COUNT = 3;

    private boolean hasAccelSensor = false;
    private Vector3 accelVec = new Vector3();

    // Graphics related
    private AssetManager assets;

    private Viewport gameViewport;
    private Viewport uiViewport;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private BitmapFont font;
    private Music bgMusic;
    private Texture background;

    private CharSequence leftHelp = "Tap and drag on the\nleft half to move";
    private CharSequence rightHelp = "Tap or hold on the\nright half to shoot";

    // Gameplay related
    private State state = State.Paused;
    private float endWaitTimer;

    private ShootyGuy playerGuy;
    private ArrayList<EnemyGuy> enemyGuys = new ArrayList<EnemyGuy>(1);
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private ArrayList<Entity> oldEntities = new ArrayList<Entity>();

    private int score;

    AssetManager getAssets() {
        return assets;
    }

    public Viewport getGameViewport() {
        return gameViewport;
    }

    public float getWorldWidth() {
        return gameViewport.getWorldWidth();
    }

    public float getWorldHeight() {
        return gameViewport.getWorldHeight();
    }

    @Override
    public void create() {
        // Different help text on desktop
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            leftHelp = "Arrows to move";
            rightHelp = "Space to shoot";
        }

        hasAccelSensor = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);

        // Load all assets
        assets = new AssetManager();
        assets.load("8-bit-operator.fnt", BitmapFont.class);
        assets.load(MUSIC_FILE, Music.class);
        assets.load(BACKGROUND_FILE, Texture.class);
        ShootyGuy.loadAssets(assets);
        Bullet.loadAssets(assets);
        EnemyGuy.loadAssets(assets);
        assets.finishLoading();

        font = assets.get("8-bit-operator.fnt", BitmapFont.class);

        bgMusic = assets.get(MUSIC_FILE, Music.class);
        bgMusic.setLooping(true);
        bgMusic.play();

        background = assets.get(BACKGROUND_FILE, Texture.class);
        gameViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera());
        uiViewport = new FitViewport(320, 180, new OrthographicCamera());

        // Initialize viewports, so that we can use their new world width and height
        // (because ExtendViewport can change the width or height).
        uiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        gameViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Reset state
        reset();
    }

    private void reset() {
        state = ShooterGame.State.Paused;
        endWaitTimer = 0;
        score = 0;
        enemyGuys.clear();
        bullets.clear();
        oldEntities.clear();

        // Create our player ship/character and input handler
        playerGuy = new ShootyGuy(this);
        playerGuy.setPos(1.5f, 1.5f);

        // Add unpauser input processor as primary, which then unpauses the
        // game if needed and passes all input through to the next processor
        InputMultiplexer input = new InputMultiplexer();
        input.addProcessor(new InputUnpauser(this));
        input.addProcessor(new ShootyGuyInput(gameViewport, playerGuy));
        Gdx.input.setInputProcessor(input);

        // Add enemies
        for (int i = 0; i < START_ENEMY_COUNT; i++) {
            addEnemyGuy();
        }
    }

    private void addEnemyGuy() {
        EnemyGuy e = new EnemyGuy(this);

        float safetyZoneSqr = 16f;

        if (score > 60) {
            safetyZoneSqr = 4f;
        } else if (score > 30) {
            safetyZoneSqr = 9f;
        }

        // Pick random positions until we get one at least 4 units away from the player
        float x, y;
        do {
            x = MathUtils.random(1f, getWorldWidth() - 1f);
            y = MathUtils.random(1f, getWorldHeight() - 1f);
        } while (Vector2.dst2(x, y, playerGuy.getX(), playerGuy.getY()) < safetyZoneSqr);

        e.setPos(x, y);
        enemyGuys.add(e);
    }

    void addBullet(Bullet b) {
        bullets.add(b);
    }

    private void onScore() {
        score += 1;
    }

    void unpause() {
        if (state == ShooterGame.State.Paused) {
            state = ShooterGame.State.Ongoing;
        } else if (state == ShooterGame.State.Ended && endWaitTimer > 1) {
            // We wait a second to avoid accidental input from restarting the game
            reset();
        }
    }

    @Override
    public void render() {
        // Do all entity logic first
        update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Camera and projection setup
        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(gameViewport.getCamera().combined);

        float bgAspect = (float) background.getWidth() / background.getHeight();

        // Draw everything
        batch.begin();
        batch.draw(background, 0, 0, getWorldWidth(), getWorldWidth() / bgAspect);
        for (Bullet b : bullets) {
            b.draw(batch);
        }
        playerGuy.draw(batch);
        for (EnemyGuy e : enemyGuys) {
            e.draw(batch);
        }
        batch.end();

        // Grab accel values for moving the HUD
        if (hasAccelSensor) {
            accelVec.set(-Gdx.input.getAccelerometerY(),
                    -Gdx.input.getAccelerometerX(),
                    0f);
        }

        uiViewport.apply(true);
        if (hasAccelSensor) {
            // Shake HUD when phone moves
            uiViewport.getCamera().translate(accelVec);
            uiViewport.getCamera().update();
        }
        batch.setProjectionMatrix(uiViewport.getCamera().combined);

        batch.begin();
        font.draw(batch, "Score: " + score, 5, uiViewport.getWorldHeight() - 5);
        // Draw help if paused or game ended
        if (state != State.Ongoing) {
            font.draw(batch, leftHelp, 5, uiViewport.getWorldHeight() / 2);
            font.draw(batch, rightHelp, uiViewport.getWorldWidth() / 2 + 5, uiViewport.getWorldHeight() / 2);
        }
        batch.end();
    }

    private void update() {
        if (state != State.Ongoing) {
            if (state == State.Ended) {
                // Advance end timer
                endWaitTimer += Gdx.graphics.getDeltaTime();
            }
            return;
        }

        float delta = Gdx.graphics.getDeltaTime();

        playerGuy.update(delta);

        for (Bullet b : bullets) {
            if (b.isDead()) {
                oldEntities.add(b);
                continue;
            }
            b.update(delta);
        }
        bullets.removeAll(oldEntities);
        oldEntities.clear();

        for (EnemyGuy e : enemyGuys) {
            if (e.isDead()) {
                oldEntities.add(e);
                continue;
            }
            e.update(delta);
        }
        enemyGuys.removeAll(oldEntities);
        oldEntities.clear();

        int enemyCount = START_ENEMY_COUNT + (score / 15);
        for (int i = enemyGuys.size(); i < enemyCount; i++) {
            addEnemyGuy();
        }

        checkCollisions();
    }

    private void checkCollisions() {
        for (EnemyGuy e : enemyGuys) {
            if (!e.isDead() && !e.isGhost() && playerGuy.collides(e)) {
                playerGuy.onCollision(e);
                state = State.Ended;
            }
            for (Bullet b : bullets) {
                if (!b.isDead() && !e.isDead() && !e.isGhost() && e.collides(b)) {
                    e.setDead(true);
                    b.setDead(true);
                    onScore();
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // on desktop, pause is called when:
        // the window goes out of focus, and when the window is closed
        if (state == State.Ongoing) {
            state = State.Paused;
        }
    }

    @Override
    public void resume() {
        // on desktop, resume is called when the window comes back in focus
        if (state == State.Paused) {
            state = State.Ongoing;
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        assets.dispose();
    }

    enum State {
        Ongoing,
        Paused,
        Ended
    }
}
