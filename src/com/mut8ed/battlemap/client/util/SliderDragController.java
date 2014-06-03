package com.mut8ed.battlemap.client.util;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.dto.FigureStruct;
import com.mut8ed.battlemap.client.view.MapView;

public class SliderDragController {
	
	public static class SliderDragHandler implements MouseDownHandler, MouseUpHandler, MouseMoveHandler {

		private Image dragee;
		private Image image;
		private HandlerRegistration mouseMoveHandler;
		private HandlerRegistration mouseUpHandler;

		@Override
		public void onMouseDown(MouseDownEvent event) {
			dragee = new Image(image.getUrl());
			dragee.setSize(image.getWidth()+"px", image.getHeight()+"px");
			dragee.setStylePrimaryName("draggingImage");
//			BattleMap
			DOM.setStyleAttribute(dragee.getElement(), "top", (event.getScreenY()-dragee.getHeight()/2)+"px");
			DOM.setStyleAttribute(dragee.getElement(), "left", (event.getScreenX()-dragee.getWidth()/2)+"px");
			this.mouseMoveHandler = dragee.addMouseMoveHandler(this);
			this.mouseUpHandler = dragee.addMouseUpHandler(this);
			BattleMap.add(dragee);
			dragee.setVisible(true);
		}

		@Override
		public void onMouseMove(MouseMoveEvent event) {
			DOM.setStyleAttribute(dragee.getElement(), "top", event.getClientY()+"px");
			DOM.setStyleAttribute(dragee.getElement(), "left", event.getClientX()+"px");
			if (BattleMap.debug)BattleMap.setMessage("mouse move "+event.getClientX()+"x"+event.getClientY());
		}

		@Override
		public void onMouseUp(MouseUpEvent event) {
			if (dropTarget==null)return;
//			dropTarget.handleDroppedFigure(dragee,event.getClientX(),event.getClientY());
			mouseMoveHandler.removeHandler();
			mouseMoveHandler = null;
			mouseUpHandler.removeHandler();
			mouseUpHandler = null;
			if (BattleMap.debug)BattleMap.setMessage("mouse up");
		}

		public void registerFigure(Image image) {
			this.image = image;
			image.addMouseDownHandler(this);
		}

	}

	private static MapView dropTarget;
	
	
	public static void makeDraggable(FigureStruct figure){

		Image image = figure.getImage();
		SliderDragHandler sliderDragHandler = new SliderDragHandler();
		sliderDragHandler.registerFigure(image);
	
	}
	
	public static void registerDropTarget(MapView mapView) {
		dropTarget = mapView;
	}
}
