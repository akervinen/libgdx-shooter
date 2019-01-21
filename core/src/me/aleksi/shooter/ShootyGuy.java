package me.aleksi.shooter;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

final class ShootyGuy extends Entity {
    private static final String TEXTURE_FILE = "thonk.png";

    private Texture texture;

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

    }
}
