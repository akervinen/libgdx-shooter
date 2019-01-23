package me.aleksi.shooter;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

final class ShootyGuyInput implements InputProcessor {
    private ShootyGuy shootyGuy;

    ShootyGuyInput(ShootyGuy guy) {
        shootyGuy = guy;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
        case Input.Keys.UP:
            shootyGuy.setMovingUp(true);
            return true;
        case Input.Keys.DOWN:
            shootyGuy.setMovingDown(true);
            return true;
        case Input.Keys.LEFT:
            //shootyGuy.setMovingLeft(true);
            shootyGuy.setTurningLeft(true);
            return true;
        case Input.Keys.RIGHT:
            //shootyGuy.setMovingRight(true);
            shootyGuy.setTurningRight(true);
            return true;
        case Input.Keys.SPACE:
            shootyGuy.setShooting(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
        case Input.Keys.UP:
            shootyGuy.setMovingUp(false);
            return true;
        case Input.Keys.DOWN:
            shootyGuy.setMovingDown(false);
            return true;
        case Input.Keys.LEFT:
            //shootyGuy.setMovingLeft(false);
            shootyGuy.setTurningLeft(false);
            return true;
        case Input.Keys.RIGHT:
            //shootyGuy.setMovingRight(false);
            shootyGuy.setTurningRight(false);
            return true;
        case Input.Keys.SPACE:
            shootyGuy.setShooting(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

