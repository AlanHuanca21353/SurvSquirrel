package com.bitabit.survsquirrel.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.bitabit.survsquirrel.Principal;

public class MainMenuScreen implements Screen{
	
	private static final int EXIT_BUTTON_WIDTH=150, EXIT_BUTTON_HEIGHT=75, PLAY_BUTTON_WIDTH=200, PLAY_BUTTON_HEIGHT=100;
	private static final int TITLE_WIDTH=709, TITLE_HEIGHT=475;
	private static final int EXIT_BUTTON_Y = 50, PLAY_BUTTON_Y = 125, TITLE_Y = 230;
	
	final Principal game;
	
	Texture gameTitle;
	
	Texture playButtonActive;
	Texture playButtonInactive;
	
	Texture exitButtonActive;
	Texture exitButtonInactive;
	
	
	public MainMenuScreen(final Principal game) {
		
		this.game = game;
		
		gameTitle = new Texture("imagenes/title.png");
		
		playButtonActive = new Texture("imagenes/play_button_active.png");
		playButtonInactive = new Texture("imagenes/play_button_inactive.png");
		exitButtonActive = new Texture("imagenes/exit_button_active.png");
		exitButtonInactive = new Texture("imagenes/exit_button_inactive.png");
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		System.out.println("Iniciar Menu Principal");
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.4f, 0.4f, 0.4f, 1);
		
		game.batch.begin();
		
		int exitX = Gdx.graphics.getWidth()/2 - EXIT_BUTTON_WIDTH/2;
		int exitY = EXIT_BUTTON_Y;
		int playX = Gdx.graphics.getWidth()/2 - PLAY_BUTTON_WIDTH/2;
		int playY = PLAY_BUTTON_Y;
		
		game.batch.draw(gameTitle, Gdx.graphics.getWidth()/2 - TITLE_WIDTH/2, TITLE_Y, TITLE_WIDTH, TITLE_HEIGHT);
		
		if (hoverButton(playButtonActive, playButtonInactive, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT, playX, playY)) {
			this.dispose();
			game.setScreen(new GameScreen(game));
		}
		if (hoverButton(exitButtonActive, exitButtonInactive, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT, exitX, exitY)) {
			Gdx.app.exit();
		}
		
		
		game.batch.end();
		
	}
	
	public boolean hoverButton(Texture buttonActive, Texture buttonInactive, int buttonWidth, int buttonHeight, int buttonX, int buttonY) {
		if (Gdx.input.getX() < buttonX + buttonWidth && Gdx.input.getX() > buttonX && Gdx.graphics.getHeight() - Gdx.input.getY() < buttonY + buttonHeight && Gdx.graphics.getHeight() - Gdx.input.getY() > buttonY) {
			game.batch.draw(buttonActive, Gdx.graphics.getWidth()/2 - buttonWidth/2, buttonY, buttonWidth, buttonHeight);
			if (Gdx.input.isTouched()) {
				return true;
			}
		}
		else {
			game.batch.draw(buttonInactive, Gdx.graphics.getWidth()/2 - buttonWidth/2, buttonY, buttonWidth, buttonHeight);
		}
		return false;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
	public void hide() {}

	@Override
	public void dispose() {}

}
