package me.aleksi.shooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import me.aleksi.shooter.ShooterGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Thonk'em Up";
        config.width = 800;
        config.height = 450;
        new LwjglApplication(new ShooterGame(), config);
    }
}
