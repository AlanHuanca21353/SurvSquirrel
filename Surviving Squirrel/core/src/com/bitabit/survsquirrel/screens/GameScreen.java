package com.bitabit.survsquirrel.screens;

import java.util.ArrayList;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bitabit.survsquirrel.InputManager;
import com.bitabit.survsquirrel.Principal;
import com.bitabit.survsquirrel.entity.EntityType;
import com.bitabit.survsquirrel.entity.Player;
import com.bitabit.survsquirrel.entity.attack.Bullet;
import com.bitabit.survsquirrel.entity.attack.SquirrelTail;
import com.bitabit.survsquirrel.entity.enemy.Enemy;
import com.bitabit.survsquirrel.entity.enemy.EnemyRat;
import com.bitabit.survsquirrel.enums.Direcciones;
import com.bitabit.survsquirrel.events.ChangeMapEvent;
import com.bitabit.survsquirrel.events.Listeners;
import com.bitabit.survsquirrel.hud.GameHud;
import com.bitabit.survsquirrel.tools.RandomGenerator;
//import com.bitabit.survsquirrel.Rebotante;
import com.bitabit.survsquirrel.world.TileType;
import com.bitabit.survsquirrel.world.TiledGameMap;

/**
 * 
 */
public class GameScreen implements Screen, ChangeMapEvent{
	
	float delta;
	
	private GameHud hud;
	private FitViewport fitViewport;

	private static final float SHOOT_WAIT_TIME = 0.4f;
	private static final float SMACK_WAIT_TIME = 0.4f;
	
	private static final int MAP_LEFTBOUNDARY = 320;

	OrthographicCamera cam;

	public RandomGenerator rg = new RandomGenerator();

	public SpriteBatch batch;
	public InputManager inputM;

	//	protected ArrayList<Entity> entities;

	public Player p;
	public ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	ArrayList<SquirrelTail> tails = new ArrayList<SquirrelTail>();

	ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();
	ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
	ArrayList<SquirrelTail> tailsToRemove = new ArrayList<SquirrelTail>();

	final Principal pr;

	public TiledGameMap gameMap;

	float shootDelayTimer, smackDelayTimer, chargeTimer, power;

	boolean charging = false, mapChange = false;

	TiledMapTileLayer collisionLayer, entitiesLayer;

	public Texture bg;

	public float w, h;

	public Color tint;

	public GameScreen(final Principal pr) {
		this.pr = pr;
		batch = pr.batch;
		fitViewport = pr.fitViewport;
		
		inputM = new InputManager();
		Gdx.input.setInputProcessor(inputM);

		tint = new Color(Color.WHITE);

		shootDelayTimer = 0f;

	}

	private void cameraFollowPlayer() {
		//		if (Gdx.input.isTouched()) {
		//			cam.translate(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
		//			cam.update();
		//		}

		if (p.getX() + p.getLeftBoundary() < MAP_LEFTBOUNDARY) {
			cam.position.set(MAP_LEFTBOUNDARY, p.getY()+20, 0);
		}
		else if (p.getX() + p.getLeftBoundary() > gameMap.getPixelWidth()-MAP_LEFTBOUNDARY){
			cam.position.set(gameMap.getPixelWidth()-MAP_LEFTBOUNDARY, p.getY()+20, 0);
		}
		else {
			cam.position.set(p.getX() + p.getLeftBoundary(), p.getY()+20, 0);
		}
	}

	private void debugDetectarTile() {
		if (Gdx.input.justTouched()) {
			Vector3 pos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			TileType type = gameMap.getTileTypeByLocation(1, pos.x, pos.y);

			if (type != null) {
				System.out.println("Haz hecho clic al Tile de ID " + type.getId() + ": " + type.getName() + ", " + type.isCollidable() + ", " + type.getDamage());
			}
		}
	}

	@Override
	public void show() {
		
		// TODO Auto-generated method stub
		System.out.println("Empezar Juego");

		w = 640;
		h = 360;

		cam = new OrthographicCamera();
		cam.setToOrtho(false, w, h);
		cam.update();

		gameMap = new TiledGameMap("1.tmx");

		bg = new Texture("imagenes/1.png");

		collisionLayer = (TiledMapTileLayer) gameMap.getTiledMap().getLayers().get("Colisiones");
		entitiesLayer = (TiledMapTileLayer) gameMap.getTiledMap().getLayers().get("Entities");

		collisionLayer.setVisible(false);
		entitiesLayer.setVisible(false);

		gameMap.entitySpawner(gameMap.getWidth(), gameMap.getHeight(), entitiesLayer, this);
		
		Listeners.addListeners(this);
		
		hud = new GameHud();

	}

