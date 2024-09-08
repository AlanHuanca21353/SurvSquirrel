package com.bitabit.survsquirrel.tools;

import java.util.Random;

public class RandomGenerator {

	Random r = new Random();
	
	/** Elige un int aleatorio.
	 * @param min - Minimo posible. (Ej: 0)
	 * @param max - Maximo posible. (Ej: 100)
	 * @return
	 */
	public int genRandomInt(int min, int max) {
		return r.nextInt(min,max+1);
	}
	
	/** Elige un float aleatorio.
	 * @param min - Minimo posible. (Ej: 1.5f)
	 * @param max - Maximo posible. (Ej: 45.93f)
	 * @return
	 */
	public float genRandomFloat(float min, float max) {
		return r.nextFloat(min,max);
	}
	
}
