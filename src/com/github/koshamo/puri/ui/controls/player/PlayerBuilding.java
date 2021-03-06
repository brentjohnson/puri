package com.github.koshamo.puri.ui.controls.player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.github.koshamo.puri.setup.BuildingType;
import com.github.koshamo.puri.setup.BuildingTypeList;
import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.PrColors;
import com.github.koshamo.puri.setup.State;

import javafx.geometry.Insets;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/*private*/ class PlayerBuilding extends Region {

	private final Player player;
	private List<BuildingField> buildings;
	
	private HBox rowOne;
	private HBox rowTwo;

	public PlayerBuilding(Player player) {
		this.player = player;
		buildings = new ArrayList<>(12);
		initGui();
		initBuildings();
		update();
	}

	public void addBuilding(BuildingTypeList type) {
		int index = buildings.size() - type.getSize();
		
		if (!buildings.get(index).type().equals(BuildingTypeList.NONE))
			return;
		
		buildings.get(index).addBuilding(type);
		if (type.getSize() == 2)
			buildings.remove(index + 1);
		sort();
	}
	
	public List<BuildingTypeList> ownedBuildings() {
		List<BuildingTypeList> buildingList = new LinkedList<>();
		for (BuildingField bf : buildings)
			if (!bf.type().equals(BuildingTypeList.NONE))
				buildingList.add(bf.type());
		return buildingList;
	}
	
	public List<BuildingField> ownedBuildingsAsFields() {
		return buildings;
	}
	
	public boolean isBuildingSpaceFull() {
		int occupied = 0;
		for (BuildingField bf : buildings)
			if (!bf.type().equals(BuildingTypeList.NONE))
				occupied += bf.type().getSize();
		if (occupied < 12)
			return false;
		return true;
	}

	public int calcEmptyPlaces() {
		int emptyPlaces = 0;
		for (BuildingField bf : buildings) 
			emptyPlaces += bf.emptyPlaces();
		return emptyPlaces;
	}
	
	public int[] calcProducableProducts() {
		int[] products = new int[4];
		
		products[0] = calcActiveProductions(PlantationType.INDIGO);
		products[1] = calcActiveProductions(PlantationType.SUGAR);
		products[2] = calcActiveProductions(PlantationType.TOBACCO);
		products[3] = calcActiveProductions(PlantationType.COFFEE);
		
		return products;
	}

	public void activateColonistsDnD() {
		updateDragging();
	}

	public void deactivateColonistsDnD() {
		cancelDragging();
	}
	
	public boolean hasActiveBuilding(BuildingTypeList type) {
		for (BuildingField field : buildings)
			if (field.type() == type && field.state() == State.ACTIVE)
				return true;
		return false;
	}
	
	public int distributeColonists() {
		int usedColonists = 0;
		for (BuildingField field : buildings) {
			int empty = field.emptyPlaces();
			if (field.type() != BuildingTypeList.NONE && empty > 0) {
				for (int i = 0; i < empty; i++) {
					field.addColonist();
					usedColonists++;
				}
			}
		}
		return usedColonists;
	}

	public void addColonistToBuilding(BuildingTypeList type) {
		for (BuildingField field : buildings)
			if (field.type() == type && field.state() == State.INACTIVE) {
				field.addColonist();
				return;
			}
	}

	public int victoryPoints() {
		int vp = 0;
		for (BuildingField field : buildings)
			vp += field.type().getVictoryPoints();
		return vp;
	}
	
	public int smallProductionBuildings() {
		int num = 0;
		for (BuildingField field : buildings)
			if (field.type().getType() == BuildingType.SMALL_PRODUCTION) 
			num++;
		return num;
	}

	public int largeProductionBuildings() {
		int num = 0;
		for (BuildingField field : buildings)
			if (field.type().getType() == BuildingType.PRODUCTION) 
				num++;
		return num;
	}
	
	public int numColonists() {
		int num = 0;
		for (BuildingField field : buildings)
			num += field.colonists();
		return num;
		
	}
	
	public int numNonProductionBuildings() {
		int num = 0;
		for (BuildingField field : buildings)
			if (field.type().getType() == BuildingType.BUILDING
				|| field.type().getType() == BuildingType.LARGE_BUILDING)
				num++;
		return num;
	}

	private void updateDragging() {
		for (BuildingField field : buildings) {
			if (field.type() != BuildingTypeList.NONE) {
				if (field.state() == State.ACTIVE)
					addColonistDragTarget(field);
				if (field.emptyPlaces() > 0)
					addColonistDropTarget(field);
			}
		}
	}

	private static void addColonistDropTarget(BuildingField field) {
		field.setOnDragOver(ev -> {
		    if (ev.getGestureSource() != field &&
		            ev.getDragboard().hasString()) {
		        ev.acceptTransferModes(TransferMode.MOVE);
		    }
		    ev.consume();
		});
		field.setOnDragEntered(ev -> {
			// TODO: show drop possible
			ev.consume();
		});
		field.setOnDragExited(ev -> {
			// TODO: end show drop possible
			ev.consume();
		});
		field.setOnDragDropped(ev -> {
		    Dragboard db = ev.getDragboard();
		    boolean success = false;
		    if (db.hasString()) {
		       field.addColonist();
		       success = true;
		    }
		    ev.setDropCompleted(success);
		    ev.consume();
		});
	}

	private void addColonistDragTarget(BuildingField field) {
		field.setOnDragDetected(ev -> {
			Dragboard db = field.startDragAndDrop(TransferMode.MOVE);
			db.setDragView(PrColors.COLONIST.icon());
			ClipboardContent cc = new ClipboardContent();
			cc.putString("1");
			db.setContent(cc);
			ev.consume();
		});
		field.setOnDragDone(ev -> {
			if (ev.isAccepted()) {
				field.removeColonist();
				player.distributeColonists();
				ev.consume();
			}
		});
	}

	private void cancelDragging() {
		for (BuildingField field : buildings) {
			if (field.type() != BuildingTypeList.NONE) {
				field.setOnDragDetected(null);
				field.setOnDragDone(null);
				field.setOnDragEntered(null);
				field.setOnDragExited(null);
				field.setOnDragDropped(null);
			}
		}
	}
	
	private int calcActiveProductions(PlantationType type) {
		int products = 0;
		
		switch (type) {
		case INDIGO: products += lookupActiveProduction(BuildingTypeList.KL_INDIGO);
					products += lookupActiveProduction(BuildingTypeList.GR_INDIGO);
					break;
		case SUGAR: products += lookupActiveProduction(BuildingTypeList.KL_ZUCKER);
					products += lookupActiveProduction(BuildingTypeList.GR_ZUCKER);
					break;
		case TOBACCO: products += lookupActiveProduction(BuildingTypeList.TABAK);
					break;
		case COFFEE: products += lookupActiveProduction(BuildingTypeList.KAFFEE);
					break;
		default: 
		}
		
		return products;
	}
	
	private int lookupActiveProduction(BuildingTypeList type) {
		for (BuildingField field : buildings)
			if (field.type() == type)
				return field.productionPlaces();
		return 0;
	}

	private void initGui() {
		VBox vbox = new VBox(3);
		vbox.setPadding(new Insets(2, 0, 0, 2));
		rowOne = new HBox(3);
		rowTwo = new HBox(3);
		vbox.getChildren().addAll(rowOne, rowTwo);
		this.getChildren().add(vbox);
	}

	private void initBuildings() {
		for (int i = 0; i < 12; i++)
			buildings.add(new BuildingField());
	}
	
	private void update() {
		rowOne.getChildren().clear();
		rowTwo.getChildren().clear();

		int listIndex = 0;
		int placesInRow = 0;
		int endReduced = 0;
		
		for (; placesInRow < 5; listIndex++) {
			BuildingField building = buildings.get(listIndex);
			rowOne.getChildren().add(building);
			placesInRow += building.size();
		}
		
		BuildingField building = buildings.get(listIndex);
		if (placesInRow < 6) {
			if (building.size() < 2) {
				rowOne.getChildren().add(building);
				listIndex++;
			} 
			else if (buildings.get(buildings.size()-1).type().equals(BuildingTypeList.NONE) ) {
				rowOne.getChildren().add(buildings.get(buildings.size()-1));
				endReduced++;
			}
		}
			
		for (; listIndex < buildings.size() - endReduced; listIndex++) {
			rowTwo.getChildren().add(buildings.get(listIndex));
		}
	}

	private void sort() {
		buildings.sort((p1, p2) -> {
			return p1.type().compareTo(p2.type());
		});
		update();
	}
}
