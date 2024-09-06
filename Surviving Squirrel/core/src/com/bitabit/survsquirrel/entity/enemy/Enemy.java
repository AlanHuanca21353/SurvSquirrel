package com.bitabit.survsquirrel.entity.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitabit.survsquirrel.entity.Entity;
import com.bitabit.survsquirrel.entity.EntityType;
import com.bitabit.survsquirrel.enums.AnimationState;
import com.bitabit.survsquirrel.screens.GameScreen;

public abstract class Enemy extends Entity {
	
	protected AnimationState animState;
	
	protected float hp;
	
	protected int bigOuchLimit;
	
	protected boolean bigOuch = false;

	public Enemy(float x, float y, EntityType type, GameScreen gameScreen) {
		super(x, y, type, gameScreen);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}
	
	public void ouch(float damage) {
		if (damage > bigOuchLimit) {
			bigOuch = true;
		}
		hp -= damage;
		System.out.println(hp);
	}
	
	public float getHP() {
		return hp;
	}
	
	public abstract void sleep();
	
	public abstract void wakeUp();

	public abstract void die();
}
