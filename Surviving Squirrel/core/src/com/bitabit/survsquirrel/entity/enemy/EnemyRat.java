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

public class EnemyRat extends Enemy {
	
	private static final int SPEED = 90;
	private static final int JUMP_VELOCITY = 4;
	
	InputManager inputManager;
	
	private Direcciones dirX;
	
	float ouchTimer;
	
	Texture idleImage, hurtImage;
	Sprite sprite;
	Sound jumpSound;
	Sound walkSound;
	
	private boolean movingRight, movingLeft, jumping, dontWalk = false, dontJump = false;
	
	public EnemyRat(float x, float y, GameScreen gameScreen) {
		super(x, y, EntityType.ENEMYRAT, gameScreen);
		
		idleImage = new Texture("imagenes/rat.png");
		hurtImage = new Texture("imagenes/rat_ouch.png");
		
		inputManager = gameScreen.inputManager;
		
		dirX = Direcciones.RIGHT;
		animState = AnimationState.IDLE;
		
		hp = 60;
		
		ouchTimer = 0f;

		jumpSound = Gdx.audio.newSound(Gdx.files.internal("Sound/jump.wav"));
		walkSound = Gdx.audio.newSound(Gdx.files.internal("Sound/walkSound.wav"));
		
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
				long id = jumpSound.play(1.0f);
				jumpSound.setLooping(id, false);
				jumpSound.setVolume(id, 0.5f);
				jumpSound.setPitch(id, 0.75f);
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
		boolean flip = (getDirX() == Direcciones.LEFT);
		if (this.animState == AnimationState.IDLE) {
			batch.draw(idleImage, flip ? pos.x + getSpriteWidth() : pos.x, pos.y, flip ? -getSpriteWidth() : getSpriteWidth(), getSpriteHeight());	
		}
		else if (this.animState == AnimationState.HURT) {
			batch.draw(hurtImage, flip ? pos.x + getSpriteWidth() : pos.x, pos.y, flip ? -getSpriteWidth() : getSpriteWidth(), getSpriteHeight());	
		}
	}
	
	public Direcciones getDirX() {
		return dirX;
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
		remove = true;
	}

}


