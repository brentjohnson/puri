package com.github.koshamo.puri.ui.controls.board;

import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.PrColorUtils;
import com.github.koshamo.puri.setup.PrColors;

import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/*private*/ class GoodsShipSkin extends SkinBase<GoodsShip> {

	private final int places;
	private final int upperRowCnt;
	private final int lowerRowCnt;
	private final int smallShipOffset;
	
	protected GoodsShipSkin(GoodsShip control, int places) {
		super(control);
		this.places = places;
		if (places > 4) {
			upperRowCnt = places / 2;
			lowerRowCnt = places / 2 + places % 2;
			smallShipOffset = 0;
		} else {
			upperRowCnt = 0;
			lowerRowCnt = places;
			smallShipOffset = 25;
		}
		drawComponent(PlantationType.NONE, 0);
	}
	
	public void drawComponent(PlantationType product, int amount) {
		this.getChildren().clear();
		
		Pane pane = new Pane();
		
		drawShip(pane);
		drawText(pane, amount);
		drawGoods(pane, product, amount);
		
		this.getChildren().add(pane);
	}

	private void drawShip(Pane pane) {
		Path path = new Path();

		MoveTo start = new MoveTo(1, 50 - smallShipOffset);
		LineTo line1 = new LineTo(21, 55 - smallShipOffset);
		LineTo line2 = new LineTo(121, 55 - smallShipOffset);
		LineTo line3 = new LineTo(141, 50 - smallShipOffset);
		LineTo line4 = new LineTo(121, 85 - smallShipOffset);
		LineTo line5 = new LineTo(21, 85 - smallShipOffset);
		LineTo line6 = new LineTo(1, 50 - smallShipOffset);

		path.getElements().add(start);
		path.getElements().addAll(line1, line2, line3, line4, line5, line6);
		path.setFill(Color.DARKGOLDENROD);
		pane.getChildren().add(path);
	}
	
	private void drawText(Pane pane, int amount) {
		Text text = new Text(amount + " / " + places);
		text.setX(60);
		text.setY(75 - smallShipOffset);
		pane.getChildren().add(text);
	}
	
	private void drawGoods(Pane pane, PlantationType product, int amount) {
		Color productColor = PrColorUtils.selectGoodsColor(product);
		Color curColor;
		double offset = (40 - upperRowCnt*20.0/2) * (lowerRowCnt-upperRowCnt);
		double distance = 80 / (lowerRowCnt-1);
		int load = 0;
		
		for (int i = 0; i < lowerRowCnt; i++, load++) {
			if (load < amount)
				curColor = productColor;
			else 
				curColor = PrColors.DEFAULT_BGD.getColor();
			Rectangle border = new Rectangle(22, 22, Color.BLACK);
			border.relocate(21+i*distance, 30 - smallShipOffset);
			Rectangle rect = new Rectangle(20, 20, curColor);
			rect.relocate(22+i*distance, 31 - smallShipOffset);
			pane.getChildren().addAll(border, rect);
		}
		for (int i = 0; i < upperRowCnt; i++, load++) {
			if (load < amount)
				curColor = productColor;
			else 
				curColor = PrColors.DEFAULT_BGD.getColor();
			Rectangle border = new Rectangle(22, 22, Color.BLACK);
			border.relocate(21+offset+i*distance, 3);
			Rectangle rect = new Rectangle(20, 20, curColor);
			rect.relocate(22+offset+i*distance, 4);
			pane.getChildren().addAll(border, rect);
		}
	}

}
