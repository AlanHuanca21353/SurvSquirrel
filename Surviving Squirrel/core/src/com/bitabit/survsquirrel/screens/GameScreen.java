package com.bitabit.survsquirrel.screens;

import java.util.ArrayList;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector3;
import com.bitabit.survsquirrel.InputManager;
import com.bitabit.survsquirrel.Principal;
import com.bitabit.survsquirrel.entity.EntityType;
import com.bitabit.survsquirrel.entity.Player;
import com.bitabit.survsquirrel.entity.attack.Bullet;
import com.bitabit.survsquirrel.entity.enemy.Enemy;
import com.bitabit.survsquirrel.entity.enemy.EnemyRat;
import com.bitabit.survsquirrel.enums.Direcciones;
import com.bitabit.survsquirrel.tools.RandomGenerator;
//import com.bitabit.survsquirrel.Rebotante;
import com.bitabit.survsquirrel.world.TileType;
import com.bitabit.survsquirrel.world.TiledGameMap;

public class GameScreen implements Screen{

	private static final float SHOOT_WAIT_TIME = 0.4f;
	private static final int MAP_LEFTBOUNDARY = 320;

	OrthographicCamera cam;

	public RandomGenerator rg = new RandomGenerator();
	
	public SpriteBatch batch;
	public InputManager inputManager;
	
//	protected ArrayList<Entity> entities;
	
	Player p, p2;
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();
	ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
	
	final Principal game;
	
	TiledGameMap gameMap;
	
	float shootDelayTimer, chargeTimer, power;
	
	boolean charging = false, mapChange = true;

	TiledMapTileLayer collisionLayer, entitiesLayer;
	
	public GameScreen(final Principal game) {
		batch = new SpriteBatch();
		inputManager = new InputManager();
	    Gdx.input.setInputProcessor(inputManager);
		this.game = game;
		
		shootDelayTimer = 0f;
		
	}
	
	private void debugMoverCamara() {
//		if (Gdx.input.isTouched()) {
//			cam.translate(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
//			cam.update();
//		}
		
		if (p.getX() + p.getLeftBoundary() < MAP_LEFTBOUNDARY) {
			cam.position.set(MAP_LEFTBOUNDARY, p.getY()+20, 0);
		}
		else if (p.getX() + p.getLeftBoundary() > getPixelWidth()-MAP_LEFTBOUNDARY){
			cam.position.set(getPixelWidth()-MAP_LEFTBOUNDARY, p.getY()+20, 0);
		}
		else {
			cam.position.set(p.getX() + p.getLeftBoundary(), p.getY()+20, 0);
		}
	}
	
	private void debugDetectarTile() {
		if (Gdx.input.justTouched()) {
			Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			TileType type = this.getTileTypeByLocation(1, pos.x, pos.y);
			
			if (type != null) {
				System.out.println("Haz hecho clic al Tile de ID " + type.getId() + ": " + type.getName() + ", " + type.isCollidable() + ", " + type.getDamage());
			}
		}
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		System.out.println("Empezar Juego");
		
		float w = Gdx.graphics.getWidth()*0.5f;
        float h = Gdx.graphics.getHeight()*0.5f;
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, w, h);
		cam.update();
		
		gameMap = new TiledGameMap("map.tmx");
		
		collisionLayer = (TiledMapTileLayer) gameMap.getTiledMap().getLayers().get("Colisiones");
		entitiesLayer = (TiledMapTileLayer) gameMap.getTiledMap().getLayers().get("Entities");
		
		entitySpawner(getWidth(), getHeight(), entitiesLayer, this);

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
		if (mapChange) {
			collisionLayer = (TiledMapTileLayer) gameMap.getTiledMap().getLayers().get("Colisiones");
			entitiesLayer = (TiledMapTileLayer) gameMap.getTiledMap().getLayers().get("Entities");
			
			entitySpawner(getWidth(), getHeight(), entitiesLayer, this);
			
			collisionLayer.setVisible(false);
			entitiesLayer.setVisible(false);
			
			mapChange = false;
		}
		
		shootDelayTimer += delta;
		
