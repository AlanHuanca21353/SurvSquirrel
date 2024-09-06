package com.bitabit.survsquirrel;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.bitabit.survsquirrel.Principal;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Surviving Squirrel");
		config.setWindowedMode(1280, 720);
		config.setWindowIcon("imagenes/icon.png");
		config.setResizable(false);
		new Lwjgl3Application(new Principal(), config);
	}
}
