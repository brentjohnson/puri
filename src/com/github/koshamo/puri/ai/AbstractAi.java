package com.github.koshamo.puri.ai;

import java.util.List;
import java.util.Optional;

import com.github.koshamo.puri.GameController;
import com.github.koshamo.puri.setup.BuildingTypeList;
import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.ui.controls.board.Board;
import com.github.koshamo.puri.ui.controls.player.Player;
import com.github.koshamo.puri.ui.controls.role.RoleBoard;
import com.github.koshamo.puri.ui.controls.role.RoleCard;

import javafx.scene.control.ButtonType;

public abstract class AbstractAi {

	protected final List<Player> players;
	protected final Board gameBoard;
	protected final RoleBoard roleBoard;
	protected GameController controller;
	
	public AbstractAi(List<Player> players, 
			Board gameBoard, RoleBoard roleBoard) {
		this.players = players;
		this.gameBoard = gameBoard;
		this.roleBoard = roleBoard;
	}
	
	public void connectController(GameController controller) {
		this.controller = controller;
	}
	
	/**
	 * call
	 * propagateRole(card)
	 * as last action
	 */
	public abstract void chooseRole();
	
	protected final void propagateRole(RoleCard card) {
		controller.chooseRole(card.type(), card.removeGuldenAndDeactivateCard());
	}

	public abstract Optional<BuildingTypeList> purchaseBuilding();

	public abstract Optional<ButtonType> useHazienda();

	/**
	 * call
	 * propagatePlantation(type)
	 * as last action
	 */
	public abstract void choosePlantation();

	protected final void propagatePlantation(PlantationType type) {
		gameBoard.plantations().selectPlantation(type);
	}

	/**
	 * call
	 * propagateColonistDistribution()
	 * as last action
	 */
	public abstract void distributeColonists();
	
	protected final void propagateColonistDistribution() {
		controller.gouvernorDone();
	}

	public abstract Optional<List<PlantationType>> chooseProductionExtra(int[] possibleExtras);

	/**
	 * call
	 * propagateShipping()
	 * as last action
	 */
	public abstract void shipProduct();
	
	protected final void propagateShipping() {
		controller.shippingDone();
	}

	/**
	 * call
	 * propagateTrading()
	 * as last action
	 */
	public abstract void trade();

	protected final void propagateTrading() {
		controller.handleTraderDone();
	}
}
