package com.bitabit.survsquirrel.events;

import java.util.EventListener;

public interface ChangeMapEvent extends EventListener{
	void changeMap(int num);
}
