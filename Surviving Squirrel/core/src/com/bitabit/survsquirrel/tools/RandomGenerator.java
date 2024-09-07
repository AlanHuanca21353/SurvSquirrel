package com.bitabit.survsquirrel.tools;

import java.util.Random;

public class RandomGenerator {

	Random r = new Random();
	
	public int genRandomInt(int min, int max) {
		return r.nextInt(min,max+1);
	}
	
	public float genRandomFloat(float min, float max) {
		return r.nextFloat(min,max);
	}
	
}
