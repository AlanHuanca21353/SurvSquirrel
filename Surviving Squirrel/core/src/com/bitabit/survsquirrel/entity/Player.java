package com.bitabit.survsquirrel.entity;

import javax.print.attribute.standard.PagesPerMinute;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitabit.survsquirrel.InputManager;
import com.bitabit.survsquirrel.enums.AnimationState;
import com.bitabit.survsquirrel.enums.Direcciones;
import com.bitabit.survsquirrel.screens.GameScreen;

public class Player extends Entity {   

	private static final float SHOOT_WAIT_TIME = 0.4f;

	private static final float SMACK_WAIT_TIME = 0.25f;
	
	private static final int SPEED = 160;
	
	private static float speedMultiplier = 1f;
	
	private static final int RUN_SPEED = 240;
	private static final int JUMP_VELOCITY = 4;

	InputManager inputManager;

	Texture idleImage, walkImage, jumpImage, fallImage, 
	shootImage, chargeImage, hurtImage, deadImage,
	smackImage;
	
	TextureRegion[] animationFrames;
	
	Animation<TextureRegion> walkAnimation, jumpAnimation, 
	fallAnimation, chargeAnimation, hurtAnimation,
	smackAnimation;

	float shootTimer, shootDelayTimer, 
	smackTimer, smackDelayTimer, atkStartX,
	iFrames;

	public Sound jumpSound, slingShotSound, hurtSound, 
	deathSound, reviveSound, walkSound;

	private boolean movingRight, movingLeft, moving, jumping, 
	chargingShot, shooting, smacking, dontWalk, dontJump, invincible;

	private boolean dead;
	
	private boolean camFollow;
	
	public Player(float x, float y, GameScreen gS) {
		this(x,y,gS,false);
	}

	public Player(float x, float y, GameScreen gS, boolean camFollow) {
		super(x, y, EntityType.PLAYER, gS);

		hp = 100;
		bigOuchLimit = 50;
		
		idleImage = new Texture("imagenes/nuez.png");
		walkImage = new Texture("imagenes/nuez_walk.png");
		jumpImage = new Texture("imagenes/nuez_jump.png");
		fallImage = new Texture("imagenes/nuez_fall.png");
		shootImage = new Texture("imagenes/nuez_shoot.png");
		chargeImage = new Texture("imagenes/nuez_charge.png");
		hurtImage = new Texture("imagenes/nuez_ouch.png");
		deadImage = new Texture("imagenes/nuez_dead.png");
		smackImage = new Texture("imagenes/nuez_attack.png");

		walkAnimation = animM.genEntAnimation(12, walkImage, this, 4, 2, 2);
		jumpAnimation = animM.genEntAnimation(12, jumpImage, this , 4, 2, 2);
		fallAnimation = animM.genEntAnimation(8, fallImage, this, 2);
		chargeAnimation = animM.genEntAnimation(12, chargeImage, this, 16, 4, 4);
		hurtAnimation = animM.genEntAnimation(12, hurtImage, this, 2);
		smackAnimation = animM.genEntAnimation(24, smackImage, this, 4, 2, 2);
		
		this.camFollow = camFollow;
		
		shootTimer = 0f;

		inputManager = gS.inputM;

		dirX = Direcciones.RIGHT;
		dirY = Direcciones.NONE;

		jumpSound = audioM.createNewSound("Sound/jump.wav");
		walkSound = audioM.createNewSound("Sound/walkSound.wav");
		slingShotSound = audioM.createNewSound("Sound/slingshot.wav");

		hurtSound = audioM.createNewSound("Sound/hurtNoise.wav");
		deathSound = audioM.createNewSound("Sound/death.wav");
		reviveSound = audioM.createNewSound("Sound/revive.mp3");

		movingLeft = false;
		movingRight = false;
		moving = false;
		jumping = false;
		shooting = false;
		chargingShot = false;
		smacking = false;
	}

