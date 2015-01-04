package com.game.snowgoon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.snowgoon.GDXInertia;

/**
 * Tutorial from
 * https://github.com/libgdx/libgdx/wiki/A-simple-game
 * 
 * @author snowgoon88@gmail.com
 *
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Goutte";
		config.width = 800;
		config.height = 400;
		new LwjglApplication(new GDXInertia(), config);
	}
}
