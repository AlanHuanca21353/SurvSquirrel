package com.bitabit.survsquirrel.entity.attack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bitabit.survsquirrel.entity.Entity;
import com.bitabit.survsquirrel.entity.EntityType;
import com.bitabit.survsquirrel.entity.Player;
import com.bitabit.survsquirrel.screens.GameScreen;

public abstract class Attack extends Entity {
	
	float dmg;
	
	float power = 0f;

	public Attack(float x, float y, EntityType type, GameScreen gS, float power) {
		super(x, y, type, gS);
		
		this.power = power;
		
		dmg = 10*(power+1);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}
	
	public float getPower() {
		return power;
	}
	
	public float getDmg() {
		return dmg;
	}

	public void setPower(float power) {
		this.power = power;
	}
	
}
