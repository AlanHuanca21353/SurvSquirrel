package com.bitabit.survsquirrel.entity;

public enum EntityType {
	
	PLAYER("player", 44, 40, 20, 20, 12, 100),
	ENEMYRAT("rat", 40, 60, 20, 40, 10, 100),
	BULLET("bullet", 5, 5, 18);
	
	private String id;
	private int spriteWidth, spriteHeight, collisionW, collisionH, leftBoundary;
	private float weight;
	
	

	private EntityType(String id, int spriteWidth, int spriteHeight, int collisionW, int collisionH, int leftBoundary, float weight) {
		this.id = id;
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		this.collisionW = collisionW;
		this.collisionH = collisionH;
		this.leftBoundary = leftBoundary;
		this.weight = weight;
	}
	
	private EntityType(String id, int width, int height, float weight) {
		this.id = id;
		spriteWidth = width;
		spriteHeight = height;
		collisionW = width;
		collisionH = height;
		leftBoundary = 0;
		this.weight = weight;
	}


	public String getId() {
		return id;
	}

	public int getSpriteWidth() {
		return spriteWidth;
	}

	public int getSpriteHeight() {
		return spriteHeight;
	}
	
	public int getCollisionWidth() {
		return collisionW;
	}

	public int getCollisionHeight() {
		return collisionH;
	}

	public float getWeight() {
		return weight;
	}

	public int getLeftBoundary() {
		return leftBoundary;
	}
	
}