	@Override
	public void render(float deltaTime) {
		
		fitViewport.apply();
		
		batch.setProjectionMatrix(fitViewport.getCamera().combined);
		
		delta = Math.min(1/16f, deltaTime);
		
		if (inputM.isKeyReleased(Input.Keys.P)) {
			pr.setScreen(new MainMenuScreen(pr));
			this.dispose();
		}
		
		// TODO Auto-generated method stub

		// debugDetectarTile();

		//-------------------------------------------------------------------------
		// [Camera]
		//-------------------------------------------------------------------------

		if (p.canCamFollow()) {
			cameraFollowPlayer();	
		}

		cam.update();
//		batch.setProjectionMatrix(cam.combined);

		//-------------------------------------------------------------------------


		//-------------------------------------------------------------------------
		// [Deltatime]
		//-------------------------------------------------------------------------

		shootDelayTimer += delta;
		smackDelayTimer += delta;

		if (chargeTimer < 2.5f && charging) {
			chargeTimer += delta;
		}

		//-------------------------------------------------------------------------
		
		
		
		//-------------------------------------------------------------------------
		// [Renders]
		//-------------------------------------------------------------------------
		
		batch.begin();

		batch.draw(bg, fitViewport.getCamera().position.x - w, fitViewport.getCamera().position.y - h, w*2, h*2);

		batch.end();

		gameMap.render(cam, pr.batch);

		batch.begin();

		for (Bullet bullet : bullets) {
			bullet.render(batch);
		}

		p.render(batch);

		for (Enemy e : enemies) {
			e.render(batch);
		}
		
		batch.end();
		
		//-------------------------------------------------------------------------
		
		//-------------------------------------------------------------------------
		// [Game Logic]
		//-------------------------------------------------------------------------

		if (inputM.isKeyReleased(Input.Keys.V)) {
			collisionLayer.setVisible(!collisionLayer.isVisible());
		}

		if (inputM.isKeyReleased(Input.Keys.E)) {
			entitiesLayer.setVisible(!entitiesLayer.isVisible());
		}


		if (inputM.isKeyReleased(Input.Keys.NUM_0)) { // Matar a todos los enemigos
			for (Enemy e : enemies) {
				e.ouch(9999f);

				if (e.gotRemoved()) {
					enemiesToRemove.add(e);
				}

			}
		}

		if (inputM.isKeyReleased(Input.Keys.NUM_1)) { // Mapa 1
			Listeners.executeMapChange(1);
			tint.set(Color.WHITE);

		}

		if (inputM.isKeyReleased(Input.Keys.NUM_2)) { // Mapa 2
			Listeners.executeMapChange(2);
			tint.set(0.5f, 0.5f, 0.85f, 1f);
		}


		if (inputM.isKeyReleased(Input.Keys.NUM_5)) { // Spawnear Rata
			enemies.add(new EnemyRat(p.getX()+70, p.getY()+100, this));
		}

		if (p.isChargingShot() && shootDelayTimer >= SHOOT_WAIT_TIME && !p.isMoving()) { // Cargar disparo
			charging = true;
		}

		if (p.isShooting() && shootDelayTimer >= SHOOT_WAIT_TIME && !p.isMoving()) { // Disparar
			shootDelayTimer = 0f;
			if (p.isGrounded() && !p.isDead()) {
				bullets.add(new Bullet(p, this, chargeTimer));
			}
			chargeTimer = 0f;
			charging = false;
		}
		
		if (p.isSmacking() && smackDelayTimer >= SMACK_WAIT_TIME) {
			
			smackDelayTimer = 0f;
			
			tails.add(new SquirrelTail(p, this, 0.5f));
			System.out.println("Agregar Cola");
		}
		
//		if (p.isSmacking() && smackDelayTimer >= SMACK_WAIT_TIME && !p.isMoving()) {
//			smackDelayTimer = 0f;
//			if (p.isGrounded() && !p.isDead()) {
//				tails.add(new SquirrelTail(p, this, 2));
//				System.out.println("Cola spawn");
//			}
//		}
		
		for (Enemy e : enemies) {
			
			if (e.gotRemoved()) {
				enemiesToRemove.add(e);
			}	

			if (e instanceof EnemyRat) {
				EnemyRat er = (EnemyRat) e;
				if (er.isAwaken()) {
					if (inputM.isKeyPressed(Input.Keys.K)) {
						bullets.add(new Bullet(er, this, 2, true));
					}
//
//					if (inputM.isKeyPressed(Input.Keys.M)) {
//						System.out.println(e.checkDistance(p));
//					}

					if (er.gotRemoved()) {
						enemiesToRemove.add(er);
					}	
					
//					System.out.println(Math.round(e.checkDistanceLR(p)));
					
					if (Math.round(er.checkDistanceLR(p)) < -20) {
						er.walkToLeft(delta);
					}
					else if (Math.round(er.checkDistanceLR(p)) > 20) {
						er.walkToRight(delta);
					}
					
					else {
						er.moving = false;
					}
					
//					System.out.println(er.isMoving());
					
					if (p.getPos().y > e.getPos().y) {
						if (rg.genRandomInt(1, 20) == 1) {
							er.jump();
						}
					}
					
					if (er.isMoving()){
						if (rg.genRandomInt(1, 200) == 1) {
							er.jump();
						}
					}

					if (er.checkDistanceTiles(p) >= 35) {
						er.backToSleep();
					}

				}
				else {
					if (er.checkDistanceTiles(p) <= 4) {
						er.wakeUp();
					}
				}
			}

		}
		
		for (Bullet b : bullets) {

			if (b.getDirX() == null) {
				b.setDirX(p.getDirX());
			}

			for (Enemy e : enemies) {

				if (!b.hurtPlayer) {
					if (b.collide(e)){
						//						System.out.println("Ouch!");

						e.ouch(b.getDmg(), b.getDirX());

						b.remove = true;
					}	
				}
			}

			if (b.hurtPlayer && !p.isDead()) {
				if (b.collide(p)) {
					//					System.out.println("Ardilla: Ouch!");

					p.ouch(5, b.getDirX());

					b.remove = true;
				}
			}

			if (b.isGrounded()) {
				b.remove = true;
			}

			if (b.gotRemoved()) {
				bulletsToRemove.add(b);
			}
		}
		
		for (SquirrelTail t : tails) {
			
			if (t.getDirX() == null) {
				t.setDirX(p.getDirX());
			}
			
			if (t.getDirX() == Direcciones.LEFT) {
				t.setX(p.getAtkStartX()-20);	
			}
			else {
				t.setX(p.getAtkStartX()+20);	
			}
			
			for (Enemy e : enemies) {
				if (t.collide(e) && !e.gotHit()){
					//						System.out.println("Ouch!");

					System.out.println("¡Pegaste con la cola!");
					
					e.bigOuch(t.getDmg(), t.getDirX());

				}	
			}
			
			
			if (t.gotRemoved()) {
				tailsToRemove.add(t);
			}
		}

		enemies.removeAll(enemiesToRemove);
		bullets.removeAll(bulletsToRemove);
		tails.removeAll(tailsToRemove);
		
		//-------------------------------------------------------------------------
		
		//-------------------------------------------------------------------------
		// [Updates]
		//-------------------------------------------------------------------------
				
		gameMap.update(Gdx.graphics.getDeltaTime());

		p.update(delta, -9.8f);

		for (Enemy e : enemies) {
			e.update(delta, -9.8f);
		}
		
		for (Bullet b : bullets) {
			b.update(delta, -9.8f);
		}
		
		for (SquirrelTail t : tails) {
			t.update(delta);
		}
		
		inputM.update();
				
		//-------------------------------------------------------------------------

		//		System.out.println("Juego Siempre");
		
		hud.setLabel("HP: ", (int) p.getHP());
		
		batch.begin();
		
		hud.dibujar();
		
		batch.end();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

		fitViewport.update(width, height, true);
		
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

	@Override
	public void changeMap(int mapNum) {
		
		gameMap = new TiledGameMap(mapNum + ".tmx");

		for (Enemy e : enemies) {
			e.remove = true;

			if (e.gotRemoved()) {
				enemiesToRemove.add(e);
			}

		}
		enemies.removeAll(enemiesToRemove);

		collisionLayer = (TiledMapTileLayer) gameMap.getTiledMap().getLayers().get("Colisiones");
		entitiesLayer = (TiledMapTileLayer) gameMap.getTiledMap().getLayers().get("Entities");

		gameMap.entitySpawner(gameMap.getWidth(), gameMap.getHeight(), entitiesLayer, this);

		collisionLayer.setVisible(false);
		entitiesLayer.setVisible(false);

		bg = new Texture("imagenes/" + mapNum + ".png"); // Cambiar fondo
	
	}
}
