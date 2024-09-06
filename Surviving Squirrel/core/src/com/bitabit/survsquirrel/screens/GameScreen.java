package com.bitabit.survsquirrel.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector3;
import com.bitabit.survsquirrel.InputManager;
import com.bitabit.survsquirrel.Principal;
import com.bitabit.survsquirrel.entity.Player;
import com.bitabit.survsquirrel.entity.attack.Bullet;
import com.bitabit.survsquirrel.entity.enemy.Enemy;
import com.bitabit.survsquirrel.entity.enemy.EnemyRat;
import com.bitabit.survsquirrel.enums.Direcciones;
//import com.bitabit.survsquirrel.Rebotante;
import com.bitabit.survsquirrel.world.TileType;
import com.bitabit.survsquirrel.world.TiledGameMap;

public class GameScreen implements Screen{

	private static final float SHOOT_WAIT_TIME = 0.4f;
	private static final int MAP_LEFTBOUNDARY = 320;

	OrthographicCamera cam;
	
	
	public SpriteBatch batch;
	public InputManager inputManager;
	
//	protected ArrayList<Entity> entities;
	
	Player player;
	ArrayList<Enemy> enemies;
	
	ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();
	ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
	
	final Principal game;
	
	TiledGameMap gameMap;
	
	ArrayList<Bullet> bullets;
	
	float shootDelayTimer, chargeTimer, power;
	
	boolean charging = false;
	
	public GameScreen(final Principal game) {
		batch = new SpriteBatch();
		inputManager = new InputManager();
	    Gdx.input.setInputProcessor(inputManager);
		this.game = game;
		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
//		entities =  new ArrayList<Entity>();
		
		shootDelayTimer = 0f;
		
	}
	
	private void debugMoverCamara() {
//		if (Gdx.input.isTouched()) {
//			cam.translate(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
//			cam.update();
//		}
		
		if (player.getX() + player.getLeftBoundary() < MAP_LEFTBOUNDARY) {
			cam.position.set(MAP_LEFTBOUNDARY, player.getY()+20, 0);
		}
		else if (player.getX() + player.getLeftBoundary() > getPixelWidth()-MAP_LEFTBOUNDARY){
			cam.position.set(getPixelWidth()-MAP_LEFTBOUNDARY, player.getY()+20, 0);
		}
		else {
			cam.position.set(player.getX() + player.getLeftBoundary(), player.getY()+20, 0);
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
		
		player = new Player(40, 700, this);
		enemies.add(new EnemyRat(60, 700, this));
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
		shootDelayTimer += delta;
		
		if (chargeTimer < 2 && charging) {
			chargeTimer += delta;
		}
		
		debugMoverCamara();
//		debugDetectarTile();
		
		cam.update();
		gameMap.update(Gdx.graphics.getDeltaTime());
        gameMap.render(cam, game.batch);
        
        if (inputManager.isKeyReleased(Input.Keys.NUM_1)) {
        	gameMap = new TiledGameMap("map.tmx");
        	player.setPos(40, 700);
        	
        	for (Enemy e : enemies) {
    			e.remove = true;
    			
    			if (e.gotRemoved()) {
    				enemiesToRemove.add(e);
    			}
    			
    		}
        	enemies.removeAll(enemiesToRemove);
        	
        	enemies.add(new EnemyRat(60, 700, this));
        	
        }
        
        if (inputManager.isKeyReleased(Input.Keys.NUM_3)) {
        	gameMap = new TiledGameMap("3.tmx");
        	player.setPos(40, 700);
        	
        	for (Enemy e : enemies) {
    			e.remove = true;
    			
    			if (e.gotRemoved()) {
    				enemiesToRemove.add(e);
    			}
    			
    		}
        	enemies.removeAll(enemiesToRemove);
        	
        }
        
        
        if (inputManager.isKeyReleased(Input.Keys.NUM_2)) {
        	enemies.add(new EnemyRat(player.getX()+70, player.getY()+100, this));
        }
		
        if (player.isChargingShot() && shootDelayTimer >= SHOOT_WAIT_TIME && !player.isMoving()) { // Cargar disparo
        	charging = true;
		}
        
        if (player.isShooting() && shootDelayTimer >= SHOOT_WAIT_TIME && !player.isMoving()) { // Disparar
        	shootDelayTimer = 0f;
        	if (player.isGrounded()) {
	        	if (player.getDirX() == Direcciones.LEFT) {
	        		bullets.add(new Bullet(player.getX(), player.getY()+20, this, Math.round(chargeTimer)));	
	        	}
	        	else if (player.getDirX() == Direcciones.RIGHT){
	        		bullets.add(new Bullet(player.getX()+player.getSpriteWidth(), player.getY()+20, this, Math.round(chargeTimer)));
	        	}
				long id = player.jumpSound.play(0.1f);
        	}
        	chargeTimer = 0f;
        	charging = false;
        }
        
        batch.begin();
		
//		for (Entity entity : entities) {
//			entity.render(batch);
//		}
        
        for (Bullet bullet : bullets) {
			bullet.render(batch);
		}
		
		player.render(cam, batch);
		
		for (Enemy e : enemies) {
			e.render(batch);
		}
		
//		for (Entity entity : entities) {
//			entity.update(delta, -9.8f);
//		}
		
		player.update(delta, -9.8f);

		for (Enemy e : enemies) {
			e.update(delta, -9.8f);
			
			if (e.gotRemoved()) {
				enemiesToRemove.add(e);
			}
			
		}
		
		for (Bullet bullet : bullets) {
			
			if (bullet.getDirX() == null) {
				bullet.setDirX(player.getDirX());
			}
			
			if (bullet.getDirX() == Direcciones.RIGHT) {
				bullet.moveX(bullet.getSpeed() * delta * (bullet.getPower()/2)+1);
			}
			else if (bullet.getDirX() == Direcciones.LEFT) {
				bullet.moveX(-bullet.getSpeed() * delta * (bullet.getPower()/2)-1);
			}
			
			bullet.update(delta, -9.8f);
			
			for (Enemy e : enemies) {
				if (bullet.collide(e)){
					System.out.println("Ouch!");
					
					e.ouch(bullet.getDmg());
					
					bullet.remove = true;
					e.hit = true;
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

}
