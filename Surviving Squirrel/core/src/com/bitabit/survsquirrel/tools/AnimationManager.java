package com.bitabit.survsquirrel.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitabit.survsquirrel.entity.Entity;
import com.bitabit.survsquirrel.enums.Direcciones;

public class AnimationManager {
	
	/** Genera una Animación usando un Spritesheet de multiples filas.
	 * @param fps - La velocidad en Frames por Segundo
	 * @param img - La imagen base
	 * @param tileWidth - Anchura de cada frame
	 * @param tileHeight - Altura de cada frame
	 * @param frameMax - La cantidad de frames en total
	 * @param rowMax - La cantidad de filas en total
	 * @param colMax - La cantidad de columnas en total
	 * @return - Devuelve una Animación
	 */
	public Animation<TextureRegion> genAnimation(float fps, Texture img, int tileWidth, int tileHeight, int frameMax, int rowMax, int colMax){
		
		TextureRegion[] animFrames = genAnimFrames(img, tileWidth, tileHeight, frameMax, rowMax, colMax);
		
		Animation<TextureRegion> anim = new Animation<TextureRegion>(1f/fps, animFrames);
		
		return anim;
		
	}
	
	/** Genera una Animación usando un Spritesheet de una sola fila.
	 * @param fps - La velocidad en Frames por Segundo
	 * @param img - La imagen base
	 * @param tileWidth - Anchura de cada frame
	 * @param tileHeight - Altura de cada frame
	 * @param frameMax - La cantidad de frames en total
	 * @param colMax - La cantidad de columnas en total
	 * @return - Devuelve una Animación
	 */
	public Animation<TextureRegion> genAnimation(float fps, Texture img, int tileWidth, int tileHeight, int frameMax, int colMax){
		
		TextureRegion[] animFrames = genAnimFrames(img, tileWidth, tileHeight, frameMax, colMax);
		
		Animation<TextureRegion> anim = new Animation<TextureRegion>(1f/fps, animFrames);
		
		return anim;
		
	}

	/** Genera un TextureRegion con los frames necesarios para una animación, 
	 * utilizando un Spritesheet de multiples filas.
	 * @param img - La imagen base
	 * @param tileWidth - Anchura de cada frame
	 * @param tileHeight - Altura de cada frame
	 * @param frameMax - La cantidad de frames en total
	 * @param colMax - La cantidad de columnas en total
	 * @return - Devuelve un TextureRegion
	 */
	public TextureRegion[] genAnimFrames(Texture img, int tileWidth, int tileHeight, int frameMax, int rowMax, int colMax) {

		TextureRegion[] animFrames;
		TextureRegion[][] tempFrames = TextureRegion.split(img, tileWidth, tileHeight);

		animFrames = new TextureRegion[frameMax];
		int index = 0;

		for (int row = 0; row < rowMax; row++) {
			for (int col = 0; col < colMax; col++) {
				animFrames[index++] = tempFrames[row][col];
			}
		}

		return animFrames;
	}

	
	/** Genera un TextureRegion con los frames necesarios para una animación, 
	 * utilizando un Spritesheet de una sola fila.
	 * @param img - La imagen base
	 * @param tileWidth - Anchura de cada frame
	 * @param tileHeight - Altura de cada frame
	 * @param frameMax - La cantidad de frames en total
	 * @param colMax - La cantidad de columnas en total
	 * @return - Devuelve un TextureRegion
	 */
	public TextureRegion[] genAnimFrames(Texture img, int tileWidth, int tileHeight, int frameMax, int colMax) {

		TextureRegion[] animFrames;
		TextureRegion[][] tempFrames = TextureRegion.split(img, tileWidth, tileHeight);

		animFrames = new TextureRegion[frameMax];
		int index = 0;

		for (int col = 0; col < colMax; col++) {
			animFrames[index++] = tempFrames[0][col];
		}

		return animFrames;
	}
	
	
	
	/** Dibuja el frame actual de la animación de una entidad.
	 * @param batch - Spritebatch
	 * @param anim - Animacion
	 * @param e - Entidad (jugador, enemigo, etc). Usualmente solo se llena con 'this'
	 * @param loop - Booleano que determina si la animación va a estar en bucle o no.
	 */
	public void drawAnimSprite(SpriteBatch batch, Animation<TextureRegion> anim, 
			Entity e, boolean loop) {
		
		boolean flip = (e.getDirX() == Direcciones.LEFT);
		float tempX, tempWidth;
		
		if (flip) {
			tempX = e.getX() + e.getSpriteWidth();
			tempWidth = -e.getSpriteWidth();
		}
		else {
			tempX = e.getX();
			tempWidth = e.getSpriteWidth();
		}
		
		batch.draw(anim.getKeyFrame(e.getAnimTime(), loop), tempX, e.getY(), tempWidth, e.getSpriteHeight());	
		
	}
	
	/** Dibuja el sprite de una entidad.
	 * @param batch - Spritebatch
	 * @param image - Textura
	 * @param e - Entidad (jugador, enemigo, etc). Usualmente solo se llena con 'this'
	 */
	public void drawStaticSprite(SpriteBatch batch, Texture image, 
			Entity e) {
		
		boolean flip = (e.getDirX() == Direcciones.LEFT);
		float tempX, tempWidth;
		
		if (flip) {
			tempX = e.getX() + e.getSpriteWidth();
			tempWidth = -e.getSpriteWidth();
		}
		else {
			tempX = e.getX();
			tempWidth = e.getSpriteWidth();
		}
		
		batch.draw(image, tempX, e.getY(), tempWidth, e.getSpriteHeight());	
		
	}

}
