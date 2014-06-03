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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.dto.FigureStruct;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.shared.dto.Dimensions;
import com.mut8ed.battlemap.shared.dto.Figure;
import com.mut8ed.battlemap.shared.dto.MapObject;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterBrief;

/**
 * dropdown sliding menu for figures.  this only exists as part of the MapMenu
 * which only exists as part of the mapview.
 * @author ryan
 *
 */
public class CharacterSlider extends EditorPanel {


	private static CharacterSlider instance = null;
	private Map<String,FigureStruct> allCharacters = new HashMap<String,FigureStruct>();
	//	private VerticalPanel categories = new VerticalPanel();
	private Label closeButton;
	private List<FigureStruct> characterList = new ArrayList<FigureStruct>();
	//	private SizePanel sizePanel;
	private double targetHeight = 100.0;
	private FlowPanel characterPanel = new FlowPanel();
	private AbsolutePanel scrollDiv;
	private int height;
	private int nextPage = 1;
	private boolean loadLock = false;
	private FigureStruct selectedCharacter;

	public interface FigureClickHandler {

		public void figureClicked(Image image, Figure figure);

	}

	private CharacterSlider(){
		super();

		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		characterPanel.getElement().setId("characterPanel");
		scrollDiv = new AbsolutePanel();
		scrollDiv.setStylePrimaryName("scrollDiv");
		scrollDiv.add(characterPanel);

		getElement().setId("characterSlider");
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
		//				loadFigures();
		//			}
		//
		//		}.schedule(250);

		//		categories = new TagPanel(this);

		//		sizePanel = new SizePanel();

		add(closeButton);
		setCellVerticalAlignment(closeButton, HasVerticalAlignment.ALIGN_BOTTOM);
		//		add(categories);
		//		add(sizePanel);
		add(scrollDiv);

		Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				setWidthAndHeight();
			}
		});


		characterPanel.addDomHandler(new ScrollHandler() {

			@Override
			public void onScroll(ScrollEvent event) {

				double scrollTop = characterPanel.getElement().getScrollTop()+height;
				double scrollHeight = characterPanel.getElement().getScrollHeight();
				if (!loadLock  && scrollHeight - scrollTop < 600){
					loadCharacters(nextPage++);
				}
			}
		}, ScrollEvent.getType());
	}

	public static CharacterSlider getInstance(){
		if (instance==null){
			instance = new CharacterSlider();
		}
		return instance;
	}

	public void loadCharacters(int page){
		loadLock = true;

		//BattleMap.debug("load characters "+page);

		BattleMap.eventBus.getCharacterList(new MTAsyncCallback<Map<String,CharacterBrief>>(){


			@Override
			public void onSuccess(Map<String,CharacterBrief> result) {
				//BattleMap.debug("received character list with "+result.size()+" characters");

				//allFigures tracks the full list
				//figureList is trimmed based on category
				for (CharacterBrief cb : result.values())setupCharacter(cb);
						displayCharacters();
			}

		});
	}

	private void setupCharacter(CharacterBrief cb) {
		FigureStruct struct = new FigureStruct(cb);
		Figure figure = cb.figure;
		if (figure!=null){
			struct.setFigure(figure);
			figure.setElementId("CHARACTER-"+cb.id);
			final Image image = figure.getStance("SW");
			image.setVisible(false);
			image.setTitle(figure.getName()+": "+figure.getTags().toString());
			image.addLoadHandler(new LoadHandler() {

				@Override
				public void onLoad(LoadEvent event) {
					int height = image.getHeight();
					int width = image.getWidth();
					image.setHeight(((int)targetHeight)+"px");
					double perc = targetHeight/height;
					width = (int)(width*perc);
					image.setWidth(width+"px");
					image.setVisible(true);
				}
			});

			struct.setImage(image);

			image.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					selectFigure(image);
				}
			});

		allCharacters.put(cb.id,struct);
		characterList.add(struct);

		}
	}


	public void setWidthAndHeight(){
		int width = Window.getClientWidth()/3;
		scrollDiv.setWidth(width+"px");
		Element elem = DOM.getElementById("alerts");
		int alertsHeight = (elem!=null) ? elem.getOffsetHeight() : 0;
		elem = DOM.getElementById("mapMenu");
		int menuHeight = (elem!=null) ? elem.getOffsetHeight() : 0;
		height = Window.getClientHeight()-closeButton.getOffsetHeight()/*-categories.getOffsetHeight()*/-alertsHeight-menuHeight/*-sizePanel.getOffsetHeight()*/;
		scrollDiv.setHeight(height+"px");
		DOM.setStyleAttribute(this.getElement(), "top", menuHeight+"px");
	}

	private void selectFigure(Image image) {
		Figure figure = null;
		for (FigureStruct fs : this.characterList){
			if (fs.getFigure()==null)continue;
			if (fs.getFigure().getStanceUrl("SW").equals(image.getUrl())){
				figure = fs.getFigure();
				selectedCharacter = fs;
				break;
			}
		}
		if (figure!=null){
			//			EditorPanel.setSelected(image.getElement(), figure, sizePanel.getDimensions());
			EditorPanel.setSelected(image.getElement(), figure, new Dimensions(1, 1, 1));
			//			sizePanel.setDisplayedWidth(figure.getWidth());
			//			sizePanel.setDisplayedHeight(figure.getHeight());
		}

	}

	private void displayCharacters() {

		characterPanel.clear();
		FlowPanel shim = new FlowPanel();
		shim.setWidth("100px");
		characterPanel.add(shim);

		for (int i = 0; i < characterList.size() ; i++){
			final FigureStruct struct = characterList.get(i);
			//BattleMap.debug("displaying "+struct.getCharacterBrief().characterName);
			FlowPanel box = new FlowPanel();
			box.setStylePrimaryName("characterBox");
			Image image = struct.getImage();
			if (i==characterList.size()-1){
				image.addLoadHandler(new LoadHandler() {

					@Override
					public void onLoad(LoadEvent event) {
						loadLock = false;
					}
				});
			}
			box.add(image);
			FlowPanel label = new FlowPanel();
			DOM.setInnerText(label.getElement(), struct.getCharacterBrief().characterName);
			box.add(label);
			characterPanel.add(box);
		}

		shim = new FlowPanel();
		shim.setWidth("100px");
		characterPanel.add(shim);
		characterPanel.add(shim);
	}

	@Override
	public void resetList() {
		characterList.clear();
		allCharacters.clear();
		load();
	}

	@Override
	public void searchByTag(List<MapObject> result) {
		//		characterList.clear();
		//		for (MapObject figure : result){
		//			setupCharacter();
		//		}
		//		displayFigures();
	}

	@Override
	public MapObject getMapObjectById(String id) {
		try {
			return allCharacters.get(id).getFigure();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Dimensions getDimensions() {
		return new Dimensions(1, 1, 1);
	}

	@Override
	public void load() {
		System.out.println("load characters called");
		if (allCharacters.size()>0)return;
		loadCharacters(0);
	}

	public FigureStruct getSelectedCharacter() {
		return selectedCharacter;
	}

	public void setSelectedCharacter(FigureStruct selectedCharacter) {
		this.selectedCharacter = selectedCharacter;
	}

}
