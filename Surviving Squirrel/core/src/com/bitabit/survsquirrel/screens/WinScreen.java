package com.bitabit.survsquirrel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bitabit.survsquirrel.InputManager;
import com.bitabit.survsquirrel.Principal;

public class WinScreen implements Screen {
	
	private final Principal pr;
	
	private Texture bg = new Texture("imagenes/win.png");
	
	private ScreenViewport screenViewport;
	
	private Sprite bgSprite;
	
	public InputManager inputM;

	public WinScreen(final Principal pr) {
		
		this.pr = pr;
		
		screenViewport = pr.screenViewport;
		
		bgSprite = new Sprite(bg);
		
		inputM = new InputManager();
		Gdx.input.setInputProcessor(inputM);
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		
		if (inputM.isKeyReleased(Input.Keys.ESCAPE)) {
			pr.setScreen(new MainMenuScreen(pr));
			this.dispose();
		}
		
		screenViewport.apply();
		
		pr.batch.setProjectionMatrix(screenViewport.getCamera().combined);
		
		pr.batch.begin();
		
		bgSprite.draw(pr.batch);
		
		pr.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		screenViewport.update(width, height, true);
		
		bgSprite.setSize(width, height);
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
