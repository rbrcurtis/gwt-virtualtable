package com.mut8ed.battlemap.client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.dto.FigureStruct;
import com.mut8ed.battlemap.client.util.FigureMoveHandler;
import com.mut8ed.battlemap.client.util.FigureMoveTimer;
import com.mut8ed.battlemap.client.util.Jq;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.client.util.MapKeyboardHandler;
import com.mut8ed.battlemap.client.util.MapMouseHandler;
import com.mut8ed.battlemap.client.widget.ChatPanel;
import com.mut8ed.battlemap.client.widget.ContextFocusPanel;
import com.mut8ed.battlemap.client.widget.LayerPanel;
import com.mut8ed.battlemap.client.widget.MapMenu;
import com.mut8ed.battlemap.client.widget.NoteDialog;
import com.mut8ed.battlemap.client.widget.ToolPanel;
import com.mut8ed.battlemap.client.widget.editor.CharacterSlider;
import com.mut8ed.battlemap.client.widget.editor.DecalSlider;
import com.mut8ed.battlemap.client.widget.editor.EditorPanel;
import com.mut8ed.battlemap.client.widget.editor.FigureSlider;
import com.mut8ed.battlemap.client.widget.editor.TileSlider;
import com.mut8ed.battlemap.shared.Defaults;
import com.mut8ed.battlemap.shared.MapObjectType;
import com.mut8ed.battlemap.shared.dto.Decal;
import com.mut8ed.battlemap.shared.dto.Figure;
import com.mut8ed.battlemap.shared.dto.GameMap;
import com.mut8ed.battlemap.shared.dto.MapCell;
import com.mut8ed.battlemap.shared.dto.MapObject;
import com.mut8ed.battlemap.shared.dto.MapObjectWrapper;
import com.mut8ed.battlemap.shared.dto.Note;
import com.mut8ed.battlemap.shared.dto.Tile;
import com.mut8ed.battlemap.shared.event.AddMapObjectsEvent;
import com.mut8ed.battlemap.shared.event.ChangeMapNameEvent;
import com.mut8ed.battlemap.shared.event.LoadMapEvent;
import com.mut8ed.battlemap.shared.event.MapEvent;
import com.mut8ed.battlemap.shared.event.MapObjectVisibilityEvent;
import com.mut8ed.battlemap.shared.event.MoveDecalEvent;
import com.mut8ed.battlemap.shared.event.MoveFigureEvent;
import com.mut8ed.battlemap.shared.event.RedirectEvent;
import com.mut8ed.battlemap.shared.event.RemoveMapObjectsEvent;
import com.mut8ed.battlemap.shared.event.RotateMapObjectEvent;
import com.mut8ed.battlemap.shared.event.SetEditableEvent;
import com.mut8ed.battlemap.shared.event.UpdateLayerVisibilityEvent;
import com.mut8ed.battlemap.shared.event.UpdateNoteEvent;
import com.mut8ed.battlemap.shared.exception.StopEventLoopException;

public class MapView extends FlowPanel {

	private int cellSize = Defaults.CELLSIZE;
	private static MapView instance = null; 
	public final GWTCanvas canvas;
	public final ContextFocusPanel focusPanel;
	public final AbsolutePanel mainPanel;
	/** the service object for talking to the server */
	public Grid mapTable;
	public boolean figureMoving = false;
	private MapObjectWrapper copied;
	private boolean editable = false;
	private Map<String,FigureStruct> figures = new HashMap<String, FigureStruct>();//figureId, figurestruct
	private GameMap gameMap = new GameMap();
	private MapKeyboardHandler kbHandler;
	/** elementId,image */
	private Map<String,Image> mapImages = new HashMap<String, Image>();
	/** elementId,map object wrapper */
	private MapMenu mapMenu;
	private MapMouseHandler mapMouseHandler;
	private int positionX = 0;
	private int positionY = 0;
	private Element selected = null;
	private HTML selectionRing;
	private int speed = 4;
	private LayerPanel layerPanel;
	private FlexTable borderPanel;
	private Label rightBorder;
	public Label leftBorder;
	private Label bottomBorder;
	public Label topBorder;


	/**
	 * constructor
	 */
	private MapView(){

		getElement().setId("mapBody");

		mainPanel = new AbsolutePanel();
		mainPanel.getElement().setId("mainPanel");


		setupMapBorders();

		focusPanel = new ContextFocusPanel(borderPanel);
		focusPanel.getElement().setId("mapView");

		mapMouseHandler = new MapMouseHandler(this);
		focusPanel.addMouseWheelHandler(mapMouseHandler);
		focusPanel.addMouseDownHandler(mapMouseHandler);
		focusPanel.addMouseMoveHandler(mapMouseHandler);
		focusPanel.addMouseUpHandler(mapMouseHandler);
		focusPanel.addClickHandler(mapMouseHandler);

		//BattleMap.debug("creating canvas");
		canvas = new GWTCanvas(gameMap.getCellCountX()*cellSize,gameMap.getCellCountY()*cellSize);
		canvas.getElement().setId("canvas");
		canvas.setLineWidth(4);
		canvas.setStrokeStyle(Color.RED);

		mainPanel.add(canvas);

		add(focusPanel);

		this.kbHandler = MapKeyboardHandler.getInstance();
		focusPanel.addMouseMoveHandler(kbHandler);


		focusPanel.setFocus(true);

		focusPanel.addFocusHandler(new FocusHandler(){

			@Override
			public void onFocus(FocusEvent event) {
				//BattleMap.debug("map has focus");
				kbHandler.setEnabled(true);
			}

		});

		focusPanel.addBlurHandler(new BlurHandler(){

			@Override
			public void onBlur(BlurEvent event) {
				//BattleMap.debug("map has blurred");
				//				kbHandler.setEnabled(true);
			}

		});

		selectionRing = new HTML();
		selectionRing.getElement().setId("selectionRing");

		Jq.disableSelect(this.getElement());

		if (ChatPanel.getInstance().isVisible())BattleMap.add(ChatPanel.getInstance());
		layerPanel = LayerPanel.getInstance();
		mapMenu = MapMenu.getInstance();
		
		BattleMap.eventBus.getMapState(new MTAsyncCallback<MapEvent>() {

			@Override
			public void onSuccess(MapEvent result) {
				try {
					
					BattleMap.eventBus.isEditor(new MTAsyncCallback<Boolean>(){
						
						@Override
						public void onSuccess(Boolean isEditor) {
							setEditable(isEditor);
						}
						
					});

					handleEvent(result);
					
					
				} catch (StopEventLoopException e) {
					BattleMap.stopEventsLoop();
				}
			}
		});

	}




