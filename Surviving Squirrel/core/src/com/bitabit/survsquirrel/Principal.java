package com.bitabit.survsquirrel;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bitabit.survsquirrel.screens.MainMenuScreen;

public class Principal extends Game {
	public SpriteBatch batch;
	public FitViewport fitViewport;
	public ScreenViewport screenViewport;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		fitViewport = new FitViewport(1280,720);
		screenViewport = new ScreenViewport();
		
		System.out.println("Abrir Aplicación");
		
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		
		ScreenUtils.clear(0, 0, 0, 1);
		
//		if (inputManager.isKeyPressed(Input.Keys.A)) {
//			System.out.println("A Pressed");
//		}
//		if (inputManager.isKeyDown(Input.Keys.A)) {
//			System.out.println("A Down");
//		}
//		if (inputManager.isKeyReleased(Input.Keys.A)) {
//			System.out.println("A Released");
//		}
		
		
		super.render();
		
//		System.out.println("Siempre");
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		
		super.dispose();
		
		System.out.println("Cerrar Aplicación");
	}
	

	
}
