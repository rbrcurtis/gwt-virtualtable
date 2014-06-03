package com.mut8ed.battlemap.client.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.dto.FigureStruct;
import com.mut8ed.battlemap.client.view.MapView;
import com.mut8ed.battlemap.client.widget.LayerPanel;
import com.mut8ed.battlemap.shared.dto.MapCell;

public class FigureMoveHandler implements MouseDownHandler, MouseMoveHandler, MouseUpHandler, KeyDownHandler {

	private FigureStruct figureStruct;
	private int col;
	private List<HandlerRegistration> handlerRegistration = new ArrayList<HandlerRegistration>();
	private int lastHighlightedCol = -1;
	private int lastHighlightedRow = -1;
	private MapView mapView;
	private List<MapCell> path = new ArrayList<MapCell>();
	private int row;
	private int mouseDownX;
	private int mouseDownY;

	public FigureMoveHandler(int col, int row, int layer, FigureStruct figureStruct, MapView map){

		this.row = row;
		this.col = col;
		path.add(new MapCell(col, row, layer));
		
		this.figureStruct = figureStruct;
		this.mapView = map;

		figureStruct.setHandler(this);
		
		handlerRegistration.add(mapView.focusPanel.addMouseUpHandler(this));
		handlerRegistration.add(mapView.focusPanel.addMouseDownHandler(this));
		handlerRegistration.add(mapView.focusPanel.addMouseMoveHandler(this));
		handlerRegistration.add(mapView.focusPanel.addKeyDownHandler(this));
		
		mapView.figureMoving = true;
		
		mapView.canvas.clear();
		
		mapView.canvas.beginPath();
		mapView.canvas.moveTo(
				col*mapView.getCellSize() + mapView.getCellSize()/2,
				row*mapView.getCellSize() + mapView.getCellSize()/2
				);

	}

	public FigureStruct getFigureStruct() {
		return figureStruct;
	}

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}

	@Override
	public void onKeyDown(KeyDownEvent event) {
		switch(event.getNativeKeyCode()){

		case KeyCodes.KEY_ENTER:
			//BattleMap.debug("path size "+path.size());
			if (mapView.figureMoving && path.size()==1){
				//BattleMap.debug("cancelled move");
				mapView.figureMoving = false;
				reset();
				break;
			}
			//check to make sure no other figures in this square
			BattleMap.eventBus.getBlockersAt(col, row, LayerPanel.getInstance().getCurrentLayerIndex(), new MTAsyncCallback<List<String>>() {


				@Override
				public void onSuccess(List<String> blockers) {
					
					if (blockers!=null) {
						for (String blocker : blockers) {
							if (blocker.contains("FIGURE")
									&& !blocker.equals(figureStruct.getFigureId())) {
								BattleMap.setMessage("Cannot end movement in an occupied square");
								reset();
								return;
							}
						}
					}
					BattleMap.eventBus.moveFigure(figureStruct.getFigureId(), path, new MTAsyncCallback<Void>()); //TODO handle failure
					reset();
				}
				
			});
			break;
			
		case KeyCodes.KEY_ESCAPE:
			//BattleMap.debug("cancelled move");
			mapView.figureMoving = false;
			reset();
			break;

		default: //ignore
			return;
		}
		
		event.stopPropagation();
		event.preventDefault();
	}


	@Override
	public void onMouseUp(MouseUpEvent event) {

		try {
			if (Math.abs(event.getX()-mouseDownX)>5 || Math.abs(event.getY()-mouseDownY)>5){
				return;
			}
		} finally {
			mouseDownX = mouseDownY = -1;
		}
		
		int x = event.getRelativeX(mapView.mapTable.getElement());
		int y = event.getRelativeY(mapView.mapTable.getElement());
		int col = x/mapView.getCellSize();
		int row = y/mapView.getCellSize();

		//BattleMap.debug("FMH:selected cell xy="+x+"x"+y+", square "+col+":"+row);

		if (isValidMove(row,col)){
			mapView.canvas.lineTo(
					col*mapView.getCellSize() + mapView.getCellSize()/2,
					row*mapView.getCellSize() + mapView.getCellSize()/2
					);
			mapView.canvas.stroke();
			path.add(new MapCell(col, row, mapView.getCurrentLayerIndex()));
			this.row = row;
			this.col = col;
		}
	}			


	@Override
	public void onMouseMove(MouseMoveEvent event) {
		int x = event.getRelativeX(mapView.mapTable.getElement());
		int y = event.getRelativeY(mapView.mapTable.getElement());
		int row = y/mapView.getCellSize();
		int col = x/mapView.getCellSize();
		if ((row!=lastHighlightedRow || col!=lastHighlightedCol) && isValidMove(row,col)){
			hightlightCell(row,col);
		}
	}


	public void setCharacter(FigureStruct figureImage) {
		this.figureStruct = figureImage;
	}


	public void setCol(int col) {
		this.col = col;
	}


	public void setRow(int row) {
		this.row = row;
	}


	private void hightlightCell(int row, int col) {
		CellFormatter cellFormatter = mapView.mapTable.getCellFormatter();
		cellFormatter.addStyleName(row, col, "selectedCell");
		//clear the previously highlighted cell
		if (lastHighlightedRow!=-1 && lastHighlightedCol!=-1){
			//unless its a path point.
			boolean clear = true;
			for (MapCell cell : path){
				if (cell.row == lastHighlightedRow && cell.col == lastHighlightedCol){
					clear = false;
					break;
				}
			}
			if (clear)cellFormatter.removeStyleName(lastHighlightedRow, lastHighlightedCol, "selectedCell");
		}
		lastHighlightedRow = row;
		lastHighlightedCol = col;
	}


	private boolean isValidMove(int row, int col) {
		if (
				this.row == row ||
				this.col == col ||
				Math.abs(this.row-row) == Math.abs(this.col-col) //diagonal
				){
			return true;
		}
		return false;
	}


	private void reset() {
		mapView.canvas.closePath();
		//remove highlight for all cells but the last.
		while (!path.isEmpty()){
			MapCell cell = path.remove(0);
			mapView.mapTable.getCellFormatter().removeStyleName(cell.row, cell.col, "selectedCell");
		}

		while (!handlerRegistration.isEmpty()){
			HandlerRegistration hr = handlerRegistration.remove(0);
			//BattleMap.debug("removing "+hr);
			hr.removeHandler();
		}
		figureStruct.setHandler(null);
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		mouseDownX = event.getX();
		mouseDownY = event.getY();
	}

}
