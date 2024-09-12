package com.bitabit.survsquirrel.entity.attack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitabit.survsquirrel.entity.EntityType;
import com.bitabit.survsquirrel.enums.Direcciones;
import com.bitabit.survsquirrel.screens.GameScreen;

public class Bullet extends Attack {

	private static final int BASE_SPEED = 400;
	private static Texture bImg;

	private Direcciones dirX;
	
	public boolean hurtPlayer = false;

	public Bullet(float x, float y, GameScreen gameScreen, float power, Direcciones dirX) {
		super(x, y, EntityType.BULLET, gameScreen, power);
		
		this.dirX = dirX;
		
		bImg = new Texture("imagenes/bullet.png");


	}
	
	public Bullet(float x, float y, GameScreen gameScreen, float power, Direcciones dirX, boolean hurtPlayer) {
		this(x,y,gameScreen,power,dirX);

		this.hurtPlayer = hurtPlayer;
		
	}

	public void update(float deltaTime, float gravity) {
		this.velocityY += getWeight() * deltaTime;


		switch (getDirX()) {
		case RIGHT:
			moveX(getSpeed() * deltaTime * (getPower()/2)+1);
			break;

		case LEFT:
			moveX(-getSpeed() * deltaTime * (getPower()/2)-1);
			break;
		}

		super.update(deltaTime, gravity);
	}

	@Override
	public void render(SpriteBatch batch) {
		animM.drawStaticSprite(batch, bImg, this);

	}

	public int getSpeed() {
		return BASE_SPEED;
	}

	public Direcciones getDirX() {
		return dirX;
	}

	public void setDirX(Direcciones dirX) {
		this.dirX = dirX;
	}

}
