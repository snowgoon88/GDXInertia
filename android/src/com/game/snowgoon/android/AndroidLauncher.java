package com.game.snowgoon.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.game.snowgoon.GDXInertia;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		// no compass or accelerometer ?? Why ??
		config.useAccelerometer = false;
		config.useCompass = false;
		initialize(new GDXInertia(), config);
	}
}
