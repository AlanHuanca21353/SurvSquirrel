package com.bitabit.survsquirrel.entity.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitabit.survsquirrel.InputManager;
import com.bitabit.survsquirrel.entity.EntityType;
import com.bitabit.survsquirrel.enums.AnimationState;
import com.bitabit.survsquirrel.enums.Direcciones;
import com.bitabit.survsquirrel.screens.GameScreen;
import com.bitabit.survsquirrel.tools.RandomGenerator;

public class EnemyRat extends Enemy {
	
	private static final int SPEED = 90;
	private static final int JUMP_VELOCITY = 4;
	
	RandomGenerator rg;
	
	InputManager inputManager;
	
	Texture idleImage, hurtImage;
	Sprite sprite;
	Sound jumpSound, walkSound;
	
	private boolean movingRight, movingLeft, jumping, dontWalk = false, dontJump = false;
	
	public EnemyRat(float x, float y, GameScreen gameScreen) {
		super(x, y, EntityType.ENEMYRAT, gameScreen);
		
		rg = gameScreen.rg;
		
		idleImage = new Texture("imagenes/rat.png");
		hurtImage = new Texture("imagenes/rat_ouch.png");
		
		inputManager = gameScreen.inputManager;
		
		dirX = Direcciones.RIGHT;
		animState = AnimationState.IDLE;
		
		hp = 60;
		
		ouchTimer = 0f;

		jumpSound = audioM.createNewSound("Sound/jump.wav");
		walkSound = audioM.createNewSound("Sound/walkSound.wav");
		
		movingLeft = false;
		movingRight = false;
		jumping = false;
	}

	@Override
	public void update(float deltaTime, float gravity) {
		
		if (!dontJump) {
			if (inputManager.isKeyDown(Input.Keys.UP) && !dontJump) {
				jumping = true;
			}
			
			if (inputManager.isKeyReleased(Input.Keys.UP) && !dontJump) {
				jumping = false;
			}	
		}
		
		if (grounded) { // Si estás en el suelo
			
			this.animState = AnimationState.IDLE;
			
			if (jumping) { // Salto inicial
				this.velocityY += JUMP_VELOCITY * getWeight();
				
				audioM.playSound(jumpSound, 0.5f, 0.65f, 0.85f);
			}
		}
		else { // Si estás en el aire

			// Si mantenes pulsado despues de saltar, saltas mas alto.
			if (jumping && this.velocityY > 0) {
				this.velocityY += JUMP_VELOCITY * getWeight() * deltaTime;
			}
		}
		
		if (!dontWalk) { // Si se permite caminar
			
			if (inputManager.isKeyDown(Input.Keys.LEFT)) {
				movingLeft = true;
			} 
			if (inputManager.isKeyReleased(Input.Keys.LEFT)) {
				movingLeft = false;
			}
			if (inputManager.isKeyDown(Input.Keys.RIGHT)) {
				movingRight = true;
			}
			if (inputManager.isKeyReleased(Input.Keys.RIGHT)) {
				movingRight = false;
			}
			
		}
		
		// Mover
		if (movingLeft && !hit) { // Mover Izquierda
			moveX(-SPEED * deltaTime);	
			
			this.dirX = Direcciones.LEFT;
			
			
		}
		
		if (movingRight && !hit) { // Mover Derecha
			moveX(SPEED * deltaTime);
			
			this.dirX = Direcciones.RIGHT;
			
			
		}
		
		if (hit) {
			
			ouchTimer += deltaTime;
			dontWalk = true;
			dontJump = true;
			
			this.animState = AnimationState.HURT;
			
			if (ouchTimer >= 0.3f) {
				
				ouchTimer = 0f;
				hit = false;
				dontWalk = false;
				dontJump = false;
				
				if (hp <= 0f) {
					die();
				}
			}
		}
		
		super.update(deltaTime, gravity); // Aplicar gravedad.
		
		
	}

	@Override
	public void render(SpriteBatch batch) {
//		boolean flip = (getDirX() == Direcciones.LEFT);
		
		switch (animState) {
		case IDLE:
			animM.drawStaticSprite(batch, idleImage, this);
			break;
			
		case HURT:
			animM.drawStaticSprite(batch, hurtImage, this);
			break;
		}
		
	}

	@Override
	public void sleep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wakeUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		System.out.println("RIP");
		
		audioM.playSound(deathSound, 0.25f);
		
		remove = true;
	}

}


