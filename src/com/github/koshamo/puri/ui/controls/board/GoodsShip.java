package com.github.koshamo.puri.ui.controls.board;

import com.github.koshamo.puri.setup.PlantationType;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class GoodsShip extends Control {

	private final GoodsShipSkin skin;
	private final int places;
	
	private PlantationType type;
	private int amount;
	
	public GoodsShip(int places) {
		skin = new GoodsShipSkin(this, places);
		this.places = places;
	}
	
	public void addGoods(PlantationType type, int amount) {
		this.type = type;
		this.amount += amount;
		update();
	}
	
	public PlantationType type() {
		return type;
	}
	
	public int storageLeft() {
		return places - amount;
	}
	
	public void clear() {
		type = PlantationType.NONE;
		amount = 0;
		update();
	}
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return skin;
	}
	
	public void update() {
		skin.drawComponent(type, amount);
	}
}