	private void setupMapBorders() {
		borderPanel = new FlexTable();
		borderPanel.getElement().setId("mapBorderPanel");
		borderPanel.setCellPadding(0);
		borderPanel.setCellSpacing(0);
		borderPanel.setWidget(1, 1, mainPanel);

		topBorder = new Label("^");
		topBorder.setStyleName("mapExpandButtonHorizontal");
		topBorder.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				BattleMap.eventBus.expandMap("up", new MTAsyncCallback<Void>());
			}
		});
		borderPanel.setWidget(0, 1, topBorder);

		bottomBorder = new Label("v");
		bottomBorder.setStyleName("mapExpandButtonHorizontal");
		bottomBorder.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				BattleMap.eventBus.expandMap("down", new MTAsyncCallback<Void>());
			}
		});
		borderPanel.setWidget(2, 1, bottomBorder);

		leftBorder = new Label("<");
		leftBorder.setStyleName("mapExpandButtonVertical");
		leftBorder.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				BattleMap.eventBus.expandMap("left", new MTAsyncCallback<Void>());
			}
		});
		borderPanel.setWidget(1, 0, leftBorder);

		rightBorder = new Label(">");
		rightBorder.setStyleName("mapExpandButtonVertical");
		rightBorder.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				BattleMap.eventBus.expandMap("right", new MTAsyncCallback<Void>());
			}
		});
		borderPanel.setWidget(1, 2, rightBorder);
	}




	@Override
	public void clear() {
		//BattleMap.debug("clearing map, there are "+mapImages.size()+" images on the map.");
		for (String id : mapImages.keySet()){
			//BattleMap.debug("clearing "+id);
			mapImages.get(id).removeFromParent();
		}
		mapImages.clear();
		layerPanel.clear();
	}


	public static MapView getInstance(){
		if (instance==null){
			instance = new MapView(); 
		}
		return instance;
	}

	/**
	 * 
	 * @param selectedImage the image
	 * @param selectedType the oject type
	 * @param x col of cell
	 * @param y row of cell
	 * @param cellWidth number of cells wide
	 * @param cellHeight number of cells tall
	 * @return 
	 */
	private Element addMapObject(MapObjectWrapper mow) {

		MapObject mo = mow.getMapObject();

		//BattleMap.debug("add map object " +mo.getElementId()+":"+mow.getX()+"x"+mow.getY()+"x"+mow.getZ()+"/"+mow.getWidth()+"/"+mow.getHeight() );
		if (mow.getCharacterId()!=null)//BattleMap.debug("character id "+mow.getCharacterId());

		mow.setObj(mo);
		gameMap.addMapObject(mow);

		List<MapObjectWrapper> mows = new ArrayList<MapObjectWrapper>();
		mows.add(mow);


		Image img = addToMap(mow);
		if (img!=null)return img.getElement();
		else return null;

	}

	//	public void addMultipleMapObjects(List<MapObjectWrapper> mowList) {
	//		List<MapObjectWrapper> mowl = new ArrayList<MapObjectWrapper>();
	//		List<String> toRemove = new ArrayList<String>();
	//
	//		for (MapObjectWrapper mow : mowList){
	//			mowl.add(mow/*.clone()*/);
	//			mow.setZ(layerPanel.getCurrentLayerIndex());
	//			MapObject mo = mow.getObj();
	//
	//			//BattleMap.debug("add map object " +mo.getElementId()+":"+mow.getX()+"x"+mow.getY()+"x"+mow.getZ()+"/"+mow.getWidth()+"/"+mow.getHeight());
	//
	//			mow.setObj(mo);
	//			gameMap.addMapObject(mow);
	//			addToMap(mow, false, toRemove);
	//		}
	//
	//		mapService.removeMapObjects(toRemove, false, new DefaultAsyncCallback<Void>());
	//		mapService.addMapObjects(mowl, new DefaultAsyncCallback<Void>());
	//
	//	}

	public void copySelected() {
		if (selected!=null){
			MapObjectWrapper mow = gameMap.getMapObjects().get(selected.getId());
			if (mow!=null){
				BattleMap.setMessage("Copied "+selected.getId());
				copied = mow;
			} else {
				//BattleMap.debug("couldnt find selected MOW in collection in order to copy");	
			}
		} else {
			//BattleMap.debug("selected is null, nothing to copy.");
		}
	}

	public void cutSelected() {
		if (selected!=null){
			copySelected();
			BattleMap.eventBus.removeMapObject(selected.getId(), new MTAsyncCallback<Void>());
			setSelected(null);
			focusPanel.setFocus(true);
		}
	}

	/**
	 * delete everything within the given box.  does not actually remove, but broadcasts
	 * and the event should come back and the removal will happen then
	 * @param startX
	 * @param endX
	 * @param startY
	 * @param endY
	 */
	public void deleteMulti(int startX, int endX, int startY, int endY) {
		//BattleMap.debug("delete multi "+startX+":"+endX+":"+startY+":"+endY);
		setSelected(null);
		startX+=positionX+leftBorder.getOffsetWidth();
		endX+=positionX+leftBorder.getOffsetWidth();
		startY+=positionY+topBorder.getOffsetHeight();
		endY+=positionY+topBorder.getOffsetHeight();
		//BattleMap.debug("adjusted delete multi "+startX+":"+endX+":"+startY+":"+endY);
		
		List<String> toRemove = new ArrayList<String>();
		for (Image image : this.mapImages.values()){
			//BattleMap.debug("checking "+image.getElement().getId()+" at " +
//					image.getAbsoluteLeft()+"x"+image.getAbsoluteTop()+",w="+image.getWidth()+",h="+image.getHeight());
			MapObjectWrapper mow = gameMap.getMapObjects().get(image.getElement().getId());
			if (mow.getZ()!=layerPanel.getCurrentLayerIndex())continue;
			if (
					image.getAbsoluteLeft()>startX
					&&
					image.getAbsoluteLeft()+image.getWidth()<endX
					&&
					image.getAbsoluteTop()>startY
					&
					image.getAbsoluteTop()+image.getHeight()<endY
					){
				toRemove.add(image.getElement().getId());
			}
		}
		//		for (String id : toRemove){
		//			removeFromMap(id, true);
		//		}
		BattleMap.eventBus.removeMapObjects(toRemove, new MTAsyncCallback<Void>());
	}

	public void deleteSelected() {
		//BattleMap.debug("delete selected called");
		if (selected==null){
			//BattleMap.debug("nothing to delete");
			return;
		}
		BattleMap.eventBus.removeMapObject(selected.getId(), new MTAsyncCallback<Void>());
		setSelected(null);
	}

	public int getCellCountX() {
		return gameMap.getCellCountX();
	}

	public int getCellCountY() {
		return gameMap.getCellCountY();
	}

	public int getCellSize() {
		return cellSize;
	}

	public int getCurrentLayerIndex(){
		return layerPanel.getCurrentLayerIndex();
	}

	public GameMap getGameMap(){
		return gameMap;
	}

