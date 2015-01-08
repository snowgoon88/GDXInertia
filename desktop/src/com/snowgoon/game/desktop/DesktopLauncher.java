package com.snowgoon.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.snowgoon.game.GDXInertia;
import com.snowgoon.ressources.CameraDemo;
import com.snowgoon.ressources.InputDemo2;
import com.snowgoon.ressources.InputDemo3;
import com.snowgoon.ressources.InputDemo4;

/**
 * Tutorial from
 * https://github.com/libgdx/libgdx/wiki/A-simple-game
 * https://github.com/libgdx/libgdx/wiki/Extending%20the%20Simple%20Game
 * 
 * @author snowgoon88@gmail.com
 *
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Goutte";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new GDXInertia(), config);
//		new LwjglApplication( new InputDemo2(), config);
//		new LwjglApplication( new InputDemo3(), config);
		//new LwjglApplication( new InputDemo4(), config);
		// new LwjglApplication( new CameraDemo(), config);
	}
}
