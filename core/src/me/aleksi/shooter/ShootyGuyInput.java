package me.aleksi.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

final class ShootyGuyInput implements InputProcessor {
    private Viewport viewport;
    private ShootyGuy shootyGuy;

    private int movingPointer;
    private int shootingPointer;
    private Vector2 initialTouch = new Vector2();

    ShootyGuyInput(Viewport viewport, ShootyGuy guy) {
        this.viewport = viewport;
        this.shootyGuy = guy;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
        case Input.Keys.UP:
            shootyGuy.setMovingForwards(true);
            return true;
        case Input.Keys.DOWN:
            shootyGuy.setMovingBackwards(true);
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
            shootyGuy.setMovingForwards(false);
            return true;
        case Input.Keys.DOWN:
            shootyGuy.setMovingBackwards(false);
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
        if (screenX < Gdx.graphics.getWidth() / 2) {
            // Left side for moving
            movingPointer = pointer;
            initialTouch.set(screenX, screenY);
            viewport.unproject(initialTouch);
            shootyGuy.setHasTargetDir(true);
            shootyGuy.setMovingForwards(true);
        } else {
            // Right side for shooting
            shootingPointer = pointer;
            shootyGuy.setShooting(true);
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == movingPointer) {
            initialTouch.setZero();
            shootyGuy.setHasTargetDir(false);
            shootyGuy.setMovingForwards(false);
        }
        if (pointer == shootingPointer) {
            shootyGuy.setShooting(false);
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer == movingPointer) {
            Vector2 newTouch = new Vector2(screenX, screenY);
            viewport.unproject(newTouch);
            Vector2 newFacing = initialTouch.cpy().sub(newTouch);
            shootyGuy.setTargetDir(newFacing);
        }
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

