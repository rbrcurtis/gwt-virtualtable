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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.client.widget.ContextMenu;
import com.mut8ed.battlemap.client.widget.SizePanel;
import com.mut8ed.battlemap.client.widget.SliderScrollPanel;
import com.mut8ed.battlemap.client.widget.TagPanel;
import com.mut8ed.battlemap.shared.dto.Dimensions;
import com.mut8ed.battlemap.shared.dto.MapObject;
import com.mut8ed.battlemap.shared.dto.Tile;
import com.mut8ed.battlemap.shared.exception.InvalidInstantiationException;

/**
 * dropdown sliding menu for tiles.  this only exists as part of the MapMenu
 * which only exists as part of the mapview.
 * @author ryan
 *
 */
public class TileSlider extends EditorPanel {


	private Map<String,Tile> allTiles = new HashMap<String,Tile>();
	private List<Tile> tileList = new ArrayList<Tile>();
	private VerticalPanel categories = new VerticalPanel();
	private Label closeButton;
	//	private double targetHeight = 100.0;
	SliderScrollPanel scrollDiv;
	FlowPanel tilePanel = new FlowPanel();
	private SizePanel sizePanel;
	protected boolean loadLock = false;
	protected int height;
	private static TileSlider instance = null;

	public static TileSlider getInstance(){
		if (instance==null){
			instance = new TileSlider(null);
		}
		return instance;
	}

	public TileSlider() throws InvalidInstantiationException {
		throw new InvalidInstantiationException();
	}

	private TileSlider(String v){
		super();
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		tilePanel.getElement().setId("tilePanel");
		scrollDiv = new SliderScrollPanel(this);
		scrollDiv.setStylePrimaryName("scrollDiv");
		scrollDiv.add(tilePanel);



		getElement().setId("tileSlider");
		closeButton = new Label("X");
		closeButton.setStylePrimaryName("sliderCloseButton");
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				EditorPanel.toggle(null);
			}
		});

		//good to wait a smidge before doing an rpc call as an immediate rpc call will cause
		//some browsers to continue to show loading.
//		new Timer(){
//
//			@Override
//			public void run() {
//				loadTiles();
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
		
		tilePanel.addDomHandler(new ScrollHandler() {
			
			private int nextPage;

			@Override
			public void onScroll(ScrollEvent event) {
				
				double scrollTop = tilePanel.getElement().getScrollTop()+height;
				double scrollHeight = tilePanel.getElement().getScrollHeight();
				if (!loadLock  && scrollHeight - scrollTop < 600){
					loadTiles(nextPage++);
				}
			}
		}, ScrollEvent.getType());

	}

	public void setWidthAndHeight(){
		int width = Window.getClientWidth()/3;
		scrollDiv.setWidth(width+"px");
		int alertsHeight = DOM.getElementById("alerts").getOffsetHeight();
		height = Window.getClientHeight()-closeButton.getOffsetHeight()-categories.getOffsetHeight()-alertsHeight-sizePanel.getOffsetHeight();
		scrollDiv.setHeight(height+"px");
	}

	public void loadTiles(int page){
		loadLock = true;
		BattleMap.eventBus.getTileList(page, new MTAsyncCallback<List<Tile>>() {

			@Override
			public void onSuccess(List<Tile> result) {

				GWT.log("got "+result.size()+" tiles from server");
				//allTiles tracks the full list
				//tileList is trimmed based on tag
				for (Tile tile : result){
					allTiles.put(tile.getElementId(),tile);
					tileList.add(tile);
				}
				displayTiles();
			}

		});
	}

	private void changeSelection(Image image) {
		Tile tile = null;
		for (Tile t : this.tileList){
			if (t.getImageUrl().equals(image.getUrl())){
				tile = t;
				break;
			}
		}
		if (tile!=null){
			sizePanel.setDisplayedWidth(tile.getWidth());
			sizePanel.setDisplayedHeight(tile.getHeight());
			EditorPanel.setSelected(image.getElement(), tile, sizePanel.getDimensions());
		}
	}

	public void searchByTag(List<MapObject> result) {
		tileList.clear();
		allTiles.clear();
		for (MapObject tile : result){
			allTiles.put(tile.getElementId(),(Tile)tile);
			tileList.add((Tile)tile);
		}
		displayTiles();
	}

	private void displayTiles() {

		tilePanel.clear();
		//		DivWidget shim = new DivWidget();
		//		shim.setWidth("100px");
		//		tilePanel.add(shim);

		for (int i = 0; i < tileList.size() ; i++){
			Tile tile = tileList.get(i);
			final Image image = new Image(tile.getImageUrl());
			//			int height = image.getHeight();
			//			int width = image.getWidth();
			//			image.setHeight(((int)targetHeight )+"px");
			//			double perc = targetHeight/height;
			//			width = (int) (width*perc);
			//			image.setWidth(width+"px");

			image.setHeight("100px");
			image.setWidth("100px");

			image.setTitle(tile.getId()+":"+tile.getTags().toString());
			image.getElement().setId(tile.getElementId());

			image.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					changeSelection(image);
					ContextMenu.hide();
				}
			});
			
			if (i==tileList.size()-1){
				image.addLoadHandler(new LoadHandler() {
					
					@Override
					public void onLoad(LoadEvent event) {
						loadLock = false;
					}
				});
			}

			tilePanel.add(image);
		}

		//		shim = new DivWidget();
		//		shim.setWidth("100px");
		//		tilePanel.add(shim);
		//		tilePanel.add(shim);
	}

	@Override
	public void resetList() {
		tileList.clear();
		for (Tile tile : allTiles.values())tileList.add(tile);
				displayTiles();
	}

	@Override
	public MapObject getMapObjectById(String id) {
		return allTiles.get(id);
	}

	@Override
	public Dimensions getDimensions() {
		return sizePanel.getDimensions();
	}
	
	@Override
	public void load() {
		if (allTiles.size()>0)return;
		loadTiles(0);		
	}

}
