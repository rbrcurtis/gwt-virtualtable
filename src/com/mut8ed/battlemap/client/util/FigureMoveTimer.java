package com.mut8ed.battlemap.client.util;

import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.dto.FigureStruct;
import com.mut8ed.battlemap.client.view.MapView;
import com.mut8ed.battlemap.shared.dto.MapCell;

public class FigureMoveTimer extends Timer {

	private int height;
	private Element elem;
	private List<MapCell> path;
	private int speed;
	private MapCell step;
	private int targetX;
	private int targetY;
	private int width;
	private int x;
	private int y;
	private MapView mapView;
	private FigureStruct figureStruct;
	private boolean updateSelectionRing = false;

	public FigureMoveTimer(FigureStruct figureStruct, List<MapCell> path, int speed, MapView mapView){
		this.figureStruct = figureStruct;
		this.elem = figureStruct.getImage().getElement();
		this.path = path;
		this.speed = speed;
		this.mapView = mapView;
		width = elem.getClientWidth();
		height = elem.getClientHeight();
		x = elem.getOffsetLeft();
		y = elem.getOffsetTop();
		this.step = path.remove(0);
		setNextStep();
	}
	

	@Override
	public void run() {
		if (x!=targetX || y!=targetY){
			if (x<targetX){
				if (x>targetX-(speed-1))x++;
				else x+=speed;
			}
			else if (x>targetX){
				if (x<targetX+(speed-1))x--;
				else x-=speed;
			}
			if (y<targetY){
				if (y>targetY-(speed-1))y++;
				else y+=speed;
			}
			else if (y>targetY){
				if (y<targetY+(speed-1))y--;
				else y-=speed;
			}
			DOM.setStyleAttribute(elem, "left", x+"px");
			DOM.setStyleAttribute(elem, "top", y+"px");
			if (updateSelectionRing)MapView.getInstance().setSelectionRingPosition();
		} else {
			setNextStep();
		}
	}

	private void setNextStep() {
		if (path.size()==0){
			this.cancel();
			//BattleMap.debug("movement complete");
			mapView.figureMoving = false;
			
			String stance = figureStruct.getStance();
			int rotation = 0;
			if (stance.equals("SW"))rotation = 0;
			else if (stance.equals("W"))rotation = 45;
			else if (stance.equals("NW"))rotation = 90;
			else if (stance.equals("N"))rotation = 135;
			else if (stance.equals("NE"))rotation = 180;
			else if (stance.equals("E"))rotation = 225;
			else if (stance.equals("SE"))rotation = 270;
			else if (stance.equals("S"))rotation = 315;
			
			mapView.setRotation(figureStruct.getFigureId(), rotation);
			
			return;
			
		} else {
			MapCell nextStep = path.remove(0);
			
			int deltaX = nextStep.col - step.col;
			int deltaY = nextStep.row - step.row;
			
			String stance = null;
			if (deltaX==0 && deltaY==0)stance = "S";
			else if (deltaX<0 && deltaY<0)stance = "NW";
			else if (deltaX==0 && deltaY<0)stance = "N";
			else if (deltaX>0 && deltaY<0)stance = "NE";
			else if (deltaX>0 && deltaY==0)stance = "E";
			else if (deltaX>0 && deltaY>0)stance = "SE";
			else if (deltaX==0 && deltaY>0)stance = "S";
			else if (deltaX<0 && deltaY>0)stance = "SW";
			else if (deltaX<0 && deltaY==0)stance = "W";
			else {
				//BattleMap.debug("can't find correct facing for "+step+" to "+nextStep, new Throwable());
				stance = "S";
			}
			figureStruct.setStance(stance);
			
			this.step = nextStep;
			int cellSize = mapView.getCellSize();
			targetX = step.col*cellSize+cellSize/2-width/2;
			targetY = step.row*cellSize+cellSize/2-height+cellSize/5;
		}
	}

	public void updateSelectionRing(boolean doEeet) {
		//BattleMap.debug("update selectionRing? "+doEeet);
		this.updateSelectionRing = doEeet;
	}


};

