package com.bitabit.survsquirrel.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bitabit.survsquirrel.screens.GameScreen;

public abstract class Entity {
	
	protected Vector2 pos;
	protected EntityType type;
	protected float velocityY = 0;
	protected GameScreen gameScreen;
	protected boolean grounded = false;
	public boolean hit = false;
	public boolean remove = false;

	public Entity(float x, float y, EntityType type, GameScreen gameScreen) {
		this.pos = new Vector2(x,y);
		this.type = type;
		this.gameScreen = gameScreen;
	}
	
	public void update(float deltaTime, float gravity) {
		float newY = pos.y;
		
		this.velocityY += gravity * deltaTime * getWeight();
		newY += this.velocityY * deltaTime;
		
		if (gameScreen.doesRectCollideWithMap(pos.x, newY, getCollisionWidth(), getCollisionHeight(), getLeftBoundary())) {
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
	
	public abstract void render(SpriteBatch batch);
	
	public void moveX(float amount) {
		float newX = this.pos.x + amount;
		if (!gameScreen.doesRectCollideWithMap(newX, pos.y, getCollisionWidth(), getCollisionHeight(), getLeftBoundary())) {
			this.pos.x = newX;
		}
		
	}

	public Vector2 getPos() {
		return pos;
	}
	
	public void setPos(int newX, int newY) {
		pos = new Vector2(newX,newY);
	}
	
	protected Rectangle getArea() {
		return new Rectangle(pos.x+type.getLeftBoundary(), pos.y, type.getCollisionWidth(), type.getCollisionHeight());
	}
	
	public boolean collide(Entity ent) {
		Rectangle rect1 = getArea();
		Rectangle rect2 = ent.getArea();
		
		return rect1.overlaps(rect2);
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
	
	public boolean gotHit() {
		return hit;
	}
	
	public boolean gotRemoved() {
		return remove;
	}
	
	
}
