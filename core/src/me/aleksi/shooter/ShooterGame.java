package me.aleksi.shooter;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class ShooterGame implements ApplicationListener {
    // Some constants
    private static final float WORLD_WIDTH = 16;
    private static final float WORLD_HEIGHT = 9;
    private static final int ENEMY_COUNT = 3;

    // Graphics related
    private AssetManager assets;

    private Viewport gameViewport;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    // Gameplay related
    private State state = State.Ongoing;
    private ShootyGuyInput input;
    private ShootyGuy playerGuy;
    private ArrayList<EnemyGuy> enemyGuys = new ArrayList<EnemyGuy>(1);
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private ArrayList<Entity> oldEntities = new ArrayList<Entity>();

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
        // Load all assets
        assets = new AssetManager();

        ShootyGuy.loadAssets(assets);
        Bullet.loadAssets(assets);
        EnemyGuy.loadAssets(assets);

        assets.finishLoading();

        gameViewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera());

        // Initialize gameViewport, so that we can use its new world width and height
        // (because ExtendViewport can change the width or height).
        gameViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Create our player ship/character and input handler

        playerGuy = new ShootyGuy(this);
        playerGuy.setPos(1.5f, 1.5f);

        input = new ShootyGuyInput(playerGuy);
        Gdx.input.setInputProcessor(input);

        // Add enemies
        for (int i = 0; i < ENEMY_COUNT; i++) {
            addEnemyGuy();
        }
    }

    private void addEnemyGuy() {
        EnemyGuy e = new EnemyGuy(this);

        // Pick random positions until we get one at least 4 units away from the player
        float x, y;
        do {
            x = MathUtils.random(getWorldWidth() - 1.0f);
            y = MathUtils.random(getWorldHeight() - 1.0f);
        } while (Vector2.dst2(x, y, playerGuy.getX(), playerGuy.getY()) < 16f);

        e.setPos(x, y);
        enemyGuys.add(e);
    }

    void addBullet(Bullet b) {
        bullets.add(b);
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

        // Draw everything
        batch.begin();
        for (Bullet b : bullets) {
            b.draw(batch);
        }
        playerGuy.draw(batch);
        for (EnemyGuy e : enemyGuys) {
            e.draw(batch);
        }
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Bullet b : bullets) {
            Rectangle r = b.getRect();
            shapeRenderer.rect(r.x, r.y, r.width, r.height);
        }
        {
            Rectangle r = playerGuy.getRect();
            shapeRenderer.rect(r.x, r.y, r.width, r.getHeight());
        }
        for (EnemyGuy e : enemyGuys) {
            Rectangle r = e.getRect();
            shapeRenderer.rect(r.x, r.y, r.width, r.height);
        }
        shapeRenderer.end();
    }

    private void update() {
        if (state != State.Ongoing) {
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
        for (Entity e : oldEntities) {
            bullets.remove(e);
        }
        oldEntities.clear();
        for (EnemyGuy e : enemyGuys) {
            if (e.isDead()) {
                oldEntities.add(e);
                continue;
            }
            e.update(delta);
        }

        for (Entity e : oldEntities) {
            enemyGuys.remove(e);
        }

        for (int i = enemyGuys.size(); i < ENEMY_COUNT; i++) {
            addEnemyGuy();
        }

        checkCollisions();
    }

    private void checkCollisions() {
        for (EnemyGuy e : enemyGuys) {
            if (playerGuy.collides(e)) {
                playerGuy.onCollision(e);
            }
            for (Bullet b : bullets) {
                if (e.collides(b)) {
                    e.setDead(true);
                    b.setDead(true);
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    @Override
    public void pause() {
        // on desktop, pause is called when:
        // the window goes out of focus, and when the window is closed
        Gdx.app.log("GSG", "pause");

        state = State.Paused;
    }

    @Override
    public void resume() {
        // on desktop, resume is called when the window comes back in focus
        Gdx.app.log("GSG", "resume");

        state = State.Ongoing;
    }

    @Override
    public void dispose() {
        batch.dispose();
        assets.dispose();
    }

    enum State {
        Ongoing,
        Paused,
        Ended
    }
}
