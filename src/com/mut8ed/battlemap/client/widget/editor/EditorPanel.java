package com.mut8ed.battlemap.client.widget.editor;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.util.Jq;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.client.util.MapEditorMouseHandler;
import com.mut8ed.battlemap.client.view.AddMapObjectView;
import com.mut8ed.battlemap.client.view.MapView;
import com.mut8ed.battlemap.client.widget.ContextMenu;
import com.mut8ed.battlemap.shared.MapObjectType;
import com.mut8ed.battlemap.shared.dto.Dimensions;
import com.mut8ed.battlemap.shared.dto.MapObject;
import com.mut8ed.battlemap.shared.dto.MapObjectWrapper;

public abstract class EditorPanel extends VerticalPanel {

	private static boolean isOpen;
	private static EditorPanel openedPanel;
	private static List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();
	private static Element selectedImage;
	protected static MapEditorMouseHandler mapEditorMouseHandler;
	public static MapObject selectedMapObject;
	
	
	protected EditorPanel(){}

	/**
	 * @return a clone of the selected map object
	 */
	public static MapObject getSelectedMapObject(){
		return selectedMapObject.clone();
	}

	public static boolean isOpen() {
		return isOpen;
	}

	public static void setSelected(Element image, MapObject mo, Dimensions dimensions) {
		if (mo!=null){
			//BattleMap.debug(mo.getElementId()+" selected on editor panel");
			if (AddMapObjectView.getInstance().isAttached()){
				AddMapObjectView.getInstance().setSelectedObject(mo);
			}
		} else {
			//BattleMap.debug("selection cleared");
		}

		if (selectedImage!=null){
			selectedImage.removeClassName("selectedSliderObject");
		}
		if (image!=null){
			image.addClassName("selectedSliderObject");
			EditorPanel.selectedImage = image;
			EditorPanel.selectedMapObject = mo;

			if (registrations.size()==0){
				mapEditorMouseHandler = MapEditorMouseHandler.getInstance();
				MapView mapView = MapView.getInstance();
				registrations.add(mapView.focusPanel.addMouseMoveHandler(mapEditorMouseHandler));
				registrations.add(mapView.focusPanel.addMouseDownHandler(mapEditorMouseHandler));
				registrations.add(mapView.focusPanel.addMouseUpHandler(mapEditorMouseHandler));
			}

			mapEditorMouseHandler.setDimensions(dimensions);

		} else {
			for(int i=registrations.size()-1;i>=0;i--){
				registrations.remove(i).removeHandler();
			}
			selectedImage = null;
			selectedMapObject = null;
		}
	}

	public static void toggle(EditorPanel editor) {
		if (editor!=null && editor.isAttached())BattleMap.add(editor);
		ContextMenu.hide();
		if (openedPanel!=null){
			//BattleMap.debug("closing panel "+openedPanel.getElement().getId());
			Jq.toggle(openedPanel.getElement().getId(), "medium");
			setSelected(null,null,null);
			isOpen = false;
		}
		if (openedPanel==editor || editor==null){
			openedPanel = null;
			isOpen = false;
		} else {
			openedPanel = editor;
			//BattleMap.debug("opening panel "+editor.getElement().getId());
			editor.load();
			Jq.toggle(openedPanel.getElement().getId(), "medium");
			isOpen = true;
		}
	}

	abstract void load();

	public abstract void resetList();

	public abstract void searchByTag(List<MapObject> ids);

	public abstract MapObject getMapObjectById(String id);

	public abstract Dimensions getDimensions();

	public static void dropMulti(int startX, int endX, int startY, int endY) {
		if (EditorPanel.selectedMapObject==null){
			//BattleMap.debug("nothing selected");
			return;
		}
		MapView mapView = MapView.getInstance();
		Dimensions dimensions = openedPanel.getDimensions();
		int jumpX = dimensions.getWidth()*mapView.getCellSize();
		int jumpY = dimensions.getHeight()*mapView.getCellSize();
		
		//BattleMap.debug("add multi "+EditorPanel.selectedMapObject.getElementId());
		MapObjectWrapper mow = new MapObjectWrapper(
				null, 
				null,
				dimensions.getHeight(), 
				null, 
				null, 
				true, 
				dimensions.getWidth(), 
				0, 
				0,
				mapView.getCurrentLayerIndex(),
				0
		);
		mow.setObj(EditorPanel.selectedMapObject.clone());

		List<MapObjectWrapper> mowList = new ArrayList<MapObjectWrapper>();
		
		if (EditorPanel.selectedMapObject.getMapObjectType().equals(MapObjectType.DECAL)){
			//decals get put at a specific x/y coord, not a specific square
			for (int x=startX;x<endX;x+=jumpX){
				for (int y = startY;y<endY;y+=jumpY){
					//BattleMap.debug("add DECAL at "+x+"x"+y);
					mow.setX(x);
					mow.setY(y);
					mowList.add(mow.clone());
				}
			}
		} else {
//			BattleMap.debug("add multi "+EditorPanel.selectedMapObject.getMapObjectType()+" at "+startX+"x"+
//					startY+" to "+endX+"x"+endY);
			for (int x=startX;x<endX;x+=jumpX){
				for (int y = startY;y<endY;y+=jumpY){
					int row = y/mapView.getCellSize();
					int col = x/mapView.getCellSize();
					//BattleMap.debug("add "+EditorPanel.selectedMapObject.getMapObjectType()+" at "+col+"x"+row);
					if (col*mapView.getCellSize()<startX)continue;
					if (row*mapView.getCellSize()<startY)continue;
					if (col*mapView.getCellSize()+mapView.getCellSize()>endX)continue;
					if (row*mapView.getCellSize()+mapView.getCellSize()>endY)continue;
					//BattleMap.debug("add succeeded!");
					mow.setX(col);
					mow.setY(row);
					mowList.add(mow.clone());
				}
			}
		}
		
		BattleMap.eventBus.addMapObjects(mowList, new MTAsyncCallback<Void>("add map objects failed"));
	}

	public static EditorPanel getOpenedPanel() {
		return openedPanel;
	}

}
