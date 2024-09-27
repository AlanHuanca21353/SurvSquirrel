package com.bitabit.survsquirrel.entity.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitabit.survsquirrel.entity.Entity;
import com.bitabit.survsquirrel.entity.EntityType;
import com.bitabit.survsquirrel.enums.AnimationState;
import com.bitabit.survsquirrel.enums.Direcciones;
import com.bitabit.survsquirrel.screens.GameScreen;
import com.bitabit.survsquirrel.tools.RandomGenerator;

public abstract class Enemy extends Entity {
	
	Sound hurtSound, deathSound;
	
	boolean awaken = false;

	public boolean isAwaken() {
		return awaken;
	}

	public Enemy(float x, float y, EntityType type, GameScreen gameScreen) {
		super(x, y, type, gameScreen);
		
		hurtSound = audioM.createNewSound("Sound/hurtNoise.wav");
		deathSound = audioM.createNewSound("Sound/death.wav");
		
	}

	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}
	
	public abstract void sleep();
	
	public void backToSleep() {
		System.out.println("Se durmió un " + type.getId());
		awaken = false;
	}
	
	public void wakeUp() {
		System.out.println("Se despertó un " + type.getId());
		awaken = true;
	}

	public abstract void die();
}
