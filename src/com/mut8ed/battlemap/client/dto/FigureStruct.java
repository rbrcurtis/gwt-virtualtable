package com.mut8ed.battlemap.client.dto;

import com.google.gwt.user.client.ui.Image;
import com.mut8ed.battlemap.client.util.FigureMoveHandler;
import com.mut8ed.battlemap.shared.dto.Figure;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterBrief;

public class FigureStruct {

	private CharacterBrief characterBrief;
	protected Figure figure;
	protected FigureMoveHandler handler;
	protected Image image;
	protected String stance;
	
	public FigureStruct(){}

	public FigureStruct(CharacterBrief cb) {
		this();
		this.characterBrief = cb;
	}

	public FigureStruct(final Figure figure) {
		this.figure = figure;
		image = figure.getStance("SW");
		stance = "SW";
		image.getElement().setId(figure.getElementId());
		image.setStylePrimaryName("figure");
	}

	public CharacterBrief getCharacterBrief() {
		return characterBrief;
	}

	public String getCharacterId() {
		return characterBrief.id;
	}

	public Figure getFigure() {
		return figure;
	}

	public String getFigureId() {
		return figure.getElementId();
	}

	public FigureMoveHandler getHandler() {
		return handler;
	}

	public Image getImage() {
		return image;
	}

	public String getStance() {
		return stance;
	}

	public void setCharacterBrief(CharacterBrief characterBrief) {
		this.characterBrief = characterBrief;
	}

	public void setFigure(Figure figure) {
		this.figure = figure;
	}

	public void setHandler(FigureMoveHandler handler) {
		this.handler = handler;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setStance(String stance) {
		if (stance==null || stance.equals(this.stance))return;
		String url = figure.getStanceUrl(stance);
		if (url!=null && image!=null){
			this.stance = stance;
			image.setUrl(url);
		}
	}

}
