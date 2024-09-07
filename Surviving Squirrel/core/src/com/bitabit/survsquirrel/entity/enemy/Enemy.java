package com.bitabit.survsquirrel.entity.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitabit.survsquirrel.entity.Entity;
import com.bitabit.survsquirrel.entity.EntityType;
import com.bitabit.survsquirrel.enums.AnimationState;
import com.bitabit.survsquirrel.screens.GameScreen;
import com.bitabit.survsquirrel.tools.RandomGenerator;

public abstract class Enemy extends Entity {
	
	protected AnimationState animState;
	
	protected float hp;
	
	protected int bigOuchLimit;
	
	protected boolean bigOuch = false;
	
	Sound hurtSound, deathSound;

	public Enemy(float x, float y, EntityType type, GameScreen gameScreen) {
		super(x, y, type, gameScreen);
		
		hurtSound = Gdx.audio.newSound(Gdx.files.internal("Sound/hurtNoise.wav"));
		deathSound = Gdx.audio.newSound(Gdx.files.internal("Sound/death.wav"));
		
	}

	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}
	
	public void ouch(float damage) {
		if (damage > bigOuchLimit) {
			bigOuch = true;
			
			long id = hurtSound.play(1.0f);
			hurtSound.setLooping(id, false);
			hurtSound.setVolume(id, 0.3f);
			hurtSound.setPitch(id, rg.genRandomFloat(0.75f, 1.25f));
			
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
