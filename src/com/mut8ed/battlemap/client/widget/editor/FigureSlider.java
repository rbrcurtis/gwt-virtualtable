package com.mut8ed.battlemap.client.widget.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.dto.FigureStruct;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.client.widget.SizePanel;
import com.mut8ed.battlemap.client.widget.TagPanel;
import com.mut8ed.battlemap.shared.dto.Dimensions;
import com.mut8ed.battlemap.shared.dto.Figure;
import com.mut8ed.battlemap.shared.dto.MapObject;

/**
 * dropdown sliding menu for figures.  this only exists as part of the MapMenu
 * which only exists as part of the mapview.
 * @author ryan
 *
 */
public class FigureSlider extends EditorPanel {


	private static FigureSlider instance = null;
	private Map<String,FigureStruct> allFigures = new HashMap<String,FigureStruct>();
	private VerticalPanel categories = new VerticalPanel();
	private Label closeButton;
	private List<FigureStruct> figureList = new ArrayList<FigureStruct>();
	private SizePanel sizePanel;
	private double targetHeight = 100.0;
	private FlowPanel figurePanel = new FlowPanel();
	private AbsolutePanel scrollDiv;
	private int height;
	private int nextPage = 1;
	private boolean loadLock = false;
	private FigureClickHandler defaultFigureHandler;
	private static FigureClickHandler figureHandler;
	
	public interface FigureClickHandler {
		
		public void figureClicked(Image image, Figure figure);
		
	}
	
	private FigureSlider(){
		super();

		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		defaultFigureHandler = new FigureClickHandler() {
			
			@Override
			public void figureClicked(Image image, Figure figure) {
				if (figure!=null){
					EditorPanel.setSelected(image.getElement(), figure, sizePanel.getDimensions());
					sizePanel.setDisplayedWidth(figure.getWidth());
					sizePanel.setDisplayedHeight(figure.getHeight());
				}
			}
		}; 

		figurePanel.getElement().setId("figurePanel");
		scrollDiv = new AbsolutePanel();
		scrollDiv.setStylePrimaryName("scrollDiv");
		scrollDiv.add(figurePanel);
		
		

		getElement().setId("figureSlider");
		closeButton = new Label("X");
		closeButton.setStylePrimaryName("sliderCloseButton");
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				EditorPanel.toggle(null);
				figureHandler = null; //closing will remove the custom handler.
			}
		});

		//good to wait a smidge before doing an rpc call as an immediate rpc call will cause
		//some browsers to continue to show loading.