	@Override
	public void update(float deltaTime, float gravity) {
		
		if (invincible) {
			if (iFrames >= 0.2f) {
				invincible = false;
				iFrames = 0f;
			}
			else {
				iFrames += deltaTime;
			}
		}
		
		if (hp < 0) {
			hp = 0;
		}
		
//		if (moving) {
//			System.out.println("Se ha recorrido una distancia de " +  velX + " px");
//		}
		
		if (velX == 0 && grounded) {
			newAnimState = AnimationState.IDLE;
		}

		if (!dead) {
			shootDelayTimer += deltaTime;
			smackDelayTimer += deltaTime;

			//		System.out.println(animTime);

			if (!dontJump) {
				if (inputManager.isKeyDown(Input.Keys.SPACE) || inputManager.isKeyDown(Input.Keys.W) && !dontJump && !inputManager.isKeyPressed(Input.Keys.F)) {
					jumping = true;
				}

				if (inputManager.isKeyReleased(Input.Keys.SPACE) || inputManager.isKeyReleased(Input.Keys.W) && !dontJump && !inputManager.isKeyPressed(Input.Keys.F)) {
					jumping = false;
				}	
			}


			if (movingLeft || movingRight) {
				moving = true;
			}
			else {
				moving = false;
			}

			if (grounded) { // Si estás en el suelo

				if (!moving && !chargingShot && !hit) { // Si no se está moviendo, activar Idle
					newAnimState = AnimationState.IDLE;
				}

				if (inputManager.isKeyDown(Input.Keys.F) && shootDelayTimer >= SHOOT_WAIT_TIME && !moving){ // Cargar
					chargingShot = true;

					if (inputManager.isKeyDown(Input.Keys.A)) { // Mirar Izquierda
						this.dirX = Direcciones.LEFT;
					}

					if (inputManager.isKeyDown(Input.Keys.D)) { // Mover Derecha
						this.dirX = Direcciones.RIGHT;
					}
				}
				if (inputManager.isKeyReleased(Input.Keys.F) && shootDelayTimer >= SHOOT_WAIT_TIME && !moving && !smacking){ // Disparar

					shootDelayTimer = 0f;

					audioM.playSound(slingShotSound, 0.6f, 0.9f, 1.1f);

					chargingShot = false;
					shooting = true;
				}

				if (jumping && !chargingShot && !shooting) { // Salto inicial
					this.velocityY += JUMP_VELOCITY * getWeight();

					audioM.playSound(jumpSound, 0.5f, 0.9f, 1.1f);

					newAnimState = AnimationState.JUMPING;
				}
			}
			else { // Si estás en el aire

				// Si mantenes pulsado despues de saltar, saltas mas alto.
				if (jumping && this.velocityY > 0) {
					this.velocityY += JUMP_VELOCITY * getWeight() * deltaTime;
					
					newAnimState = AnimationState.JUMPING;
					
				}
			}
			
			if (inputManager.isKeyPressed(Input.Keys.G) && smackDelayTimer >= SMACK_WAIT_TIME && !chargingShot) {
				smackDelayTimer = 0f;
				
				audioM.playSound(slingShotSound, 0.6f, 0.5f, 0.7f);
				
				smacking = true;
				
			}

			if (!dontWalk) { // Si se permite caminar

				if (inputManager.isKeyDown(Input.Keys.A) && !inputManager.isKeyPressed(Input.Keys.F)) {
					movingLeft = true;
				} 
				if (inputManager.isKeyReleased(Input.Keys.A)) {
					movingLeft = false;
				}
				if (inputManager.isKeyDown(Input.Keys.D) && !inputManager.isKeyPressed(Input.Keys.F)) {
					movingRight = true;
				}
				if (inputManager.isKeyReleased(Input.Keys.D)) {
					movingRight = false;
				}
			}

			if (!grounded && this.velocityY < 0) {
				newAnimState = AnimationState.FALLING;
			}

			if (chargingShot && !jumping && !moving) {
				// Está cargando el disparo
				newAnimState = AnimationState.CHARGESHOT;
				dontWalk = true;
				dontJump = true;
			}

			if (shooting) {
				shootTimer += deltaTime;
				newAnimState = AnimationState.SHOOT;
				if (shootTimer >= 0.2f) {
					shootTimer = 0f;
					shooting = false;
					dontWalk = false;
					dontJump = false;
				}
			}
			
			if (smacking) {
				if (grounded) {
					speedMultiplier = 0.7f;	
				}
				else {
					speedMultiplier = 1f;
				}
				smackTimer += deltaTime;
				newAnimState = AnimationState.SMACK;
				if (smackTimer >= 0.16f) {
					smackTimer = 0f;
					smacking = false;
					speedMultiplier = 1f;
				}
				
			}

			// Mover
			if (movingLeft && !hit && !chargingShot) { // Mover Izquierda
				
				
				
				if (inputManager.isKeyDown(Input.Keys.SHIFT_LEFT)) {
					moveX(-RUN_SPEED * deltaTime * speedMultiplier);
				}
				else {
					moveX(-SPEED * deltaTime * speedMultiplier);	
				}

				this.dirX = Direcciones.LEFT;
				if (!jumping && grounded && !smacking && velX != 0) {
					newAnimState = AnimationState.WALKING;	
				}


			}

			if (movingRight && !hit && !chargingShot) { // Mover Derecha
				if (inputManager.isKeyDown(Input.Keys.SHIFT_LEFT)) {
					moveX(RUN_SPEED * deltaTime * speedMultiplier);
				}
				else {
					moveX(SPEED * deltaTime * speedMultiplier);	
				}

				this.dirX = Direcciones.RIGHT;
				if (!jumping && grounded && !smacking && velX != 0) {
					newAnimState = AnimationState.WALKING;	
				}


			}

			if (movingLeft && movingRight) {
				if (grounded && !jumping) {
					newAnimState = AnimationState.IDLE;	
				}
			}

			if (hit) {
				
				movingLeft = movingRight = jumping = false;
				
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
					moveX(tempSpeed * 1.5f * deltaTime);
					this.newAnimState = AnimationState.HURT;
				} else {
					this.newAnimState = AnimationState.HURT;
				}

				if (ouchTimer >= 0.3f) {

					ouchTimer = 0f;
					hit = dontWalk = dontJump = bigOuch = hitLeft = hitRight = false;
					invincible = true;

					if (hp <= 0f) {
						die();
					}
				}
			}
		}
		else {
			newAnimState = AnimationState.DEAD;
			
//			if (inputManager.isKeyPressed(Input.Keys.Q)) {
//				revive();
//			}
			
		}
		super.update(deltaTime, gravity); // Aplicar gravedad.

	}

	@Override
	public void render(SpriteBatch batch) {
		
		batch.setColor(this.gameScreen.tint);

		switch (animState) {
		case IDLE:
			animM.drawStaticSprite(batch, idleImage, this);
			break;

		case WALKING:
			animM.drawAnimSprite(batch, walkAnimation, this, true);
			break;

		case JUMPING:
			animM.drawAnimSprite(batch, jumpAnimation, this, false);
			break;

		case FALLING:
			animM.drawAnimSprite(batch, fallAnimation, this, true);
			break;

		case SHOOT:
			animM.drawStaticSprite(batch, shootImage, this);
			break;

		case CHARGESHOT:
			animM.drawAnimSprite(batch, chargeAnimation, this, false);
			break;
			
		case HURT:
			animM.drawAnimSprite(batch, hurtAnimation, this, false);
			break;
		
		case DEAD:
			animM.drawStaticSprite(batch, deadImage, this);
			break;
		
		case SMACK:
			animM.drawAnimSprite(batch, smackAnimation, this, false);
			break;
			
		}
		
		batch.setColor(Color.WHITE);

	}

	@Override
	public void ouch(float damage) {
		audioM.playSound(hurtSound, 0.3f, 0.75f, 1.25f);
		super.ouch(damage);
	}

	public void render(OrthographicCamera cam, SpriteBatch batch) {
		batch.setProjectionMatrix(cam.combined);
		this.render(batch);
	}

	public float getVelocityY() {
		return velocityY;
	}


	public boolean isGrounded() {
		return grounded;
	}

	public boolean isMoving() {
		return moving;
	}

	public boolean isJumping() {
		return jumping;
	}

	public boolean isShooting() {
		return shooting;
	}

	public boolean isChargingShot() {
		return chargingShot;
	}
	
	public boolean isSmacking() {
		return smacking;
	}

	public void die() {
		// TODO Auto-generated method stub
		
		dirX = Direcciones.RIGHT;
		
		System.out.println("RIP");

		audioM.playSound(deathSound, 0.25f);
		dead = true;

	}
	
	public void revive() {
		System.out.println("Volvió a la vida");
		
		hp = 100;
		dead = false;
	
	}

	public boolean isDead() {
		return dead;
	}
	
	public boolean isInvincible() {
		return invincible;
	}
	
	public boolean canCamFollow() {
		return camFollow;
	}


}
