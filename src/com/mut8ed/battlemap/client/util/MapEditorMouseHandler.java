/**
 * 
 */
package com.mut8ed.battlemap.client.util;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.view.MapView;
import com.mut8ed.battlemap.client.widget.ToolPanel;
import com.mut8ed.battlemap.client.widget.ToolPanel.ToolType;
import com.mut8ed.battlemap.client.widget.editor.CharacterSlider;
import com.mut8ed.battlemap.client.widget.editor.EditorPanel;
import com.mut8ed.battlemap.shared.Defaults;
import com.mut8ed.battlemap.shared.MapObjectType;
import com.mut8ed.battlemap.shared.dto.Dimensions;
import com.mut8ed.battlemap.shared.dto.MapObjectWrapper;

public class MapEditorMouseHandler implements MouseMoveHandler, MouseDownHandler, MouseUpHandler {
	
	private boolean clickValid = true;
	private boolean tracking = false;
	private Dimensions dimensions;
	private boolean enabled = true;
	private static MapEditorMouseHandler instance = null; 

	private MapEditorMouseHandler(){
		dimensions = new Dimensions(1,1,1); 
	}
	
	public static MapEditorMouseHandler getInstance(){
		if (instance==null)instance = new MapEditorMouseHandler();
		return instance;
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		
		if (!enabled)return;
		
		tracking = true;
		//BattleMap.debug("mouse down");
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (tracking){
			clickValid = false;
			if (BattleMap.debug)BattleMap.setMessage("click invalidated");
		}
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		
		if (!enabled)return;
		
		//BattleMap.debug("mouse up");
		if (clickValid && !ToolPanel.selectedTool.equals(ToolType.MULTI)){
			//BattleMap.debug("valid click");
			int row, col;
			MapView mapView = MapView.getInstance();
			double multiplier = (Defaults.CELLSIZE*1.0/mapView.getCellSize());
			
			int x = event.getRelativeX(mapView.mapTable.getElement());
			int y = event.getRelativeY(mapView.mapTable.getElement());
			
			row = y/mapView.getCellSize();
			col = x/mapView.getCellSize();
			
			x*=multiplier;
			y*=multiplier;

			//BattleMap.debug("MapEditorMouseHandler.onMouseUp:adding map object "+EditorPanel.selectedMapObject.getElementId());
			MapObjectWrapper mow = new MapObjectWrapper(
					null, 
					null,
					dimensions.getHeight(), 
					null, 
					null, 
					true, 
					dimensions.getWidth(), 
					x, 
					y,
					MapView.getInstance().getCurrentLayerIndex(),
					0
			);
			mow.setObj(EditorPanel.selectedMapObject.clone());
			if (!EditorPanel.selectedMapObject.getMapObjectType().equals(MapObjectType.DECAL)){
				mow.setX(col);
				mow.setY(row);
			}
			if (EditorPanel.selectedMapObject.getMapObjectType().equals(MapObjectType.FIGURE)){
				mow.setRotation(135);//facing SW
			}
			if (EditorPanel.selectedMapObject.getMapObjectType().equals(MapObjectType.FIGURE) && 
					EditorPanel.isOpen() && EditorPanel.getOpenedPanel() instanceof CharacterSlider){
				
				mow.setCharacterId(CharacterSlider.getInstance().getSelectedCharacter().getCharacterId());
				
			}

			
			BattleMap.eventBus.addMapObject(mow, new MTAsyncCallback<Void>());

			//BattleMap.debug("selected cell "+col+":"+row);
		} else {
			//BattleMap.debug("mouse up");
		}
		tracking = false;
		clickValid = true;
	}

	public void setDimensions(Dimensions dimensions) {
		this.dimensions = dimensions;
	}

	public Dimensions getDimensions() {
		return dimensions;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
}