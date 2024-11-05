package com.bitabit.survsquirrel.entity.attack;

import com.bitabit.survsquirrel.entity.Entity;
import com.bitabit.survsquirrel.entity.EntityType;
import com.bitabit.survsquirrel.enums.Direcciones;
import com.bitabit.survsquirrel.screens.GameScreen;

public class SquirrelTail extends Attack {
	
	float tailTime;
	
	Entity e;
	
	private Direcciones dirX;

	public SquirrelTail(float x, float y, GameScreen gS, float power, Direcciones dirX) {
		super(x, y, EntityType.SQUIRRELTAIL, gS, power);
		
		this.dirX = dirX;
		
	}
	
	public SquirrelTail(Entity e, GameScreen gS, float power) {
		this(e.getAtkStartX(), e.getY(), gS, power, e.getDirX());
		
		if (e.getDirX() == Direcciones.LEFT) {
			pos.x -= 30;
		}
		else {
			pos.x += 30;
		}
		
	}
	
	public void update (float deltaTime) {
		tailTime += deltaTime;
		
		if (tailTime >= 0.25f) {
			remove = true;
			System.out.println("Chau hitbox cola");
		}
		
		super.update(deltaTime, 0f);
		
	}
	
	public void setX(float x) {
		pos.x = x;
	}
	
	public Direcciones getDirX() {
		return dirX;
	}

	public void setDirX(Direcciones dirX) {
		this.dirX = dirX;
	}

}
