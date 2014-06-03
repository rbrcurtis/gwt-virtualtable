package com.mut8ed.battlemap.client.widget.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.mut8ed.battlemap.client.widget.SizePanel;
import com.mut8ed.battlemap.client.widget.SliderScrollPanel;
import com.mut8ed.battlemap.client.widget.TagPanel;
import com.mut8ed.battlemap.shared.dto.Decal;
import com.mut8ed.battlemap.shared.dto.Dimensions;
import com.mut8ed.battlemap.shared.dto.MapObject;

/**
 * dropdown sliding menu for decals.  this only exists as part of the MapMenu
 * which only exists as part of the mapview.
 * @author ryan
 *
 */
public class DecalSlider extends EditorPanel {


	private Map<String,Decal> allDecals = new HashMap<String,Decal>();
	private List<Decal> decalList = new ArrayList<Decal>();
	private VerticalPanel categories = new VerticalPanel();
	private Label closeButton;
	private double targetSize = 50.0;
	SliderScrollPanel scrollDiv;
	FlowPanel decalPanel = new FlowPanel();
	private SizePanel sizePanel;
	protected int height;
	protected boolean loadLock = false;
	private static DecalSlider instance = null;

	public static DecalSlider getInstance(){
		if (instance==null){
			instance = new DecalSlider();
		}
		return instance;
	}


	private DecalSlider(){
		super();
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		decalPanel.getElement().setId("decalPanel");
		scrollDiv = new SliderScrollPanel(this);
		scrollDiv.setStylePrimaryName("scrollDiv");
		scrollDiv.add(decalPanel);



		getElement().setId("decalSlider");
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
//				loadDecals();
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
		Label blank = new Label();
		blank.setHeight("200px");
		add(blank);

		Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				setWidthAndHeight();
			}
		});
		
		decalPanel.addDomHandler(new ScrollHandler() {
			
			private int nextPage;

			@Override
			public void onScroll(ScrollEvent event) {
				
				double scrollTop = decalPanel.getElement().getScrollTop()+height;
				double scrollHeight = decalPanel.getElement().getScrollHeight();
				if (!loadLock  && scrollHeight - scrollTop < 600){
					loadDecals(nextPage++);
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

	public void loadDecals(int page){
		loadLock = true;
		BattleMap.eventBus.getDecalList(page, new MTAsyncCallback<List<Decal>>() {

			@Override
			public void onSuccess(List<Decal> result) {

				//allDecals tracks the full list
				//decalList is trimmed based on tag
				for (Decal decal : result){
					allDecals.put(decal.getElementId(),decal);
					decalList.add(decal);
				}
				displayDecals();
			}

		});
	}

	private void changeSelection(Image image) {
		Decal decal = null;
		for (Decal t : this.decalList){
			if (t.getImageUrl().equals(image.getUrl())){
				decal = t;
				break;
			}
		}
		if (decal!=null){
			sizePanel.setDisplayedWidth(decal.getWidth());
			sizePanel.setDisplayedHeight(decal.getHeight());
			EditorPanel.setSelected(image.getElement(), decal, sizePanel.getDimensions());
		}
	}

	public void searchByTag(List<MapObject> result) {
		decalList.clear();
		for (MapObject decal : result){
			allDecals.put(decal.getElementId(),(Decal)decal);
			decalList.add((Decal)decal);
		}
		displayDecals();
	}

	private void displayDecals() {

		decalPanel.clear();
		//		DivWidget shim = new DivWidget();
		//		shim.setWidth("100px");
		//		decalPanel.add(shim);

		for (int i = 0; i < decalList.size() ; i++){
			Decal decal = decalList.get(i);
			final Image image = new Image(decal.getImageUrl());
			image.setHeight(((int)targetSize*decal.getHeight() )+"px");
			image.setWidth(((int)targetSize*decal.getWidth() )+"px");

			image.setTitle(decal.getId()+":"+decal.getTags().toString());
			image.getElement().setId(decal.getElementId());

			image.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					changeSelection(image);
				}
			});
			
			if (i==decalList.size()-1){
				image.addLoadHandler(new LoadHandler() {
					
					@Override
					public void onLoad(LoadEvent event) {
						loadLock = false;
					}
				});
			}

			decalPanel.add(image);
		}

		//		shim = new DivWidget();
		//		shim.setWidth("100px");
		//		decalPanel.add(shim);
		//		decalPanel.add(shim);
	}

	@Override
	public void resetList() {
		decalList.clear();
		for (Decal decal : allDecals.values())decalList.add(decal);
				displayDecals();
	}

	@Override
	public MapObject getMapObjectById(String id) {
		return allDecals.get(id);
	}

	@Override
	public Dimensions getDimensions() {
		return sizePanel.getDimensions();
	}

	@Override
	public void load() {
		if (allDecals.size()>0)return;
		loadDecals(0);		
	}
}
