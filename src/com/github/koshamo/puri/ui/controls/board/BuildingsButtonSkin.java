package com.github.koshamo.puri.ui.controls.board;


import com.github.koshamo.puri.setup.PrColors;

import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/*private*/ class BuildingsButtonSkin extends SkinBase<BuildingsButton> {

	public BuildingsButtonSkin(BuildingsButton control) {
		super(control);
		drawComponent();
	}

	public void drawComponent() {
		this.getChildren().clear();
		StackPane stack = new StackPane();

		drawButton(stack);
		
		this.getChildren().add(stack);
	}

	private static void drawButton(StackPane stack) {
		Rectangle border = new Rectangle(102, 52, Color.BLACK);
		Rectangle rect = new Rectangle(100, 50, PrColors.BUILDING.getColor());
		Text text = new Text("anschauen");
		stack.getChildren().addAll(border, rect, text);
	}
}
