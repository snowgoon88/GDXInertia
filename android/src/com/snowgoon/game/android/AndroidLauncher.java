package com.snowgoon.game.android;

import android.os.Bundle;

/**
 * Pour lancer sur ma tablette.
 * /usr/local/share/android_sdk/platform-tools/adb kill-server
 * /usr/local/share/android_sdk/platform-tools/adb start-server
 * plug in tablet and authorise access
 */
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.snowgoon.game.GDXInertia;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		// no compass or accelerometer ?? Why ??
		config.useAccelerometer = false;
		config.useCompass = false;
		initialize(new GDXInertia(), config);
		// initialize(new InputDemo2(), config);
		//initialize(new InputDemo3(), config);
		//initialize(new InputDemo4(), config);
		// initialize(new CameraDemo(), config);
	}
}
