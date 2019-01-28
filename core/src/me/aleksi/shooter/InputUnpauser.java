package me.aleksi.shooter;

import com.badlogic.gdx.InputAdapter;

final class InputUnpauser extends InputAdapter {
    private ShooterGame game;

    InputUnpauser(ShooterGame game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        game.unpause();
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        game.unpause();
        return false;
    }
}
