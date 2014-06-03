package com.mut8ed.battlemap.client.util;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.view.MapView;
import com.mut8ed.battlemap.client.widget.ToolPanel;
import com.mut8ed.battlemap.client.widget.editor.EditorPanel;
import com.mut8ed.battlemap.shared.Defaults;
import com.mut8ed.battlemap.shared.MapObjectType;
import com.mut8ed.battlemap.shared.dto.MapObjectWrapper;

public class MapMouseHandler implements MouseDownHandler, MouseMoveHandler, MouseUpHandler, ClickHandler, MouseWheelHandler {

	private int cellSize;
	private MapView mapView;
	private boolean mouseDown = false;
	private boolean mouseWheelStarted = false;
	private boolean moved = false;
	private MultiToolMouseHandler multiHandler;
	private RotateToolMouseHandler rotateHandler;
	private Image selectedImage;
	private MapObjectWrapper selectedObject;
	private SelectToolMouseHandler selectHandler;
	private int startX = -1;
	private int startY = -1;
	Timer resizeTimer = null;
	FlowPanel zoomSquare;
	public MapMouseHandler(MapView mapView){
		this.mapView = mapView;
		cellSize = mapView.getCellSize();
		this.selectHandler = new SelectToolMouseHandler();
		this.multiHandler = new MultiToolMouseHandler();
		this.rotateHandler = new RotateToolMouseHandler();
	}
	public boolean isDragging() {
		return mouseDown;
	}
	@Override
	public void onClick(ClickEvent event) {
		//		////BattleMap.debug("click!");
		//		if (toolCanvas!=null){
		//			toolCanvas.removeFromParent();
		//			toolCanvas = null;
		//		}
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {


		//		////BattleMap.debug("MapMousehandler:mouse down");
		if (event.getNativeButton()==NativeEvent.BUTTON_RIGHT){
			return;
		}

		mouseDown = true;

		switch (ToolPanel.selectedTool){
		case SELECT:
			selectHandler.onMouseDown(event);
			break;
		case MULTI:
			multiHandler.onMouseDown(event);
			break;
		case ROTATE:
			rotateHandler.onMouseDown(event);
			break;
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {


		if (mouseDown){
			moved = true;

			event.preventDefault();
			event.stopPropagation();

			switch (ToolPanel.selectedTool){
			case SELECT:
				selectHandler.onMouseMove(event);
				break;
			case MULTI:
				multiHandler.onMouseMove(event);
				break;
			case ROTATE:
				rotateHandler.onMouseMove(event);
				break;
			}
		}
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {

		//		////BattleMap.debug("MapMouseHandler:mouse up");


		switch (ToolPanel.selectedTool){
		case ROTATE:
			rotateHandler.onMouseUp(event);
			break;
		case SELECT:
			selectHandler.onMouseUp(event);
			break;
		case MULTI:
			multiHandler.onMouseUp(event);
			break;
		}

		mouseDown = false;
		moved = false;
		startX = -1;
		startY = -1;
		selectedObject = null;
		selectedImage = null;


	}

	@Override
	public void onMouseWheel(final MouseWheelEvent event) {

		////BattleMap.debug("mouse wheel at "+event.getClientX()+"x"+event.getClientY());

		if (mapView.figureMoving){
			BattleMap.setMessage("zoom is not allowed while characters move.");
			return;
		}

		if (!mouseWheelStarted){
			mouseWheelStarted = true;
			zoomSquare = new FlowPanel();
			zoomSquare.getElement().setId("zoomSquare");
			mapView.addToMap(zoomSquare);
			resizeTimer = new Timer() {

				int clientX = event.getClientX();
				int clientY = event.getClientY();
				int x = event.getX();
				int y = event.getY();

				@Override
				public void run() {
					zoomSquare.removeFromParent();
					int col = (x)/mapView.getCellSize();
					int row = (y)/mapView.getCellSize();
					////BattleMap.debug("zoom:"+col+"x"+row);
					mapView.setMapSize(cellSize);
					mapView.setPosition(
							clientX-cellSize*col-cellSize/2,
							clientY-cellSize*row-cellSize/2
							);
					mouseWheelStarted = false;
				}
			};

		}

		int delta = -event.getDeltaY();
		if (delta>0){
			cellSize+=3;
		} else if (delta<0){
			if (cellSize<=20)return;
			cellSize-=3;
		} else return;

		DOM.setStyleAttribute(zoomSquare.getElement(), "left", (event.getX()-(cellSize/2))+"px");
		DOM.setStyleAttribute(zoomSquare.getElement(), "top", (event.getY()-(cellSize/2))+"px");
		zoomSquare.setHeight(cellSize+"px");
		zoomSquare.setWidth(cellSize+"px");
		resizeTimer.cancel();
		resizeTimer.schedule(250);


	}

	/**
	 * MULTI TOOL HANDLER
	 */
	private class MultiToolMouseHandler {

		private GWTCanvas toolCanvas;

		public void onMouseDown(MouseDownEvent event) {

			startX = event.getRelativeX(mapView.mapTable.getElement());
			startY = event.getRelativeY(mapView.mapTable.getElement());

			////BattleMap.debug("creating toolCanvas");
			toolCanvas = new GWTCanvas(
					mapView.getCellCountX()*mapView.getCellSize(),
					mapView.getCellCountY()*mapView.getCellSize()
					);
			toolCanvas.getElement().setId("toolCanvas");
			toolCanvas.setLineWidth(2);
			toolCanvas.setStrokeStyle(Color.RED);
			mapView.mainPanel.add(toolCanvas);
		}

		public void onMouseMove(MouseMoveEvent event) {
			
			int x = event.getRelativeX(mapView.mapTable.getElement());
			int y = event.getRelativeY(mapView.mapTable.getElement());
			
			if (toolCanvas==null || !toolCanvas.isAttached()){
				////BattleMap.debug("tool canvas is null or not attached, cant draw the box", new Throwable());
				//				mouseDown = false;
				//				moved = false;
				return;
			}
			toolCanvas.clear();
			toolCanvas.strokeRect(startX, startY, x-startX, y-startY);
		}

		public void onMouseUp(MouseUpEvent event) {

			int x = event.getRelativeX(mapView.mapTable.getElement());
			int y = event.getRelativeY(mapView.mapTable.getElement());

			if (toolCanvas!=null && toolCanvas.isAttached()){
				toolCanvas.removeFromParent();
				toolCanvas = null;

				if (startX>x){
					int t = x;
					x = startX;
					startX = t;
				}
				if (startY>y){
					int t = y;
					y = startY;
					startY = t;
				}
				if (EditorPanel.isOpen())EditorPanel.dropMulti(startX,x,startY,y);
				else mapView.deleteMulti(startX,x,startY,y);
			} else {
				////BattleMap.debug("toolCanvas="+toolCanvas, new Throwable());
			}

			if (toolCanvas!=null){
				toolCanvas.removeFromParent();
				toolCanvas = null;
			}

		}
	}

	/**
	 * ROTATE TOOL HANDLER
	 */
	private class RotateToolMouseHandler {

		private Integer rotation = null;

		public void onMouseDown(MouseDownEvent event) {

			startX = event.getX();
			startY = event.getY();

			String selected = mapView.getSelected();
			if (selected==null)return;
			selectedObject = mapView.getMapObject(selected);
			selectedImage = mapView.getMapImage(selected);
			rotation = selectedObject.getRotation(); 
			if (selectedObject.getMapObjectType().equals(MapObjectType.DECAL)){
				startX = selectedObject.getX();
				startY = selectedObject.getY();
				if (mapView.getCellSize()!=Defaults.CELLSIZE){
					startX = (int)(startX*Defaults.CELLSIZE/mapView.getCellSize());
					startY = (int)(startY*Defaults.CELLSIZE/mapView.getCellSize());
				}
			} else {
				int cellSize = mapView.getCellSize();
				//				////BattleMap.debug("cellsize "+cellSize+", so "+selectedObject);
				startX = (selectedObject.getX()*cellSize)+(cellSize/2);
				startY = (selectedObject.getY()*cellSize)+(cellSize/2);
			}
		}


		public void onMouseMove(MouseMoveEvent event) {

			if (selectedObject!=null){

				int posX = event.getX();
				int posY = event.getY();


				////BattleMap.debug(posX+"x"+posY+" vs "+startX+"x"+startY);
				int deltaX = posX-startX;
				int deltaY = posY-startY;
				//				////BattleMap.debug("delta "+deltaX+"x"+deltaY);
				double hypotenuse = Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
				//				////BattleMap.debug("hypotenuse "+hypotenuse);
				int degrees = (int)(Math.asin(deltaY/hypotenuse)*57.2957795);//convert radians to degrees
				degrees = Math.abs(degrees);

				if (deltaX>=0 && deltaY<=0)rotation = 360-degrees; //quadrant 1
				else if (deltaX<=0 && deltaY<=0)rotation = 180+degrees; //quadrant 2
				else if (deltaX<=0 && deltaY>=0)rotation = 180-degrees; //quadrant 3
				else if (deltaX>=0 && deltaY>=0)rotation = degrees; //quadrant 4
				//				////BattleMap.debug("rotation! "+x+"x"+y+":"+degrees+"/"+rotation);
				mapView.showRotation(selectedImage.getElement(), rotation);
			}
		}

		public void onMouseUp(MouseUpEvent event) {
			if (moved && selectedObject!=null){
				class TempAsyncCallback<T> extends MTAsyncCallback<T>{

					private Element elem;
					private int rotation;

					public TempAsyncCallback(Element elem, int rotation, String msg){
						super(msg);
						this.elem = elem;
						this.rotation = rotation;
					}

					@Override
					public void onFailure(Throwable caught) {
						////BattleMap.debug("rotation failure:"+selectedObject);
						super.onFailure(caught);


						MapView.getInstance().showRotation(elem, rotation);
					}


				}

				BattleMap.eventBus.rotateObject(selectedObject.getElementId(), rotation, new TempAsyncCallback<Void>(selectedImage.getElement(), rotation, "Failed to Move "+selectedObject.getMapObject().getElementId()));
			} else if (!moved && !EditorPanel.isOpen()){

				//select the clicked object if no movement has occurred.
				////BattleMap.debug("mouseup registered at "+x+" x "+y);
				int x = event.getRelativeX(mapView.mapTable.getElement());
				int y = event.getRelativeY(mapView.mapTable.getElement());
				mapView.registerClick(x, y);
			}
		}
	}

	/**
	 * SELECT TOOL HANDLER
	 */
	private class SelectToolMouseHandler{

		public void onMouseDown(MouseDownEvent event) {

			startX = event.getX();
			startY = event.getY();

			String selected = mapView.getSelected();
			if (selected!=null){
				////BattleMap.debug("thinkin hard about draggin "+selected);
			}
			if (selected!=null && selected.contains("DECAL")){
				selectedObject = mapView.getMapObject(selected);
				selectedImage = mapView.getMapImage(selected);
				startX = selectedObject.getX();
				startY = selectedObject.getY();
				////BattleMap.debug("dragging "+selected);
				event.preventDefault();
				event.stopPropagation();
			}
		}

		public void onMouseMove(MouseMoveEvent event) {
			if (selectedObject!=null && selectedImage!=null){

				int x = event.getRelativeX(mapView.mapTable.getElement());
				int y = event.getRelativeY(mapView.mapTable.getElement());

				DOM.setStyleAttribute(selectedImage.getElement(), "left", (x-selectedImage.getWidth()/2)+"px");
				DOM.setStyleAttribute(selectedImage.getElement(), "top", (y-selectedImage.getHeight()/2)+"px");
				mapView.setSelectionRingPosition();

			} else {
				mapView.setPosition(
						event.getX() - startX + mapView.getPositionX(),
						event.getY() - startY + mapView.getPositionY()
						);
			}
		}

		public void onMouseUp(MouseUpEvent event) {

			int x = event.getRelativeX(mapView.mapTable.getElement());
			int y = event.getRelativeY(mapView.mapTable.getElement());


			if (moved && selectedObject!=null && selectedImage!=null){

				class TempAsyncCallback<T> extends MTAsyncCallback<T>{

					private MapObjectWrapper selectedObject;
					private int startX;
					private int startY;

					public TempAsyncCallback(MapObjectWrapper selectedObject, int startX, int startY, String msg){
						super(msg);
						this.selectedObject = selectedObject;
						this.startX = startX;
						this.startY = startY;

					}

					@Override
					public void onFailure(Throwable caught) {
						////BattleMap.debug("decal move failure:"+selectedObject+"/"+startX+"x"+startY);
						Image selectedImage = MapView.getInstance().getMapImage(selectedObject.getElementId());
						super.onFailure(caught);

						int x = startX;
						int y = startY;
						if (mapView.getCellSize()!=Defaults.CELLSIZE){
							x = (int)(x*mapView.getCellSize()/Defaults.CELLSIZE);
							y = (int)(y*mapView.getCellSize()/Defaults.CELLSIZE);
						}

						selectedObject.setX(startX);
						selectedObject.setY(startY);
						DOM.setStyleAttribute(selectedImage.getElement(), "left", (x-selectedImage.getWidth()/2)+"px");
						DOM.setStyleAttribute(selectedImage.getElement(), "top", (y-selectedImage.getHeight()/2)+"px");
						MapView.getInstance().setSelectionRingPosition();
					}


				}

				if (mapView.getCellSize()!=Defaults.CELLSIZE){
					double multiplier = (Defaults.CELLSIZE*1.0/mapView.getCellSize());
					x*=multiplier;
					y*=multiplier;
					//BattleMap.debug("adjusting position for zoom, multiplier "+multiplier);
				}
				
				selectedObject.setX(x);
				selectedObject.setY(y);
				BattleMap.eventBus.moveDecal(selectedObject, new TempAsyncCallback<Void>(selectedObject, startX, startY, "Failed to Move "+selectedObject.getMapObject().getElementId()));
			} else if (!moved && !EditorPanel.isOpen()){

				//select the clicked object if no movement has occurred.
				////BattleMap.debug("mouseup registered at "+x+" x "+y);
				mapView.registerClick(x, y);
			}


		}
	}
}
