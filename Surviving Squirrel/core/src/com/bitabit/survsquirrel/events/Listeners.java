package com.bitabit.survsquirrel.events;

import java.util.ArrayList;
import java.util.EventListener;

public abstract class Listeners {

	private static ArrayList<EventListener> listeners = new ArrayList<EventListener>();
	
	public static void addListeners(EventListener e) {
		if (!listeners.contains(e)) {
			listeners.add(e);
		}
		
	}
	
	public static void executeMapChange(int num) {
		for (int i=0;i<listeners.size();i++) {
			
			if (listeners.get(i) instanceof ChangeMapEvent) {
				((ChangeMapEvent)listeners.get(i)).changeMap(num);
				
			}
		}
		
		
		
	}
	
	
}