		if (chargeTimer < 2 && charging) {
			chargeTimer += delta;
		}
		
		debugMoverCamara();
//		debugDetectarTile();
		
		cam.update();
		gameMap.update(Gdx.graphics.getDeltaTime());
        gameMap.render(cam, game.batch);
        
        if (inputManager.isKeyReleased(Input.Keys.V)) {
        	collisionLayer.setVisible(!collisionLayer.isVisible());
        }
        
        if (inputManager.isKeyReleased(Input.Keys.E)) {
        	entitiesLayer.setVisible(!entitiesLayer.isVisible());
        }
        
        if (inputManager.isKeyReleased(Input.Keys.NUM_0)) { // Matar a todos los enemigos
        	for (Enemy e : enemies) {
    			e.ouch(9999f);
    			
    			if (e.gotRemoved()) {
    				enemiesToRemove.add(e);
    			}
    			
    		}
        }
        
        if (inputManager.isKeyReleased(Input.Keys.NUM_1)) { // Mapa 1
        	gameMap = new TiledGameMap("map.tmx");
        	
        	for (Enemy e : enemies) {
    			e.remove = true;
    			
    			if (e.gotRemoved()) {
    				enemiesToRemove.add(e);
    			}
    			
    		}
        	
        	enemies.removeAll(enemiesToRemove);
        	
        	mapChange = true;
        	
        }
        
        if (inputManager.isKeyReleased(Input.Keys.NUM_3)) { // Mapa 3
        	gameMap = new TiledGameMap("3.tmx");
        	
        	for (Enemy e : enemies) {
    			e.remove = true;
    			
    			if (e.gotRemoved()) {
    				enemiesToRemove.add(e);
    			}
    			
    		}
        	enemies.removeAll(enemiesToRemove);
        	
        	mapChange = true;
        	
        }
        
        
        if (inputManager.isKeyReleased(Input.Keys.NUM_2)) { // Spawnear Rata
        	enemies.add(new EnemyRat(p.getX()+70, p.getY()+100, this));
        }
		
        if (p.isChargingShot() && shootDelayTimer >= SHOOT_WAIT_TIME && !p.isMoving()) { // Cargar disparo
        	charging = true;
		}
        
        if (p.isShooting() && shootDelayTimer >= SHOOT_WAIT_TIME && !p.isMoving()) { // Disparar
        	shootDelayTimer = 0f;
        	if (p.isGrounded()) {
	        	if (p.getDirX() == Direcciones.LEFT) {
	        		bullets.add(new Bullet(p.getX(), p.getY()+20, this, Math.round(chargeTimer), p.getDirX()));	
	        	}
	        	else if (p.getDirX() == Direcciones.RIGHT){
	        		bullets.add(new Bullet(p.getX()+p.getSpriteWidth(), p.getY()+20, this, Math.round(chargeTimer), p.getDirX()));
	        	}
        	}
        	chargeTimer = 0f;
        	charging = false;
        }
        
        batch.begin();
        
        for (Bullet bullet : bullets) {
			bullet.render(batch);
		}
		
		p.render(cam, batch);
		
		for (Enemy e : enemies) {
			e.render(batch);
		}
		
		p.update(delta, -9.8f);

		for (Enemy e : enemies) {
			e.update(delta, -9.8f);
			
			if (e.gotRemoved()) {
				enemiesToRemove.add(e);
			}
			
		}
		
		for (Bullet bullet : bullets) {
			
			if (bullet.getDirX() == null) {
				bullet.setDirX(p.getDirX());
			}
			
			bullet.update(delta, -9.8f);
			
			for (Enemy e : enemies) {
				if (bullet.collide(e)){
					System.out.println("Ouch!");
					
					e.ouch(bullet.getDmg());
					
					bullet.remove = true;
				}
			}
			

			if (bullet.isGrounded()) {
				bullet.remove = true;
			}
			
			if (bullet.gotRemoved()) {
				bulletsToRemove.add(bullet);
			}
		}
		
		enemies.removeAll(enemiesToRemove);
		bullets.removeAll(bulletsToRemove);
        
		batch.end();
		
