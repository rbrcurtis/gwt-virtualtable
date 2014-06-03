package com.mut8ed.battlemap.shared.event;

import java.util.List;

import com.mut8ed.battlemap.shared.dto.MapCell;

public class MoveFigureEvent extends MapEvent {

	private String figureId;
	private List<MapCell> path;

	public MoveFigureEvent(){}
	
	public MoveFigureEvent(String figureId, List<MapCell> path) {
		this.figureId = figureId;
		this.path = path;
	}

	public String getFigureId() {
		return figureId;
	}

	public void setFigureId(String figureId) {
		this.figureId = figureId;
	}

	public List<MapCell> getPath() {
		return path;
	}

	public void setPath(List<MapCell> path) {
		this.path = path;
	}
	
}