//	public int getMapId() {
//		return gameMap.getId();
//	}

	public Image getMapImage(String elementId) {
		return mapImages.get(elementId);
	}

	public MapObjectWrapper getMapObject(String elementId) {
		if (gameMap==null)return null;
		return gameMap.getMapObjects().get(elementId);
	}


	public int getPositionX() {
		return positionX;
	}


	public int getPositionY() {
		return positionY;
	}


	public String getSelected(){
		if (selected!=null)return selected.getId();
		return null;
	}


	public String getSelectedName() {
		if (selected==null)return null;
		return gameMap.getMapObjects().get(selected).getName();
	}


	public boolean isEditable() {
		return editable;
	}


	public boolean isLayerVisible(int layer) {
		return gameMap.isLayerVisible(layer);
	}


	public void moveDownOneLayer() {
		switchLayer(layerPanel.getCurrentLayerIndex()-1);		
	}


	public void moveUpOneLayer() {
		switchLayer(layerPanel.getCurrentLayerIndex()+1);
	}

	public void pasteSelected(int x, int y) {
		//BattleMap.debug("pasting "+copied+" at "+x+"x"+y);
		if (copied!=null)copied = copied.clone();
		if (copied.getMapObjectType().equalsAny(MapObjectType.DECAL, MapObjectType.NOTE)){
			copied.setX(x);
			copied.setY(y);
			copied.setZ(layerPanel.getCurrentLayerIndex());
		} else {
			copied.setX(x/cellSize);
			copied.setY(y/cellSize);
			copied.setZ(layerPanel.getCurrentLayerIndex());
		}
		BattleMap.eventBus.addMapObject(copied, new MTAsyncCallback<Void>("add map object failed"));
	}