		inputManager.update();
		
//		System.out.println("Juego Siempre");
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
//		System.out.println("Cambio de Tamaño de Ventana");
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	
		System.out.println("Volver al Inicio");
	}
	
	public TileType getTileTypeByLocation(int layer, float x, float y) {
		return this.getTileTypeByCoordinate(layer, (int) (x / TileType.TILE_SIZE), (int) (y / TileType.TILE_SIZE));
	}

	public TileType getTileTypeByCoordinate(int layer, int col, int row) {
		
		Cell cell = ((TiledMapTileLayer) gameMap.getTiledMap().getLayers().get(layer)).getCell(col, row);
		
		if (cell != null) {
			TiledMapTile tile = cell.getTile();
			
			if (tile != null) {
				int id = tile.getId();
				return TileType.getTileTypeByID(id);
			}
		}
		
		return null;
	}
	
	public boolean doesRectCollideWithMap(float x, float y, int width, int height) {
		if (x<0 || y<0 || x + width > getPixelWidth() || y + height > getPixelHeight()) {
			return true;
		}
		
		for (int row = (int) (y / TileType.TILE_SIZE); row < Math.ceil((y+height)) / TileType.TILE_SIZE; row++) {
			for (int col = (int) (x / TileType.TILE_SIZE); col < Math.ceil((x+width)) / TileType.TILE_SIZE; col++) {
				for (int layer = 0; layer < getLayers(); layer++) {
					TileType type = getTileTypeByCoordinate(layer, col, row);
					if (type != null && type.isCollidable()) {
						return true;
					}
				}
			}
		}
		
		return false;
		
	}
	
	public boolean doesRectCollideWithMap(float x, float y, int width, int height, int leftBound) {
		if (x+leftBound<0 || y<0 || x+leftBound+width > getPixelWidth() || y + height > getPixelHeight()) {
			return true;
		}
		
		for (int row = (int) (y / TileType.TILE_SIZE); row < Math.ceil((y+height)) / TileType.TILE_SIZE; row++) {
			for (int col = (int) ((x+leftBound) / TileType.TILE_SIZE); col < Math.ceil((x+leftBound+width)) / TileType.TILE_SIZE; col++) {
				for (int layer = 0; layer < getLayers(); layer++) {
					TileType type = getTileTypeByCoordinate(layer, col, row);
					if (type != null && type.isCollidable()) {
						return true;
					}
				}
			}
		}
		
		return false;
		
	}

	public int getWidth() {
		return ((TiledMapTileLayer) gameMap.getTiledMap().getLayers().get(0)).getWidth();
	}

	public int getHeight() {
		return ((TiledMapTileLayer) gameMap.getTiledMap().getLayers().get(0)).getHeight();
	}

	public int getLayers() {
		return gameMap.getTiledMap().getLayers().getCount();
	}
	
	public int getPixelWidth() {
		return this.getWidth() * TileType.TILE_SIZE;
	}
	
	public int getPixelHeight() {
		return this.getHeight() * TileType.TILE_SIZE;
	}
	
	public void entitySpawner(int mapWidth, int mapHeight, TiledMapTileLayer layer, GameScreen gameScreen) {
		
		boolean hasPlayerSpawned = false;
		
		for (int row = 0; row < mapWidth; row++) {
			for (int col = 0; col < mapHeight; col++) {
				Cell cell = layer.getCell(row, col);
				
				if (cell != null) {
					
					MapProperties props = cell.getTile().getProperties();
					
					if (props.containsKey("spawner")) {
						
						if (props.get("spawner", String.class).equals("player") && !hasPlayerSpawned) {
							
							p = new Player(row * TileType.TILE_SIZE, col * TileType.TILE_SIZE, gameScreen);
							
							hasPlayerSpawned = true;
							
						}
						
						if (props.get("spawner", String.class).equals("rat")) {
						
							enemies.add(new EnemyRat(row * TileType.TILE_SIZE, col * TileType.TILE_SIZE, gameScreen));
							
						}
						
					}
					
				}
			}
		}
		
		if (!hasPlayerSpawned) {
			p = new Player(40, 800, gameScreen);
		}
		
	}

}
