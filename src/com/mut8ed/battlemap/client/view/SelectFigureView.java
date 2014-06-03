package com.mut8ed.battlemap.client.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.Page;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.shared.dto.Figure;

public class SelectFigureView extends Composite {

	double targetHeight=100.0;
	private List<Figure> figureList = new ArrayList<Figure>();
	private List<Figure> allFigures;
	private HorizontalPanel categories = new HorizontalPanel();
	private final FlowPanel body = new FlowPanel();
	
	public SelectFigureView(){
		
		categories.getElement().setId("figureCategoryList");
		categories.getElement().setAttribute("align", "center");
		categories.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		body.getElement().setId("selectFigureViewList");

		BattleMap.eventBus.getFigureList(0, new MTAsyncCallback<List<Figure>>() {

			@Override
			public void onSuccess(List<Figure> result) {

				//allFigures tracks the full list
				//figureList is trimmed based on category
				allFigures = result;
				List<String> cats = new ArrayList<String>();
				
				//build figureList and also find categories
				for (Figure figure : allFigures){
					figureList.add(figure);
					for (String tag : figure.getTags()){
						if (!cats.contains(tag))cats.add(tag);
					}
				}

				for (final String category : cats){
					Label cat = new Label(category);
					cat.setStylePrimaryName("selectFigureCategory");
					cat.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							trimFigures(category);
						}

						
					});
					
					categories.add(cat);
				}

				BattleMap.setMessage(cats.toString());
				displayFigures();
			}
		});
		
		this.initWidget(body);
	}

	private void trimFigures(String category) {
		figureList.clear();
		for (Figure figure : allFigures){
			if (figure.hasTag(category))figureList.add(figure);
		}
		displayFigures();
	}

	protected void displayFigures() {
		
		body.clear();
		body.add(categories);
		
		for (final Figure fig : figureList){
			
			Image image = fig.getStance("SW");
			int height = image.getHeight();
			int width = image.getWidth();
			image.setHeight(((int)targetHeight)+"px");
			double perc = targetHeight/height;
			width = (int) (width*perc);
			image.setWidth(width+"px");
			image.setStylePrimaryName("selectFigureListImage");
			
			image.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					BattleMap.eventBus.chooseFigure(fig.getId(), new MTAsyncCallback<Void>("there was a problem choosing a figure") {

						@Override
						public void onSuccess(Void result) {
							Page.MAP.redirect();
						}
					});
				}
				
			});
			
			body.add(image);
		
		}
	}
	
	
	
	
}
