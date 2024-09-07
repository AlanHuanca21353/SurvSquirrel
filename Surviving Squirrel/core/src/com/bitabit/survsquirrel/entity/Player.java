package com.bitabit.survsquirrel.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
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
	
	private static final int SPEED = 160;
	private static final int RUN_SPEED = 240;
	private static final int JUMP_VELOCITY = 4;
	
	InputManager inputManager;
	
	private AnimationState animState;
	private Direcciones dirX, dirY;
	
	Texture idleImage, walkImage, jumpImage, fallImage, shootImage, chargeImage;
	TextureRegion[] animationFrames;
	Animation<TextureRegion> walkAnimation, jumpAnimation, fallAnimation, chargeAnimation;
	
	float animTime, shootTimer, shootDelayTimer;
	
	public Sound jumpSound, slingShotSound;
	Sound walkSound;
	
	private boolean movingRight, movingLeft, moving, jumping, chargingShot, shooting, dontWalk, dontJump;
	
	public Player(float x, float y, GameScreen gameScreen) {
		super(x, y, EntityType.PLAYER, gameScreen);
		
		idleImage = new Texture("imagenes/nuez.png");
		walkImage = new Texture("imagenes/nuez_walk.png");
		jumpImage = new Texture("imagenes/nuez_jump.png");
		fallImage = new Texture("imagenes/nuez_fall.png");
		shootImage = new Texture("imagenes/nuez_shoot.png");
		chargeImage = new Texture("imagenes/nuez_charge.png");
		
		walkAnimation = new Animation<TextureRegion>(1f/12f,genAnimFrames(walkImage, 44, 40, 4, 2, 2));
		jumpAnimation = new Animation<TextureRegion>(1f/12f,genAnimFrames(jumpImage, 44, 40, 4, 2, 2));
		fallAnimation = new Animation<TextureRegion>(1f/8f,genAnimFrames(fallImage, 44, 40, 2, 2));
		chargeAnimation = new Animation<TextureRegion>(1f/2f,genAnimFrames(chargeImage, 44, 40, 4, 2, 2));
		
		animTime = 0f;
		shootTimer = 0f;
		
		inputManager = gameScreen.inputManager;
		
		dirX = Direcciones.RIGHT;
		dirY = Direcciones.NONE;
		animState = AnimationState.IDLE;
		
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("Sound/jump.wav"));
		walkSound = Gdx.audio.newSound(Gdx.files.internal("Sound/walkSound.wav"));
		slingShotSound = Gdx.audio.newSound(Gdx.files.internal("Sound/slingshot.wav"));
		
		movingLeft = false;
		movingRight = false;
		moving = false;
		jumping = false;
		shooting = false;
		chargingShot = false;
	}
	
	@Override
	public void update(float deltaTime, float gravity) {
		
		animTime += deltaTime;
		shootDelayTimer += deltaTime;
		
		
		if (!dontJump) {
			if (inputManager.isKeyDown(Input.Keys.SPACE) || inputManager.isKeyDown(Input.Keys.W) && !dontJump) {
				jumping = true;
			}
			
			if (inputManager.isKeyReleased(Input.Keys.SPACE) || inputManager.isKeyReleased(Input.Keys.W) && !dontJump) {
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
			
			if (!moving && !chargingShot) { // Si no se está moviendo, activar Idle
				animTime = 0f;
				this.animState = AnimationState.IDLE;
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
			if (inputManager.isKeyReleased(Input.Keys.F) && shootDelayTimer >= SHOOT_WAIT_TIME){ // Disparar
				
				shootDelayTimer = 0f;
				
				chargingShot = false;
				shooting = true;
			}
			
			if (jumping) { // Salto inicial
				this.velocityY += JUMP_VELOCITY * getWeight();
				long id = jumpSound.play(1.0f);
				jumpSound.setLooping(id, false);
				jumpSound.setVolume(id, 0.5f);
				jumpSound.setPitch(id, rg.genRandomFloat(0.9f, 1.1f));
				this.animState = AnimationState.JUMPING;
			}
		}
		else { // Si estás en el aire

			// Si mantenes pulsado despues de saltar, saltas mas alto.
			if (jumping && this.velocityY > 0) {
				this.velocityY += JUMP_VELOCITY * getWeight() * deltaTime;
			}
		}
		
		if (!dontWalk) { // Si se permite caminar
			
			if (inputManager.isKeyDown(Input.Keys.A)) {
				movingLeft = true;
			} 
			if (inputManager.isKeyReleased(Input.Keys.A)) {
				movingLeft = false;
			}
			if (inputManager.isKeyDown(Input.Keys.D)) {
				movingRight = true;
			}
			if (inputManager.isKeyReleased(Input.Keys.D)) {
				movingRight = false;
			}
		}
		
		if (!grounded && this.velocityY < 0) {
			this.animState = AnimationState.FALLING;
		}
		
		if (chargingShot && !jumping) {
			// Está cargando el disparo
			this.animState = AnimationState.CHARGESHOT;
			dontWalk = true;
			dontJump = true;
		}
		
		if (shooting) {
			shootTimer += deltaTime;
			this.animState = AnimationState.SHOOT;
			if (shootTimer >= 0.2f) {
				shootTimer = 0f;
				shooting = false;
				dontWalk = false;
				dontJump = false;
			}
		}
		
		// Mover
		if (movingLeft) { // Mover Izquierda
			if (inputManager.isKeyDown(Input.Keys.SHIFT_LEFT)) {
				moveX(-RUN_SPEED * deltaTime);
			}
			else {
				moveX(-SPEED * deltaTime);	
			}
			
			this.dirX = Direcciones.LEFT;
			if (!jumping && grounded) {
				this.animState = AnimationState.WALKING;	
			}
			
			
		}
		
		if (movingRight) { // Mover Derecha
			if (inputManager.isKeyDown(Input.Keys.SHIFT_LEFT)) {
				moveX(RUN_SPEED * deltaTime);
			}
			else {
				moveX(SPEED * deltaTime);	
			}
			
			this.dirX = Direcciones.RIGHT;
			if (!jumping && grounded) {
				this.animState = AnimationState.WALKING;	
			}
			
			
		}
		
		if (movingLeft && movingRight) {
			if (grounded && !jumping) {
				this.animState = AnimationState.IDLE;	
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
		else if (this.animState == AnimationState.WALKING) {
			batch.draw(walkAnimation.getKeyFrame(animTime, true), flip ? pos.x + getSpriteWidth() : pos.x, pos.y, flip ? -getSpriteWidth() : getSpriteWidth(), getSpriteHeight());	
		}
		else if (this.animState == AnimationState.JUMPING) {
			batch.draw(jumpAnimation.getKeyFrame(animTime, false), flip ? pos.x + getSpriteWidth() : pos.x, pos.y, flip ? -getSpriteWidth() : getSpriteWidth(), getSpriteHeight());	
		}
		else if (this.animState == AnimationState.FALLING) {
			batch.draw(fallAnimation.getKeyFrame(animTime, true), flip ? pos.x + getSpriteWidth() : pos.x, pos.y, flip ? -getSpriteWidth() : getSpriteWidth(), getSpriteHeight());	
		}
		else if (this.animState == AnimationState.SHOOT) {
			batch.draw(shootImage, flip ? pos.x + getSpriteWidth() : pos.x, pos.y, flip ? -getSpriteWidth() : getSpriteWidth(), getSpriteHeight());
		}
		else if (this.animState == AnimationState.CHARGESHOT) {
			batch.draw(chargeAnimation.getKeyFrame(animTime, false), flip ? pos.x + getSpriteWidth() : pos.x, pos.y, flip ? -getSpriteWidth() : getSpriteWidth(), getSpriteHeight());	
		}
		
	}
	
	public void render(OrthographicCamera cam, SpriteBatch batch) {
		batch.setProjectionMatrix(cam.combined);
		this.render(batch);
	}

	public Direcciones getDirX() {
		return dirX;
	}
	
	public float getVelocityY() {
		return velocityY;
	}

	
	public boolean isGrounded() {
		return grounded;
	}
	
	
	// Meter todo esto en una Clase Diferente para que pueda ser utilizada por diferentes Entidades
	
	public TextureRegion[] genAnimFrames(Texture image, int tileWidth, int tileHeight, int frameMax, int rowMax, int colMax) {
		
		TextureRegion[][] tempFrames = TextureRegion.split(image, tileWidth, tileHeight);
		
		animationFrames = new TextureRegion[frameMax];
		int index = 0;
		
		for (int row = 0; row < rowMax; row++) {
			for (int col = 0; col < colMax; col++) {
				animationFrames[index++] = tempFrames[row][col];
			}
		}
		
		return animationFrames;
	}
	
	public TextureRegion[] genAnimFrames(Texture image, int tileWidth, int tileHeight, int frameMax, int colMax) {
		
		TextureRegion[][] tempFrames = TextureRegion.split(image, tileWidth, tileHeight);
		
		animationFrames = new TextureRegion[frameMax];
		int index = 0;
		
		for (int col = 0; col < colMax; col++) {
			animationFrames[index++] = tempFrames[0][col];
		}
		
		return animationFrames;
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
	
	
}
