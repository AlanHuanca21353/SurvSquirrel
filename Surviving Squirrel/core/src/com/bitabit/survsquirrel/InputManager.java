package com.bitabit.survsquirrel;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

public class InputManager implements InputProcessor{
	
	public Array<KeyState> keyStates = new Array<KeyState>();
	
	public InputManager() {
	    //create the initial state of every key on the keyboard.
	    //There are 256 keys available which are all represented as integers.
	    for (int i = 0; i < 256; i++) {
	        keyStates.add(new KeyState(i));
	    }
	}

	public class InputState {
	    public boolean pressed = false;
	    public boolean down = false;
	    public boolean released = false;
	}
	
	public class KeyState extends InputState{
	    //the keyboard key of this object represented as an integer.
	    public int key;

	    public KeyState(int key){
	        this.key = key;
	    }
	}


	@Override
	public boolean keyDown(int keycode) {
		//this function only gets called once when an event is fired. (even if this key is being held down)

	    //I need to store the state of the key being held down as well as pressed
	    keyStates.get(keycode).pressed = true;
	    keyStates.get(keycode).down = true;

	    //every overridden method needs a return value. I won't be utilizing this but it can be used for error handling.
	    return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		//the key was released, I need to set it's down state to false and released state to true
	    keyStates.get(keycode).down = false;
	    keyStates.get(keycode).released = true;
	    return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}
	

	public void update(){
		  //for every keystate, set pressed and released to false.
		  for (int i = 0; i < 256; i++) {
		      KeyState k = keyStates.get(i);
		      k.pressed = false;
		      k.released = false;
		  }
		}
	
	//check states of supplied key
	public boolean isKeyPressed(int key){
	    return keyStates.get(key).pressed;
	}
	public boolean isKeyDown(int key){
	    return keyStates.get(key).down;
	}
	public boolean isKeyReleased(int key){
	    return keyStates.get(key).released;
	}

	
}
