package com.bitabit.survsquirrel.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bitabit.survsquirrel.enums.AnimationState;
import com.bitabit.survsquirrel.enums.Direcciones;
import com.bitabit.survsquirrel.screens.GameScreen;
import com.bitabit.survsquirrel.tools.AnimationMethods;
import com.bitabit.survsquirrel.tools.AudioMethods;
import com.bitabit.survsquirrel.tools.RandomGenerator;
import com.bitabit.survsquirrel.world.TileType;

public abstract class Entity {

	protected float hp;

	protected int bigOuchLimit;

	protected boolean bigOuch = false, hitLeft, hitRight;

	protected Vector2 pos;
	protected EntityType type;
	protected float velocityY = 0;
	protected GameScreen gameScreen;
	protected boolean grounded = false;
	
	private float lastPosX;

	protected float velX;

	protected Direcciones dirX, dirY;

	protected AnimationState newAnimState = AnimationState.IDLE, animState = AnimationState.IDLE;

	public boolean hit = false;
	public boolean remove = false, animChange = false;

	protected float ouchTimer;

	public AudioMethods audioM = new AudioMethods();
	public AnimationMethods animM = new AnimationMethods();

	protected RandomGenerator rg;

	protected float animTime;
	
	public float atkStartX;

	public Entity(float x, float y, EntityType type, GameScreen gameScreen) {
		this.pos = new Vector2(x,y);
		this.type = type;
		this.gameScreen = gameScreen;

		rg = gameScreen.rg;

	}

	public void update(float deltaTime, float gravity) {
		
		velX = pos.x-lastPosX;
		lastPosX = pos.x;
		
		if (getDirX() == Direcciones.LEFT) {
			atkStartX = getX() + getLeftBoundary();	
		}
		else if (getDirX() == Direcciones.RIGHT) {
			atkStartX = getX() + getCollisionWidth();
		}

		if (animState != newAnimState) {
			animChange = true;
			animState = newAnimState;
		}

		if (animChange) {
//			System.out.println("Animation Changed to: " + newAnimState);
			animTime = 0;
			animChange = false;
		}

		animTime += deltaTime;

		float newY = pos.y;

		this.velocityY += gravity * deltaTime * getWeight();
		newY += this.velocityY * deltaTime;

//		if (gameScreen.doesRectCollideWithMap(pos.x, newY, getCollisionWidth(), getCollisionHeight(), getLeftBoundary())) {
		if (gameScreen.gameMap.doesRectCollideWithMap(pos.x, newY, getCollisionWidth(), getCollisionHeight(), getLeftBoundary())) {
			if (velocityY < 0) {
				this.pos.y = (float) Math.floor(pos.y);
				grounded = true;
			}
			this.velocityY = 0;
		}
		else {
			this.pos.y = newY;
			grounded = false;
		}
	}

	public void ouch(float damage) {
		
		animTime = 0f;

		hp -= damage;
		System.out.println(hp);

		hit = true;

	}

	public void ouch(float damage, Direcciones dirX) {
		if (damage >= bigOuchLimit) {
			bigOuch = true;
		}

		switch (dirX) {
		case LEFT:
			hitRight = true;
			break;

		case RIGHT:
			hitLeft = true;
			break;

		default:
			break;
		}

		ouch(damage);

	}
	
	public void bigOuch(float damage, Direcciones dirX) {
		bigOuch = true;
		
		switch (dirX) {
		case LEFT:
			hitRight = true;
			break;

		case RIGHT:
			hitLeft = true;
			break;

		default:
			break;
		}
		
		ouch(damage);
	}

	public float getHP() {
		return hp;
	}

	public abstract void render(SpriteBatch batch);

	public void moveX(float amount) {
		float newX = this.pos.x + amount;
		if (!gameScreen.gameMap.doesRectCollideWithMap(newX, pos.y, getCollisionWidth(), getCollisionHeight(), getLeftBoundary())) {
			this.pos.x = newX;
		}

	}

	public int checkDistanceTiles(Entity ent) {
		return (int) checkDistance(ent)/TileType.TILE_SIZE;
	}
	
	public float checkDistance(Entity ent) {
		float a, b;
		if (pos.x < ent.getPos().x) {
			a = pos.x;
			b = ent.getPos().x;
		}
		else {
			a = ent.getPos().x;
			b = pos.x;
		}

		return b-a;
	}
	
	public float checkDistanceLR(Entity ent) {
		float a, b;
		
		a = pos.x;
		b = ent.getPos().x;

		return b-a;
	}
	
	public float checkDistanceLRTiles(Entity ent) {
		return (int) checkDistanceLR(ent)/TileType.TILE_SIZE;
	}
	
	public float getAtkStartX() {
		return atkStartX;
	}

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(int newX, int newY) {
		pos = new Vector2(newX,newY);
	}

	protected Rectangle getArea() {
		return new Rectangle(pos.x+getLeftBoundary(), pos.y, getCollisionWidth(), getCollisionHeight());
	}

	public boolean collide(Entity ent) {
		Rectangle rect1 = getArea();
		Rectangle rect2 = ent.getArea();

		return rect1.overlaps(rect2);
	}

	public Direcciones getDirX() {
		return dirX;
	}

	public float getX() {
		return pos.x;
	}

	public float getY() {
		return pos.y;
	}

	public EntityType getType() {
		return type;
	}

	public boolean isGrounded() {
		return grounded;
	}

	public int getSpriteWidth() {
		return type.getSpriteWidth();
	}

	public int getSpriteHeight() {
		return type.getSpriteHeight();
	}

	public int getCollisionWidth() {
		return type.getCollisionWidth();
	}

	public int getCollisionHeight() {
		return type.getCollisionHeight();
	}

	public int getLeftBoundary() {
		return type.getLeftBoundary();
	}

	public float getWeight() {
		return type.getWeight();
	}

	public float getAnimTime() {
		return animTime;
	}

	public boolean gotHit() {
		return hit;
	}

	public boolean gotRemoved() {
		return remove;
	}
	
	public float getVelX() {
		return velX;
	}


}
