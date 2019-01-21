package me.aleksi.shooter;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class ShooterGame implements ApplicationListener {
    private static final float WORLD_WIDTH = 16;
    private static final float WORLD_HEIGHT = 9;

    private AssetManager assets;

    private Viewport gameViewport;

    private SpriteBatch batch;

    private ShootyGuyInput input;
    private ShootyGuy playerGuy;
    private ArrayList<EnemyGuy> enemyGuys = new ArrayList<EnemyGuy>(1);

    private float getWorldWidth() {
        return gameViewport.getWorldWidth();
    }

    private float getWorldHeight() {
        return gameViewport.getWorldHeight();
    }

    @Override
    public void create() {
        assets = new AssetManager();

        ShootyGuy.loadAssets(assets);
        EnemyGuy.loadAssets(assets);

        assets.finishLoading();

        gameViewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, new OrthographicCamera());

        // Initialize gameViewport, so that we can use its new world width and height
        // (because ExtendViewport can change the width or height).
        gameViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        batch = new SpriteBatch();

        playerGuy = new ShootyGuy(assets);
        playerGuy.setX(1.5f);

        input = new ShootyGuyInput(playerGuy);
        Gdx.input.setInputProcessor(input);

        enemyGuys.add(new EnemyGuy(assets));
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        playerGuy.update(delta);
        for (EnemyGuy e : enemyGuys) {
            e.update(delta);
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameViewport.apply();

        batch.setProjectionMatrix(gameViewport.getCamera().combined);

        batch.begin();
        playerGuy.draw(batch);
        for (EnemyGuy e : enemyGuys) {
            e.draw(batch);
        }
        batch.end();
    }

    @Override
    public void pause() {
        // on desktop, pause is called when:
        // the window goes out of focus, and when the window is closed
        Gdx.app.log("GSG", "pause");
    }

    @Override
    public void resume() {
        // on desktop, resume is called when the window comes back in focus
        Gdx.app.log("GSG", "resume");
    }

    @Override
    public void dispose() {
        batch.dispose();
        assets.dispose();
    }
}
