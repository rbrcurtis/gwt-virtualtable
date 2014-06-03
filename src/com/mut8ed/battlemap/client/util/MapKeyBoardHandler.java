package com.mut8ed.battlemap.client.util;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.Page;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.client.view.MapView;
import com.mut8ed.battlemap.client.widget.ChatPanel;
import com.mut8ed.battlemap.client.widget.ContextMenu;
import com.mut8ed.battlemap.client.widget.MapMenu;
import com.mut8ed.battlemap.client.widget.ToolPanel;
import com.mut8ed.battlemap.client.widget.ToolPanel.ToolType;
import com.mut8ed.battlemap.client.widget.editor.CharacterSlider;
import com.mut8ed.battlemap.client.widget.editor.DecalSlider;
import com.mut8ed.battlemap.client.widget.editor.EditorPanel;
import com.mut8ed.battlemap.client.widget.editor.FigureSlider;
import com.mut8ed.battlemap.client.widget.editor.TileSlider;
import com.mut8ed.battlemap.shared.dto.MapObjectWrapper;
import com.mut8ed.battlemap.shared.dto.Note;

public class MapKeyboardHandler implements KeyDownHandler, MouseMoveHandler, KeyPressHandler, KeyUpHandler, ValueChangeHandler<String> {

	private int x;
	private int y;
	private boolean enabled = false;
	private static MapKeyboardHandler self = null;

	private MapKeyboardHandler() {
		History.addValueChangeHandler(this);
	}

	public static MapKeyboardHandler getInstance(){
		if (self==null){
			self = new MapKeyboardHandler();
		}
		return self;
	}

