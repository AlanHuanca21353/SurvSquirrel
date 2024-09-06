package com.bitabit.survsquirrel.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TiledGameMap {
	
	TiledMap tiledMap;
	OrthogonalTiledMapRenderer tiledMapRenderer;

	public TiledGameMap() {
		tiledMap = new TmxMapLoader().load("map.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		
	}
	
	public TiledGameMap(String mapPath) {
		tiledMap = new TmxMapLoader().load(mapPath);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		
	}
	
	public void render(OrthographicCamera camera, SpriteBatch batch) {
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.end();
	}

	public void update(float delta) { // No agregar nada
		
	}

	public void dispose() {
		tiledMap.dispose();
	}

	public TiledMap getTiledMap() {
		return tiledMap;
	}

	public OrthogonalTiledMapRenderer getTiledMapRenderer() {
		return tiledMapRenderer;
	}
	
	
	
//	public TileType getTileTypeByLocation(int layer, float x, float y) {
//		return this.getTileTypeByCoordinate(layer, (int) (x / TileType.TILE_SIZE), (int) (y / TileType.TILE_SIZE));
//	}
//
//	public TileType getTileTypeByCoordinate(int layer, int col, int row) {
//		
//		Cell cell = ((TiledMapTileLayer) tiledMap.getLayers().get(layer)).getCell(col, row);
//		
//		if (cell != null) {
//			TiledMapTile tile = cell.getTile();
//			
//			if (tile != null) {
//				int id = tile.getId();
//				return TileType.getTileTypeByID(id);
//			}
//		}
//		
//		return null;
//	}
//	
//	public boolean doesRectCollideWithMap(float x, float y, int width, int height) {
//		if (x<0 || y<0 || x + width > getPixelWidth() || y + height > getPixelHeight()) {
//			return true;
//		}
//		
//		for (int row = (int) (y / TileType.TILE_SIZE); row < Math.ceil((y+height)) / TileType.TILE_SIZE; row++) {
//			for (int col = (int) (x / TileType.TILE_SIZE); col < Math.ceil((x+width)) / TileType.TILE_SIZE; col++) {
//				for (int layer = 0; layer < getLayers(); layer++) {
//					TileType type = getTileTypeByCoordinate(layer, col, row);
//					if (type != null && type.isCollidable()) {
//						return true;
//					}
//				}
//			}
//		}
//		
//		return false;
//		
//	}
//	
//	public boolean doesRectCollideWithMap(float x, float y, int width, int height, int leftBound) {
//		if (x+leftBound<0 || y<0 || x+leftBound+width > getPixelWidth() || y + height > getPixelHeight()) {
//			return true;
//		}
//		
//		for (int row = (int) (y / TileType.TILE_SIZE); row < Math.ceil((y+height)) / TileType.TILE_SIZE; row++) {
//			for (int col = (int) ((x+leftBound) / TileType.TILE_SIZE); col < Math.ceil((x+leftBound+width)) / TileType.TILE_SIZE; col++) {
//				for (int layer = 0; layer < getLayers(); layer++) {
//					TileType type = getTileTypeByCoordinate(layer, col, row);
//					if (type != null && type.isCollidable()) {
//						return true;
//					}
//				}
//			}
//		}
//		
//		return false;
//		
//	}
//
//	public int getWidth() {
//		return ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth();
//	}
//
//	public int getHeight() {
//		return ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight();
//	}
//
//	public int getLayers() {
//		return tiledMap.getLayers().getCount();
//	}
//	
//	public int getPixelWidth() {
//		return this.getWidth() * TileType.TILE_SIZE;
//	}
//	
//	public int getPixelHeight() {
//		return this.getHeight() * TileType.TILE_SIZE;
//	}

}
