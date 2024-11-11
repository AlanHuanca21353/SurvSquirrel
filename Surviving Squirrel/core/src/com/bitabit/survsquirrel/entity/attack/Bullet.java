package com.bitabit.survsquirrel.entity.attack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitabit.survsquirrel.entity.Entity;
import com.bitabit.survsquirrel.entity.EntityType;
import com.bitabit.survsquirrel.entity.Player;
import com.bitabit.survsquirrel.enums.Direcciones;
import com.bitabit.survsquirrel.screens.GameScreen;

public class Bullet extends Attack {

	private static final int BASE_SPEED = 400;
	private static Texture bImg;

	private Direcciones dirX;
	
	public boolean hurtPlayer = false;
	
	Animation<TextureRegion> bulletAnim;

	public Bullet(float x, float y, GameScreen gameScreen, float power, Direcciones dirX) {
		super(x, y, EntityType.BULLET, gameScreen, power);
		
		this.dirX = dirX;
		
		bImg = new Texture("imagenes/bullet.png");
		
		bulletAnim = animM.genEntAnimation(12, bImg, this, 4, 2, 2);
		


	}
	
	public Bullet(float x, float y, GameScreen gameScreen, float power, Direcciones dirX, boolean hurtPlayer) {
		this(x,y,gameScreen,power,dirX);

		this.hurtPlayer = hurtPlayer;
		
	}
	
	public Bullet(Entity e, GameScreen gS, float power) {
		this(e.getAtkStartX(), e.getY()+15, gS, power, e.getDirX());
		
		if (e.getDirX() == Direcciones.LEFT) {
			pos.x -= 10;
		}
		else {
			pos.x += 25;
		}
		
	}
	
	public Bullet(Entity e, GameScreen gS, float power, boolean hurtPlayer) {
		this(e.getAtkStartX(), e.getY()+20, gS, power, e.getDirX(), hurtPlayer);
		
		if (e.getDirX() == Direcciones.LEFT) {
			pos.x -= 10;
		}
		else {
			pos.x += 25;
		}
		
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
		
		if (velX == 0) {
			remove = true;
		}
	}

	@Override
	public void render(SpriteBatch batch) {
//		animM.drawStaticSprite(batch, bImg, this);
		
		animM.drawAnimSprite(batch, bulletAnim, this, true);

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
