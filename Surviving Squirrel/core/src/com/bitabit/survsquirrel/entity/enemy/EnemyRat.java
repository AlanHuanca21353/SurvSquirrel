package com.bitabit.survsquirrel.entity.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

	Texture idleImage, hurtImage, veryHurtImage, sleepImage, jumpImage,
	fallImage, walkImage;
	Sprite sprite;
	Sound jumpSound, walkSound;

	Animation<TextureRegion> hurtAnimation, veryHurtAnimation, walkAnimation;

	private boolean movingRight, movingLeft;
	public boolean moving;
	private boolean jumping;
	private boolean dontWalk = false;
	private boolean dontJump = false;
	private float count, jumpDelayTimer;

	public EnemyRat(float x, float y, GameScreen gameScreen) {
		super(x, y, EntityType.ENEMYRAT, gameScreen);

		rg = gameScreen.rg;

		idleImage = new Texture("imagenes/rat.png");
		hurtImage = new Texture("imagenes/rat_ouch.png");
		veryHurtImage = new Texture("imagenes/rat_bigouch.png");
		sleepImage = new Texture("imagenes/rat_sleep.png");
		jumpImage = new Texture("imagenes/rat_jump.png");
		fallImage = new Texture("imagenes/rat_fall.png");
		walkImage = new Texture("imagenes/rat_walk.png");

		hurtAnimation = animM.genEntAnimation(12, hurtImage, this, 2);
		veryHurtAnimation = animM.genEntAnimation(12, veryHurtImage, this, 2);
		walkAnimation = animM.genEntAnimation(8, walkImage, this, 4, 2, 2);

		inputManager = gameScreen.inputM;

		dirX = Direcciones.RIGHT;

		hp = 60;

		bigOuchLimit = 30;

		ouchTimer = 0f;

		jumpSound = audioM.createNewSound("Sound/jump.wav");
		walkSound = audioM.createNewSound("Sound/walkSound.wav");

		moving = false;
		movingLeft = false;
		movingRight = false;
		hitLeft = false;
		hitRight = false;
		jumping = false;
	}

	@Override
	public void update(float deltaTime, float gravity) {
		
		jumpDelayTimer += deltaTime;

		if (awaken) {
			
			if (jumping) {
				newAnimState = AnimationState.JUMPING;
			}
			
			if (velocityY <= 0) {
				jumping = false;
			}

			if (grounded) { // Si estás en el suelo
				
				if (!hit && !moving) {
					newAnimState = AnimationState.IDLE;	
				}
				
			}
			else { // Si estás en el aire
				
				if (!jumping) {
					newAnimState = AnimationState.FALLING;
				}

				// Si mantenes pulsado despues de saltar, saltas mas alto.
				if (jumping && this.velocityY > 0 && !dontJump) {
					this.velocityY += JUMP_VELOCITY * getWeight() * deltaTime;
					
				}
			}	
		}
		else {
			sleep();
		}
		
		if (hit) {
			
			if (!awaken) {
				wakeUp();
			}

			ouchTimer += deltaTime;
			dontWalk = true;
			dontJump = true;

			int tempSpeed = SPEED;

			if (hitLeft) {
				tempSpeed = SPEED;
			}
			else if (hitRight) {
				tempSpeed = -SPEED;
			}

			if (bigOuch) {
				moveX(tempSpeed * 5 * deltaTime);
				this.newAnimState = AnimationState.VERYHURT;
			} else {
				this.newAnimState = AnimationState.HURT;
			}

			if (ouchTimer >= 0.3f) {

				ouchTimer = 0f;
				hit = dontWalk = dontJump = bigOuch = hitLeft = hitRight = false;

				if (hp <= 0f) {
					die();
				}
			}
		}

		super.update(deltaTime, gravity); // Aplicar gravedad.


	}
	
	@Override
	public void ouch(float damage) {
		audioM.playSound(hurtSound, 0.3f, 0.75f, 1.25f);
		super.ouch(damage);
	}

	@Override
	public void render(SpriteBatch batch) {
		//		boolean flip = (getDirX() == Direcciones.LEFT);
		
		batch.setColor(gameScreen.tint);

		switch (animState) {
		case IDLE:
			animM.drawStaticSprite(batch, idleImage, this);
			break;
			
		case JUMPING:
			animM.drawStaticSprite(batch, jumpImage, this);
			break;

		case HURT:
			animM.drawAnimSprite(batch, hurtAnimation, this, false);
			break;

		case VERYHURT:
			animM.drawAnimSprite(batch, veryHurtAnimation, this, false);
			break;

		case SLEEPING:
			animM.drawStaticSprite(batch, sleepImage, this);
			break;
			
		case FALLING:
			animM.drawStaticSprite(batch, fallImage, this);
			break;
			
		case WALKING:
			animM.drawAnimSprite(batch, walkAnimation, this, true);
			
		}
		
		batch.setColor(Color.WHITE);

	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		System.out.println("RIP");

		audioM.playSound(deathSound, 0.25f);

		remove = true;
	}

	@Override
	public void sleep() {
		newAnimState = AnimationState.SLEEPING;
	}
	
	public void walkToLeft(float delta) {
		if (!hit && !dontWalk) {
			moveX(-SPEED * delta);	
			this.dirX = Direcciones.LEFT;
			newAnimState = AnimationState.WALKING;
		}
		
		if (!moving) { moving = true; }
		
	}
	
	public void walkToRight(float delta) {
		if (!hit && !dontWalk) {
			moveX(SPEED * delta);
			this.dirX = Direcciones.RIGHT;
			newAnimState = AnimationState.WALKING;
		}
		
		if (!moving) { moving = true; }
	}
	
	public void jump() {
		if (jumpDelayTimer > 1f) {
			if (!dontJump) {
				
				System.out.println("Jump");
				
				jumping = true;
				
				newAnimState = AnimationState.JUMPING;
				System.out.println(animState);
				System.out.println(animState);
				
				this.velocityY += JUMP_VELOCITY * getWeight();

				audioM.playSound(jumpSound, 0.5f, 0.65f, 0.85f);
				
			}	
			
			jumpDelayTimer = 0f;
		}
	}
	
	public boolean isMoving() {
		return moving;
	}

}