//		new Timer(){
//
//			@Override
//			public void run() {
//				loadFigures();
//			}
//
//		}.schedule(250);
		
		categories = new TagPanel(this);
		
		sizePanel = new SizePanel();
		
		add(closeButton);
		setCellVerticalAlignment(closeButton, HasVerticalAlignment.ALIGN_BOTTOM);
		add(categories);
		add(sizePanel);
		add(scrollDiv);
		
		Window.addResizeHandler(new ResizeHandler() {
			
			@Override
			public void onResize(ResizeEvent event) {
				setWidthAndHeight();
			}
		});
		
		
		figurePanel.addDomHandler(new ScrollHandler() {
			
			@Override
			public void onScroll(ScrollEvent event) {
				
				double scrollTop = figurePanel.getElement().getScrollTop()+height;
				double scrollHeight = figurePanel.getElement().getScrollHeight();
				if (!loadLock  && scrollHeight - scrollTop < 600){
					loadFigures(nextPage++);
				}
			}
		}, ScrollEvent.getType());
	}

	public static FigureSlider getInstance(){
		if (instance==null){
			instance = new FigureSlider();
		}
		return instance;
	}
	
	public static FigureSlider getInstance(FigureClickHandler handler){
		figureHandler = handler;
		return getInstance();
	}

	public void loadFigures(int page){
		loadLock = true;
		
		//BattleMap.debug("load figures "+page);
		
		BattleMap.eventBus.getFigureList(page, new MTAsyncCallback<List<Figure>>() {


			@Override
			public void onSuccess(List<Figure> result) {
				GWT.log("received figure list");

				//allFigures tracks the full list
				//figureList is trimmed based on category
				for (Figure figure : result){
					setupFigure(figure);
				}
				displayFigures();
			}

		});
	}
	
	private void setupFigure(Figure figure) {
		FigureStruct struct = new FigureStruct(figure);
		final Image image = figure.getStance("SW");
		image.setVisible(false);
		image.setTitle(figure.getName()+": "+figure.getTags().toString());
		image.addLoadHandler(new LoadHandler() {
			
			@Override
			public void onLoad(LoadEvent event) {
				int height = image.getHeight();
				int width = image.getWidth();
				image.setHeight(((int)targetHeight )+"px");
				double perc = targetHeight/height;
				width = (int) (width*perc);
				image.setWidth(width+"px");
				image.setVisible(true);
			}
		});
		image.setStylePrimaryName("selectFigureListImage");
		
		struct.setImage(image);
		
		image.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				selectFigure(image);
			}
		});
		
		
		allFigures.put(figure.getElementId(),struct);
		figureList.add(struct);
	}

	
	public void setWidthAndHeight(){
		int width = Window.getClientWidth()/3;
		scrollDiv.setWidth(width+"px");
		Element elem = DOM.getElementById("alerts");
		int alertsHeight = (elem!=null) ? elem.getOffsetHeight() : 0;
		elem = DOM.getElementById("mapMenu");
		int menuHeight = (elem!=null) ? elem.getOffsetHeight() : 0;
		height = Window.getClientHeight()-closeButton.getOffsetHeight()-categories.getOffsetHeight()-alertsHeight-menuHeight-sizePanel.getOffsetHeight();
		scrollDiv.setHeight(height+"px");
		DOM.setStyleAttribute(this.getElement(), "top", menuHeight+"px");
	}

	private void selectFigure(Image image) {
		Figure figure = null;
		for (FigureStruct fs : this.figureList){
			if (fs.getFigure().getStanceUrl("SW").equals(image.getUrl())){
				figure = fs.getFigure();
				break;
			}
		}
		if (figureHandler!=null)figureHandler.figureClicked(image, figure);
		else defaultFigureHandler.figureClicked(image, figure);
	}

	private void displayFigures() {

		figurePanel.clear();
		FlowPanel shim = new FlowPanel();
		shim.setWidth("100px");
		figurePanel.add(shim);
		
		for (int i = 0; i < figureList.size() ; i++){
			final FigureStruct struct = figureList.get(i);
			Image image = struct.getImage();
			if (i==figureList.size()-1){
				image.addLoadHandler(new LoadHandler() {
					
					@Override
					public void onLoad(LoadEvent event) {
						loadLock = false;
					}
				});
			}
			figurePanel.add(image);
		}

		shim = new FlowPanel();
		shim.setWidth("100px");
		figurePanel.add(shim);
		figurePanel.add(shim);
	}

	@Override
	public void resetList() {
		figureList.clear();
		allFigures.clear();
		load();
	}

	@Override
	public void searchByTag(List<MapObject> result) {
		figureList.clear();
		for (MapObject figure : result){
			setupFigure((Figure)figure);
		}
		displayFigures();
	}

	@Override
	public MapObject getMapObjectById(String id) {
		try {
			return allFigures.get(id).getFigure();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Dimensions getDimensions() {
		return sizePanel.getDimensions();
	}

	@Override
	public void load() {
		if (allFigures.size()>0)return;
		loadFigures(0);		
	}

	public FigureClickHandler getFigureHandler() {
		return figureHandler;
	}

	public void setFigureHandler(FigureClickHandler figureHandler) {
		FigureSlider.figureHandler = figureHandler;
	}

}