//	public void reloadMap(){
//		//		//BattleMap.debug("reloadMap called, gameMap:"+gameMap);
//		//		if (gameMap!=null){
//		//			loadMapFromServer(gameMap.getId());
//		//		} else {
//		//			//BattleMap.debug("cant reload map, gameMap is null.");
//		//		}
//
//		getEventsLoop();
//	}

	private void removeMapObjects(List<String> ids){
		for (String id : ids){
			MapObjectWrapper mow = gameMap.remove(id);
			//BattleMap.debug("removing "+id);

			
			if (mow!=null) {
				layerPanel.decrementObjectCount(mow.getZ());
				if (!mow.getElementId().contains("NOTE")) {
					mapImages.remove(id).removeFromParent();
				} else {
					DOM.getElementById(id).removeFromParent();
				}
			}
		}
	}

	public void setRotation(String id, int rotation){
		if (selected==null)return;
		while (rotation<0)rotation+=360;
		rotation = rotation%360;
		MapObjectWrapper mow = gameMap.getMapObjects().get(id);
		//BattleMap.debug("rotating "+selected.getId()+" to "+rotation);
		if (mow!=null){
			BattleMap.eventBus.rotateObject(selected.getId(), mow.getRotation(), new MTAsyncCallback<Void>());
		} else {
			//BattleMap.debug("couldn't find selected object in gameMap");
		}
	}

	/**
	 * calculates rotation and send to the server to broadcast
	 * @param delta
	 */
	public void rotateSelected(int delta) {
		if (selected==null)return;
		MapObjectWrapper mow = gameMap.getMapObjects().get(selected.getId());
		if (mow!=null){
			int rotation = mow.getRotation();
			rotation = rotation+=delta;
			if (rotation>360)rotation=0+rotation-360;
			if (rotation<0)rotation=360+rotation;
			//BattleMap.debug("rotating "+selected.getId()+" to "+rotation);
			BattleMap.eventBus.rotateObject(selected.getId(), rotation, new MTAsyncCallback<Void>());
		} else {
			//BattleMap.debug("couldn't find selected object in gameMap");
		}
	}

	public void registerClick(int x, int y) {
		//BattleMap.debug("register click at "+x+"x"+y+", game map has "+gameMap.getMapObjects().size()+" objects");

		if (!figureMoving){
			focusPanel.setFocus(true);
			Element selected = null;
			for (MapObjectWrapper mow : gameMap.getMapObjects().values()){
				String id = mow.getMapObject().getElementId();
				Element element = DOM.getElementById(id);
				int left = element.getOffsetLeft();
				int top = element.getOffsetTop();
				int right = left+element.getClientWidth();
				int bottom = top+element.getClientHeight();
//				//BattleMap.debug("checking "+id+" at "+left+"x"+top+"x"+right+"x"+bottom+" on layer "+mow.getZ());
				if (mow.getZ()!=layerPanel.getCurrentLayerIndex())continue;
				if (
						x>=left && x<=right
						&&
						y>=top && y<=bottom
						){
					if (selected!=null){
						String sId = selected.getId();
						MapObjectType sType = MapObjectType.valueOf(sId.substring(0,sId.indexOf("-")));
						int selectedIndex = getSelectionPreferenceIndex(sType);
						String iId = element.getId();
						MapObjectType iType = MapObjectType.valueOf(iId.substring(0,iId.indexOf("-")));
						int objIndex = getSelectionPreferenceIndex(iType);
						if (objIndex>selectedIndex){
							selected = element;
						}

					} else {
						selected = element;
					}

				} else {

				}
			}
			if (selected!=null){
				BattleMap.setMessage("selected "+selected.getId()+" on layer "+gameMap.getMapObjects().get(selected.getId()).getZ());
				setSelected(selected);
			} else {
				setSelected(null);
			}
		}
	}

	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}

	/**
	 * @param image
	 * @param mow
	 */
	public void setDecalPosition(Image image, MapObjectWrapper mow) {
		//BattleMap.debug("set position of "+mow);
		//set decal position
		int imgWidth = cellSize*mow.getWidth();
		int imgHeight = cellSize*mow.getHeight();
		image.setWidth(imgWidth+"px");
		image.setHeight(imgHeight+"px");

		Element elem = image.getElement();

		double multiplier = cellSize/(Defaults.CELLSIZE*1.0);
		int x = (int)(multiplier*mow.getX())-imgWidth/2;
		int y = (int)(multiplier*mow.getY())-imgHeight/2;
		//BattleMap.debug("putting decal at "+x+":"+y+" using multiplier "+multiplier);
		DOM.setStyleAttribute(elem, "position", "absolute");
		DOM.setStyleAttribute(elem, "left", x+"px");
		DOM.setStyleAttribute(elem, "top", y+"px");
		int zIndex = (-(layerPanel.getHighestLayer()-mow.getZ())*1000)+(mow.getY()/cellSize)+2;
		DOM.setStyleAttribute(elem, "zIndex", zIndex+"");
	}

	public void setEditable(boolean editing) {

		this.editable = editing;
		//TODO animate this
		if (editing){
			mainPanel.add(ToolPanel.getInstance());
			mainPanel.add(layerPanel);
			add(mapMenu);
			ChatPanel.getInstance().resetHeight();
			
			EditorPanel m = CharacterSlider.getInstance(); 
			if (!m.isAttached())BattleMap.add(m);
			m = FigureSlider.getInstance();
			if (!m.isAttached())BattleMap.add(m);
			m = TileSlider.getInstance();
			if (!m.isAttached())BattleMap.add(m);
			m = DecalSlider.getInstance();
			if (!m.isAttached())BattleMap.add(m);
			
		} else {
			mainPanel.remove(ToolPanel.getInstance());
			mainPanel.remove(layerPanel);
			remove(mapMenu);
			ChatPanel.getInstance().resetHeight();
			remove(FigureSlider.getInstance());
			remove(TileSlider.getInstance());
			remove(DecalSlider.getInstance());
		}
	}

	public void setFigurePosition(Image image, MapObjectWrapper mow) {
		//BattleMap.debug("set figure position");
		//		FigureStruct struct = figures.get(image.getElement().getId());
		int height = (int)(cellSize*1.5*mow.getHeight());
		int width = (int)(cellSize*4.0/5.0*mow.getWidth());
		Element elem = image.getElement();
		//BattleMap.debug("image width x height = "+width+"x"+height);
		image.setWidth(width+"px");
		image.setHeight(height+"px");
		int x = cellSize*mow.getX() + cellSize/2 - width/2;
		int y = cellSize*mow.getY() + cellSize/2 - height + cellSize/5;//last gives room for the foot.
		//BattleMap.debug(x+":"+y);
		DOM.setStyleAttribute(elem, "position", "absolute");
		DOM.setStyleAttribute(elem, "left", x+"px");
		DOM.setStyleAttribute(elem, "top", y+"px");
		//(-(highestLayer-z)*1000)+cellY+2
		int zIndex = (-(layerPanel.getHighestLayer()-mow.getZ())*1000)+(mow.getY())+2;
		DOM.setStyleAttribute(elem, "zIndex", zIndex+"");
	}