	@Override
	public void onKeyDown(KeyDownEvent event) {
		//		int keyCode = event.getNativeKeyCode();
		//		////BattleMap.debug("MapKBHandler.onKeyDown:key code:"+keyCode);
		//		if (keyCode>=37 && keyCode <=40){ //arrows
		//			nudge(keyCode);
		//		if (keyCode==17 || keyCode==91 || keyCode==224){
		//			controlDown = true;
		//			//			////BattleMap.debug("MKBH.onKeyDown:setting control to down");
		//		} else {
		//			return;
		//		}
		if (enabled && !event.isAnyModifierKeyDown()){
			event.stopPropagation();
			event.preventDefault();
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		x = event.getRelativeX(MapView.getInstance().mapTable.getElement());
		y = event.getRelativeY(MapView.getInstance().mapTable.getElement());
	}

	public void setEnabled(boolean enabled) {
		////BattleMap.debug("mapkbhandler is "+((enabled)?"enabled":"disabled"));
		Page page = Page.valueOf(History.getToken().toUpperCase());
		if (page!=Page.MAP){
			enabled = false;
			return;
		}
		this.enabled = enabled;
	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		String key = ""+event.getCharCode();
		int keyCode = event.getNativeEvent().getKeyCode();
		//BattleMap.debug("MapKBHandler.onKeyPress:"+key+"/"+keyCode+" at "+x+"x"+y+", "+((enabled)?"enabled":"disabled"));

		if (!enabled)return;

		if (keyCode==27 || keyCode==8){
			event.stopPropagation();
			event.preventDefault();
		}


		if (key!=null){
			if (key.equals("?")){
				ToolPanel.getInstance().showHelp();
			} else if (key.equals("/")){
				ChatPanel.getInstance().focus();
			}
		}
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		int keyCode = event.getNativeKeyCode();
		//BattleMap.debug("MapKBHandler.onKeyUp:enabled="+enabled+", keycode "+keyCode+" at "+x+"x"+y);

		if (keyCode==8){
			if (enabled)MapView.getInstance().deleteSelected(); //delete always works for control D
			event.stopPropagation();
			event.preventDefault();
			//				////BattleMap.debug("MapKBHandler.onKeyUp:prop stop");
			return;
		}
		if (enabled && MapView.getInstance().isEditable()){
			if (keyCode==27){
				//This wont work in IE apparently.
				handleEscape();

			} else if (keyCode>=37 && keyCode <=40){ //arrows
				nudge(keyCode); //TODO collect all nudges in a short duration and send the move decal one time only
				event.stopPropagation();
				event.preventDefault();
			} else if (keyCode==46){ //del
				MapView.getInstance().deleteSelected();
			} else if (keyCode==8){ //backspace
				//backspace key, usually goes back a page, probably not wanted by the user here.
				//				////BattleMap.debug("preventing page back");
				MapView.getInstance().deleteSelected();

			} else if (keyCode==13){//enter
				MapView.getInstance().startFigureMove();

			} else if (keyCode==83){ //S select tool
				ToolPanel.getInstance().selectButton(ToolType.SELECT);
			} else if (keyCode==82 && !event.isAnyModifierKeyDown()){ //R rotate tool
				ToolPanel.getInstance().selectButton(ToolType.ROTATE);
			} else if (keyCode==77){ //M multi tool
				ToolPanel.getInstance().selectButton(ToolType.MULTI);
			} else if (keyCode==88){ //X cut
				////BattleMap.debug("cut selected");
				MapView.getInstance().cutSelected();
			} else if (keyCode==67){ //C copy
				////BattleMap.debug("copy selected");
				MapView.getInstance().copySelected();
			} else if (keyCode==86){ //V paste
				////BattleMap.debug("paste selected");
				MapView.getInstance().pasteSelected(x,y);


			} else if (keyCode==72){
				//				text+="H	Character Panel\n";
//				EditorPanel.toggle(CharacterSlider.getInstance());
			} else if (keyCode==70){	
				//				text+="F	Figure Panel\n";
				EditorPanel.toggle(FigureSlider.getInstance());
			} else if (keyCode==84){
				//				text+="T	Tile Panel\n";
				EditorPanel.toggle(TileSlider.getInstance());
			} else if (keyCode==68){
				//				text+="D	Decal Panel\n";
				EditorPanel.toggle(DecalSlider.getInstance());
			} else if (keyCode==90){
				//				text+="Z	Selected Character Sheet\n";
//				MapView mapView = MapView.getInstance();
//				String selected = mapView.getSelected();
//				if (selected==null)return;
//				MapObjectWrapper mow = mapView.getMapObject(selected);
//				if (mow==null)return;
//				if (mow.getCharacterId()==null){
//					//TODO handle figure that doesnt have a character already
//				}
//				final CharacterView cv = CharacterView.getInstance();
//				cv.setVisible(false);
//				if (!cv.isAttached())BattleMap.add(cv);
//				cv.showCharacter(mow.getCharacterId());
//				Jq.show(cv.getElement().getId(), "fast");
//				setEnabled(false);
//				cv.setFocus(true);
//				cv.addKeyUpHandler(new KeyUpHandler() {
//
//					@Override
//					public void onKeyUp(KeyUpEvent event) {
//						if (event.getNativeKeyCode()==27){
//							Jq.hide(cv.getElement().getId(), "fast");
//							if (EditorPanel.isOpen())EditorPanel.toggle(null);
//							setEnabled(true);
//							event.stopPropagation();
//							event.preventDefault();
//						}
//					}
//				});

			} else if (keyCode==81){//Q up layer
				//BattleMap.debug("move up a layer");
				MapView.getInstance().moveUpOneLayer();
			} else if (keyCode==65){//A down layer
				//BattleMap.debug("move down a layer");
				MapView.getInstance().moveDownOneLayer();
			} else if (keyCode==79){//O object visible
				MapView.getInstance().toggleObjectVisible(MapView.getInstance().getSelected());
				//BattleMap.debug("toggle object visible");
			} else if (keyCode==76){//L layer visible
				//BattleMap.debug("toggle layer visible");
				MapView.getInstance().toggleLayerVisible();

			} else if (keyCode==78){ //N TODO notes disabled for now.
				MapView.getInstance().setSelected(null);
				MapObjectWrapper mow = new MapObjectWrapper(
						null, 
						null,
						0, 
						null, 
						null, 
						true, 
						0, 
						x, 
						y,
						MapView.getInstance().getCurrentLayerIndex(),
						0
						);
				mow.setObj(new Note(""));
				BattleMap.eventBus.addMapObject(mow, new MTAsyncCallback<Void>());

			} else {
				return;
			}

			event.stopPropagation();
			event.preventDefault();
			//			////BattleMap.debug("MapKBHandler.onKeyUp:prop stop");
		}

	}

	private void handleEscape() {
		//				////BattleMap.debug("escape pressed");
		if (enabled){
			MapView.getInstance().setSelected(null);
			MapView.getInstance().canvas.clear();
		}
		EditorPanel.toggle(null);
		ContextMenu.hide();
		ToolPanel.getInstance().hideHelp();
		MapMenu.getInstance().hideDropMenu();
		if (CharacterView.getInstance().isVisible() &&
				CharacterView.getInstance().isAttached()){
			Jq.hide(CharacterView.getInstance().getElement().getId(), "fast");
			setEnabled(true);
		}
		BattleMap.setMessage("");
	}

	private void nudge(int keyCode) {
		String selected = MapView.getInstance().getSelected();
		if (selected!=null && selected.contains("DECAL")){
			MapObjectWrapper selectedObject = MapView.getInstance().getMapObject(selected);
			////BattleMap.debug("nudging "+selected);
			int x = selectedObject.getX();
			int y = selectedObject.getY();
			switch (keyCode){
			case 37://left
				x--;
				break;
			case 38://up
				y--;
				break;
			case 39://right
				x++;
				break;
			case 40://down
				y++;
				break;
			}
			selectedObject.setX(x);
			selectedObject.setY(y);

			//BattleMap.debug("broadcasting decal move to "+x+":"+y);
			BattleMap.eventBus.moveDecal(selectedObject, new MTAsyncCallback<Void>("Failed to Move "+selected));
		}		
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		Page page = Page.valueOf(event.getValue().toUpperCase());
		if (page==Page.MAP)enabled = true;
		else enabled = false;
	}

}
