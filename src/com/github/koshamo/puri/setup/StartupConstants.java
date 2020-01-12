package com.github.koshamo.puri.setup;

public final class StartupConstants {
	
	public final int VICTORY_POINTS;
	public final int COLONISTS;
	public final int MIN_COLONISTS;
	public final int MIN_PLANTATION;
	public final int SMALL_SHIP_PLACES;
	public final int MEDIUM_SHIP_PLACES;
	public final int LARGE_SHIP_PLACES;
	
	
	public StartupConstants(int players) {
		MIN_COLONISTS = players;
		MIN_PLANTATION = players + 1;
		
		SMALL_SHIP_PLACES = players + 1;
		MEDIUM_SHIP_PLACES = players + 2;
		LARGE_SHIP_PLACES = players + 3;
		
		if (players == 3) {
			VICTORY_POINTS = 75;
			COLONISTS = 55;
		}
		else if (players == 4) {
			VICTORY_POINTS = 100;
			COLONISTS = 75;
		}
		else if (players == 5) {
			VICTORY_POINTS = 126;
			COLONISTS = 95;
		}
		else {
			VICTORY_POINTS = 0;
			COLONISTS = 0;
		}
	}
}
