package com.bitabit.survsquirrel.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class AudioMethods {
	
	private RandomGenerator rg = new RandomGenerator();

	/** Crea un Efecto de Sonido nuevo.
	 * @param path - La ruta del archivo a utilizar (puede ser ogg, wav, mp3, etc)
	 * @return Devuelve el sonido creado.
	 */
	public Sound createNewSound(String path) {
		Sound newSound = Gdx.audio.newSound(Gdx.files.internal(path));
		return newSound;
	}
	
	/** Reproduce un Sonido.
	 * @param sound - El sonido a reproducir.
	 */
	public void playSound(Sound sound) {
		sound.play();
	}
	
	/** Reproduce un Sonido.
	 * @param sound - El sonido a reproducir.
	 * @param volume - El volumen del sonido.
	 */
	public void playSound(Sound sound, float volume) {
		sound.play(volume);
	}
	
	/** Reproduce un Sonido.
	 * @param sound - El sonido a reproducir.
	 * @param volume - El volumen del sonido.
	 * @param pitch - El tono del sonido. (1.0f es el tono original)
	 */
	public void playSound(Sound sound, float volume, float pitch) {
		long id = sound.play(volume);
		sound.setPitch(id, pitch);
	}
	
	/** Reproduce un Sonido. Su tono es determinado aleatoriamente, para dar mayor variedad.
	 * @param sound - El sonido a reproducir.
	 * @param volume - El volumen del sonido.
	 * @param minPitch - El tono más grave posible. (1.0f es el tono original)
	 * @param maxPitch - El tono más agudo posible. (1.0f es el tono original)
	 */
	public void playSound(Sound sound, float volume, float minPitch, float maxPitch) {
		
		long id = sound.play(volume);
		sound.setPitch(id, rg.genRandomFloat(minPitch, maxPitch));
		
	}
	
}
