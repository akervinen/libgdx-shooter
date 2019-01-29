package me.aleksi.shooter;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

final class InputUnpauser extends InputAdapter {
    private ShooterGame game;

    InputUnpauser(ShooterGame game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
        case Input.Keys.UP:
        case Input.Keys.DOWN:
        case Input.Keys.LEFT:
        case Input.Keys.RIGHT:
        case Input.Keys.SPACE:
            game.unpause();
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        game.unpause();
        return false;
    }
}
