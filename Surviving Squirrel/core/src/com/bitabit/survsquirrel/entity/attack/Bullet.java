package com.bitabit.survsquirrel.entity.attack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitabit.survsquirrel.entity.Entity;
import com.bitabit.survsquirrel.entity.EntityType;
import com.bitabit.survsquirrel.enums.Direcciones;
import com.bitabit.survsquirrel.screens.GameScreen;

public class Bullet extends Attack {
	
	private static final int SPEED = 400;
	private static Texture texture;

	private Direcciones dirX;
	
	public Bullet(float x, float y, GameScreen gameScreen, float power) {
		super(x,y,EntityType.BULLET,gameScreen,power);
		
		if (texture == null) {
			texture = new Texture("imagenes/bullet.png");
		}
		
		
	}
	
	public void update(float deltaTime, float gravity) {
		this.velocityY += getWeight() * deltaTime;
		super.update(deltaTime, gravity);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(texture, pos.x, pos.y);
		
	}

	public int getSpeed() {
		return SPEED;
	}

	public Direcciones getDirX() {
		return dirX;
	}

	public void setDirX(Direcciones dirX) {
		this.dirX = dirX;
	}
	
}