//	public void setMapId(int mapId) {
//		this.gameMap.setId(mapId);
//	}

	public void setMapSize(int cellSize) {
		this.cellSize = cellSize;
		//BattleMap.debug("cell size:"+cellSize);
		int width = cellSize*gameMap.getCellCountX();
		int height = cellSize*gameMap.getCellCountY();

		mapTable.setWidth(width+"px");
		mapTable.setHeight(height+"px");
		canvas.setCoordSize(width, height);
		leftBorder.setHeight(height+"px");
		rightBorder.setHeight(height+"px");

		Element selected = this.selected;
		if (selected!=null)setSelected(null);

		for (MapObjectWrapper mow : gameMap.getMapObjects().values()){
			Image image = mapImages.get(mow.getMapObject().getElementId());
			if (image==null && mow.getMapObjectType()!=MapObjectType.NOTE){
				//BattleMap.debug("image is null for "+mow.getMapObject().getElementId()+"!", new Throwable());
				continue;
			}
			switch (mow.getMapObject().getMapObjectType()){

			case FIGURE:
				setFigurePosition(image, mow);
				break;

			case TILE:
				setTilePosition(image, mow);
				break;

			case DECAL:
				setDecalPosition(image, mow);
				break;

			case NOTE:
				setNotePosition(DOM.getElementById(mow.getMapObject().getElementId()), mow);
				break;

			default:break;
			}
		}

		if (selected!=null)setSelected(selected);

	}

	public void setPosition(int x, int y){
		setPositionX(x);
		setPositionY(y);
	}

	public void setPositionX(int x) {
		this.positionX = x;
		DOM.setStyleAttribute(focusPanel.getElement(), "left", x+"px");
	}

	public void setPositionY(int y) {
		this.positionY = y;
		DOM.setStyleAttribute(focusPanel.getElement(), "top", y+"px");
	}

	public void setSelected(Element elem){

		if (selected!=null){
			//BattleMap.debug("de-setSelected "+selected.getId()+":"+selected.getClassName());
			mainPanel.remove(selectionRing);
			selected = null;
		}
		if (elem==null){
			mapMenu.setSelectedEnabled(false);
		}
		if (elem!=null){
			//BattleMap.debug("setSelected "+elem.getId());
			selected = elem;
			setSelectionRingPosition();
			mainPanel.add(selectionRing);
			mapMenu.setSelectedEnabled(true);
			MapObjectWrapper mow = gameMap.getMapObjects().get(selected.getId());
			mapMenu.setSelectedName(mow.getName());
			mapMenu.setSelectedVisible(mow.isVisible());

			if (selected.getId().contains("FIGURE")){
				BattleMap.setMessage("Hit ENTER to start figure move.");
			}
			
			showRotation(selected, getMapObject(selected.getId()).getRotation());
		}

	}

	public void setSelectedName(String value) {
		if (selected==null)return;
		gameMap.getMapObjects().get(selected.getId()).setName(value);
	}

	public void setSelectionRingPosition() {
		if (selected==null || selected.getId().contains("NOTE"))return;
		int padding = 30;
		if (cellSize<100){
			padding*=(cellSize/100.0);
		}

		selectionRing.setWidth((selected.getClientWidth()+(padding*2))+"px");
		selectionRing.setHeight((selected.getClientHeight()+(padding*2))+"px");
		DOM.setStyleAttribute(selectionRing.getElement(), "left", (selected.getOffsetLeft()-padding)+"px");
		DOM.setStyleAttribute(selectionRing.getElement(), "top", (selected.getOffsetTop()-padding)+"px");
	}

	public void startFigureMove() {

		if (selected!=null && selected.getId().contains("FIGURE") && !figureMoving){

			//BattleMap.debug("figure move started for "+selected.getId());
			//			figureMoving = true;

			MapObjectWrapper cell = getMapObject(selected.getId());

			new FigureMoveHandler(cell.getX(), cell.getY(), cell.getZ(), figures.get(selected.getId()), this);

		}
	}

	public void switchLayer(int layer) {
		//BattleMap.debug("switching to layer "+layer);
		mapMenu.setLayerVisible(gameMap.isLayerVisible(layer));
		int previousLayerIndex = layerPanel.getCurrentLayerIndex();
		
		layerPanel.switchLayer(layer);
		
		int highest = layerPanel.getHighestLayer();
		if (layer>highest){
			highest = layer+1;
		}
		int zIndex = -999;
//		int zIndex = (-(highest-layer)*1000)+1;
		DOM.setStyleAttribute(mapTable.getElement(), "zIndex", zIndex+"");

		setSelected(null);

		mapMenu.setLayerName(gameMap.getLayerName(layerPanel.getCurrentLayerIndex()));

		for (MapObjectWrapper mow : gameMap.getMapObjects().values()){
			int z = mow.getZ();
			if (z!=previousLayerIndex && z!=layerPanel.getCurrentLayerIndex())continue;
			String elementId = mow.getMapObject().getElementId();
			Image image = mapImages.get(elementId);
			if (image!=null){
				setObjectVisibilityByLayer(z, image);
			}
		}
	}




	public void setObjectVisibilityByLayer(int z, Image image) {
//		if (z>layerPanel.getCurrentLayerIndex()){
//			image.setVisible(false);
//		} else if (z==layerPanel.getCurrentLayerIndex()){
//			image.setVisible(true);
//			image.removeStyleName("blur");
//		} else if (z<layerPanel.getCurrentLayerIndex()){
//			image.setVisible(true);
//			image.addStyleName("blur");
//		}
		image.setVisible(z==layerPanel.getCurrentLayerIndex());
	}

	public void toggleLayerVisible(){
		toggleLayerVisible(layerPanel.getCurrentLayerIndex());
	}

	public void toggleLayerVisible(int layer){
		boolean visible = !gameMap.isLayerVisible(layer);
		BattleMap.eventBus.setLayerVisibility(layer, visible, new MTAsyncCallback<Void>());
	}

	public void toggleObjectVisible(String elementId){
		MapObjectWrapper mow = gameMap.getMapObjects().get(elementId);
		if (mow!=null){
			boolean visible = !mow.isVisible();
			BattleMap.eventBus.setObjectVisible(elementId,visible,new MTAsyncCallback<Void>());
		}
	}

	public void toggleSelectedVisible() {
		toggleObjectVisible(selected.getId());
	}

	private Image addDecalToMap(MapObjectWrapper mow) {
		Decal decal = (Decal)mow.getMapObject();
		//BattleMap.debug("display decal "+decal.getElementId());
		final Image image = new Image(decal.getImageUrl());
		image.getElement().setId(decal.getElementId());
		image.setStylePrimaryName("decal");

		//BattleMap.debug("display decal image url "+image.getUrl());
		mainPanel.add(image);
		setDecalPosition(image, mow);

		return image;

	}

	private Image addFigureToMap(final MapObjectWrapper mow) {
		Figure figure = (Figure)mow.getMapObject();
		FigureStruct struct = new FigureStruct(figure);
		figures.put(struct.getFigureId(),struct);
		final Image image = struct.getImage();
		//BattleMap.debug("display figure image url "+image.getUrl());

		//BattleMap.debug("adding to main panel");
		mainPanel.add(image);
		setFigurePosition(image, mow);

		//BattleMap.debug("done with display figure");

		return image;
	}

	/**
	 * 
	 * @param obj
	 * @param y pixel position assuming a 50px cell width
	 * @param x pixel position assuming a 50px cell width
	 * @param height
	 * @param width
	 * @return 
	 */
	private Image addNoteToMap(MapObjectWrapper mow) {
		final Note note = (Note)mow.getMapObject();
		//BattleMap.debug("adding note "+note.getElementId());
		final Image ta = new Image(Defaults.IMGURL+"/ToolButtons/note.png");
		ta.getElement().setId(note.getElementId());
		ta.setStylePrimaryName("note");
		//		ta.setValue(note.getNote());
		mainPanel.add(ta);
		mow.setWidth(ta.getOffsetWidth());
		mow.setHeight(ta.getOffsetHeight());

		ta.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				NoteDialog nd = new NoteDialog(note);
				nd.center();
				nd.focus();
				if (editable)BattleMap.setMessage("Hit escape to close the dialog and save changes.");
			}
		});

		setNotePosition(ta.getElement(), mow);

		return null;

	}

	private Image addTileToMap(MapObjectWrapper mow) {
		Tile tile = (Tile)mow.getMapObject();
		//BattleMap.debug("display tile "+tile.getElementId());
		final Image image = new Image(tile.getImageUrl());
		image.getElement().setId(tile.getElementId());

		//BattleMap.debug("display tile image url "+image.getUrl());
		mainPanel.add(image);
		setTilePosition(image, mow);
		image.setVisible(true);

		//		image.addClickHandler(new ClickHandler() {
		//
		//			@Override
		//			public void onClick(ClickEvent event) {
		//				setSelected(image.getElement());
		//			}
		//		});

		return image;

	}

	private Image addToMap(final MapObjectWrapper mow) {
		//BattleMap.debug("add to map "+mow.getMapObject().getElementId());

		Image image = null;

		layerPanel.incrementObjectCount(mow.getZ());
		
		switch (mow.getMapObjectType()){
		case FIGURE:
			image = addFigureToMap(mow);
			break;
		case TILE:
			image = addTileToMap(mow);
			break;
		case DECAL:
			image = addDecalToMap(mow);
			break;
		case NOTE:
			image = addNoteToMap(mow);
			break;
		default:
			//BattleMap.debug("invalid type "+mow.getMapObjectType());//freak out
		}

		if (image!=null){
			mapImages.put(image.getElement().getId(), image);
			showRotation(image.getElement(),mow.getRotation());
			Jq.disableSelect(image.getElement());
			Jq.disableDrag(image.getElement());
			setObjectVisibilityByLayer(mow.getZ(), image);
		}

		//BattleMap.debug("add to map end");
		return image;
	}

	

	private int getSelectionPreferenceIndex(MapObjectType type) {
		switch (type){
		case NOTE:return 3;
		case FIGURE:return 2;
		case DECAL:return 1;
		case TILE:return 0;
		default:return -1;
		}
	}

	/**
	 * handle the given event.  usually this means update the map.
	 * @param mapEvent
	 */
	public void handleEvent(MapEvent mapEvent) throws StopEventLoopException {

		try {
			if (mapEvent instanceof LoadMapEvent){

				MapView.getInstance().handleLoadMapEvent(((LoadMapEvent)mapEvent));
				
			} else if (mapEvent instanceof RedirectEvent){
				
				((RedirectEvent)mapEvent).getPage().redirect();
				return;

			} else if (mapEvent instanceof UpdateNoteEvent){

				Note note = ((UpdateNoteEvent)mapEvent).getNote();

				Note n = (Note)gameMap.getMapObjects().get(note.getElementId()).getMapObject();
				n.setNote(note.getNote());

			} else if (mapEvent instanceof SetEditableEvent){

				if (((SetEditableEvent)mapEvent).isEditable()){
					setEditable(true);
				} else {
					setEditable(false);
				}

			} else if (mapEvent instanceof MapObjectVisibilityEvent){

				MapObjectVisibilityEvent me = (MapObjectVisibilityEvent)mapEvent;
				MapObjectWrapper mow = gameMap.getMapObjects().get(me.getElementId());
				Image image = mapImages.get(me.getElementId());

				if (mow!=null && image!=null){
					setObjectVisible(mow, image, me.isVisable());
				}

			} else if (mapEvent instanceof UpdateLayerVisibilityEvent){

				UpdateLayerVisibilityEvent me = (UpdateLayerVisibilityEvent)mapEvent;

				setLayerVisibility(me.getLayer(), me.isVisibile());

			} else if (mapEvent instanceof AddMapObjectsEvent){
				for (MapObjectWrapper mo : ((AddMapObjectsEvent)mapEvent).getMapObjects()){
					addMapObject(mo);
				}

			} else if (mapEvent instanceof RemoveMapObjectsEvent){
				removeMapObjects(((RemoveMapObjectsEvent)mapEvent).getMapObjectIds());

			} else if (mapEvent instanceof MoveFigureEvent){

				MoveFigureEvent mce = (MoveFigureEvent)mapEvent;
				//BattleMap.debug("moving "+mce.getFigureId()+" along path "+mce.getPath());

				MapObjectWrapper figure = gameMap.getMapObjects().get(mce.getFigureId());
				MapCell end = mce.getPath().get(mce.getPath().size()-1);
				figure.setX(end.col);
				figure.setY(end.row);
				figure.setZ(end.layer);				

				FigureMoveTimer timer = new FigureMoveTimer(figures.get(mce.getFigureId()), mce.getPath(), speed, this);
				timer.updateSelectionRing(selected!=null && selected.getId().equals(figure.getElementId()));
				timer.scheduleRepeating(1);


			} else if (mapEvent instanceof MoveDecalEvent){

				MapObjectWrapper mow = ((MoveDecalEvent)mapEvent).getMapObject();

				if (mow==null || mow.getMapObject()==null)return;
				String id = mow.getMapObject().getElementId();

				int x = mow.getX();
				int y = mow.getY();

				Image image = getMapImage(id);

				//BattleMap.debug("moving "+id);

				//get the local copy of the object.
				mow = gameMap.getMapObjects().get(id);

				mow.setX(x);
				mow.setY(y);

				double multiplier = cellSize*1.0/Defaults.CELLSIZE;
				x = (int)(multiplier*mow.getX())-image.getWidth()/2;
				y = (int)(multiplier*mow.getY())-image.getHeight()/2;
				//BattleMap.debug("putting decal at "+x+":"+y);

				//				Jq.move(id, x, y);
				DOM.setStyleAttribute(image.getElement(), "left", x+"px");
				DOM.setStyleAttribute(image.getElement(), "top", y+"px");

				int zIndex = (-(layerPanel.getHighestLayer()-mow.getZ())*1000)+(mow.getY()/cellSize)+2;
				DOM.setStyleAttribute(image.getElement(), "zIndex", zIndex+"");

				//recenter the selection ring if the moving object is currently selected.
				if (selected!=null && selected.getId().equals(id)){
					//BattleMap.debug("updating ring position");
					selected = image.getElement();
					setSelectionRingPosition();
				}

			} else if (mapEvent instanceof RotateMapObjectEvent){
				RotateMapObjectEvent rmoe = (RotateMapObjectEvent)mapEvent; 
				String elementId = rmoe.getElementId();
				int rotation = rmoe.getRotation();

				MapObjectWrapper mow = gameMap.getMapObjects().get(elementId);
				mow.setRotation(rotation);

				Image image = mapImages.get(elementId);

				showRotation(image.getElement(), rotation);

			
			} else if (mapEvent instanceof ChangeMapNameEvent){
				mapMenu.setMapName(((ChangeMapNameEvent)mapEvent).getMapName());
			}


		} catch (Exception e) {
			if (e instanceof StopEventLoopException)throw (StopEventLoopException)e;
			Window.alert(e.toString());
			//BattleMap.debug(e.getMessage(), e);
		}

	}
	
	public void handleLoadMapEvent(LoadMapEvent mue) {
		BattleMap.setMessage("found "+mue.getGameMap().getMapObjects().size()+" objects");
		
		this.gameMap.load(mue.getGameMap());
		redrawMap();
	}

	private void redrawMap(){
		clear();
		//BattleMap.debug("redrawing map, cellcountX = "+getCellCountX()+", cellcountY = "+getCellCountY());
		
		if (mapTable!=null){
			mapTable.removeFromParent();
		}
		
		mapTable = new Grid(gameMap.getCellCountY(), gameMap.getCellCountX());
		mapTable.setCellPadding(0);
		mapTable.setCellSpacing(0);
		mapTable.getElement().setId("mapTable");
		mainPanel.add(mapTable);
		
		Jq.disableSelect(mapTable.getElement());
		
		int width = cellSize*gameMap.getCellCountX();
		int height = cellSize*gameMap.getCellCountY();

		mapTable.setWidth(width+"px");
		mapTable.setHeight(height+"px");
		canvas.setCoordSize(width, height);
		leftBorder.setHeight(height+"px");
		rightBorder.setHeight(height+"px");

		//BattleMap.debug("adding "+gameMap.getMapObjects().size()+" objects to the map");
		for (MapObjectWrapper mo : gameMap.getMapObjects().values()){
			addToMap(mo);
		}

		mapMenu.setMapName(gameMap.getName());
		switchLayer(0);

		//BattleMap.debug("done loading map");
	}

	private void setLayerVisibility(int layer, boolean visible) {
		gameMap.setLayerVisible(layer, visible);
		layerPanel.setLayerVisible(layer, visible);
		BattleMap.setMessage("setting layer "+layer+" to "+((visible)?"visible":"invisible"));
		for (MapObjectWrapper mow : gameMap.getMapObjects().values()){
			try {
				String elementId = mow.getMapObject().getElementId();
				if (mow.getZ()==layer){
					//BattleMap.debug("making "+elementId+" visible:"+visible);
					if (layer==layerPanel.getCurrentLayerIndex()){
						Image image = mapImages.get(elementId);
						if (!visible)image.addStyleName("blur");
						else if (mow.isVisible())image.removeStyleName("blur"); 
						mapMenu.setLayerVisible(visible);
					} else {
						Image image = mapImages.get(elementId);
						image.setVisible(visible);
					}
				}
			} catch (Exception e) {
				//BattleMap.debug(e.getMessage(),e);
			}
		}		
	}

	private void setNotePosition(Element elem, MapObjectWrapper mow) {
		double multiplier = cellSize/50.0;
		int x = (int)(multiplier*mow.getX());
		int y = (int)(multiplier*mow.getY());
		//BattleMap.debug("putting note at "+x+":"+y);
		DOM.setStyleAttribute(elem, "position", "absolute");
		DOM.setStyleAttribute(elem, "left", x+"px");
		DOM.setStyleAttribute(elem, "top", y+"px");
		//		int zIndex = (-(layerPanel.getHighestLayer()-mow.getZ())*1000)+999;
		int zIndex = 0;
		DOM.setStyleAttribute(elem, "zIndex", zIndex+"");
	}

	private void setObjectVisible(MapObjectWrapper mow, Image image, boolean visible) {
		boolean layerVisible = gameMap.isLayerVisible(mow.getZ());
		mow.setVisible(visible);
		if (visible && layerVisible)image.removeStyleName("blur");
		else image.addStyleName("blur");
		mapMenu.setSelectedVisible(visible);		
	}

	private void setTilePosition(Image image, MapObjectWrapper mow) {
		//BattleMap.debug("set position of "+mow);
		//set tile position
		int imgWidth = cellSize*mow.getWidth();
		int imgHeight = cellSize*mow.getHeight();
		image.setWidth(imgWidth+"px");
		image.setHeight(imgHeight+"px");

		Element elem = image.getElement();

		int x = cellSize*mow.getX();
		int y = cellSize*mow.getY();
		//BattleMap.debug("putting tile at "+x+":"+y);
		DOM.setStyleAttribute(elem, "position", "absolute");
		DOM.setStyleAttribute(elem, "left", x+"px");
		DOM.setStyleAttribute(elem, "top", y+"px");


		int zIndex = -(layerPanel.getHighestLayer()-mow.getZ())*1000;
		DOM.setStyleAttribute(elem, "zIndex", zIndex+"");
	}

	public void showRotation(Element elem, int rotation) {
		if (elem==null || elem.getId()==null)return;
		
		int ie = (int)(rotation/360.0*4);

		if (selected!=null && selected.getId().contains("DECAL") && elem.getId().equals(selected.getId())){
			DOM.setStyleAttribute(selectionRing.getElement(),"filter","progid:DXImageTransform.Microsoft.BasicImage(rotation="+ie+")"); 
			DOM.setStyleAttribute(selectionRing.getElement(),"transform", "rotate("+rotation+"deg)");
			DOM.setStyleAttribute(selectionRing.getElement(),"MozTransform", "rotate("+rotation+"deg)");
			DOM.setStyleAttribute(selectionRing.getElement(),"OTransform", "rotate("+rotation+"deg)");
			DOM.setStyleAttribute(selectionRing.getElement(),"WebkitTransform", "rotate("+rotation+"deg)");
		} else {
			DOM.setStyleAttribute(selectionRing.getElement(),"filter",""); 
			DOM.setStyleAttribute(selectionRing.getElement(),"transform", "");
			DOM.setStyleAttribute(selectionRing.getElement(),"MozTransform", "");
			DOM.setStyleAttribute(selectionRing.getElement(),"OTransform", "");
			DOM.setStyleAttribute(selectionRing.getElement(),"WebkitTransform", "");
		}

		if (elem.getId().contains("TILE")){

			if (rotation>270)rotation = 270;
			else if (rotation>180)rotation = 180;
			else if (rotation>90)rotation = 90;
			else rotation = 0;

		} else if (elem.getId().contains("FIGURE")){

			FigureStruct f = figures.get(elem.getId());
			if (rotation>=315)f.setStance("NE");
			else if (rotation>=270)f.setStance("N");
			else if (rotation>=225)f.setStance("NW");
			else if (rotation>=180)f.setStance("W");
			else if (rotation>=135)f.setStance("SW");
			else if (rotation>=90)f.setStance("S");
			else if (rotation>=45)f.setStance("SE");
			else f.setStance("E");

			return;

		} else if (elem.getId().contains("NOTE")){

			return;

		}
		DOM.setStyleAttribute(elem,"filter","progid:DXImageTransform.Microsoft.BasicImage(rotation="+ie+")"); 
		DOM.setStyleAttribute(elem,"transform", "rotate("+rotation+"deg)");
		DOM.setStyleAttribute(elem,"MozTransform", "rotate("+rotation+"deg)");
		DOM.setStyleAttribute(elem,"OTransform", "rotate("+rotation+"deg)");
		DOM.setStyleAttribute(elem,"WebkitTransform", "rotate("+rotation+"deg)");
		
	}


	public void addToMap(Widget w) {
		mainPanel.add(w);
	}




}
