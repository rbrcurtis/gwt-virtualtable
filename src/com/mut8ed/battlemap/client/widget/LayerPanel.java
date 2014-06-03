package com.mut8ed.battlemap.client.widget;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.util.MapEditorMouseHandler;
import com.mut8ed.battlemap.client.view.MapView;

public class LayerPanel extends VerticalPanel {

	public enum LayerColor {
		SELECTED,NONE,OCCUPIED;

		public String getColorValue(){
			switch (this){
			case SELECTED: return "#E89E54";
			case OCCUPIED: return "#AAFAAF";
			default:return "white";
			}
		}
	}

	public enum LayerTextColor {
		VISIBLE,INVISIBLE;

		public String getColorValue(){
			switch (this){
			case VISIBLE: return "BLACK";
			case INVISIBLE: return "GREY";
			default: return "RED";
			}
		}

	}

	public enum LayerBorder {
		SELECTED,NONE;

		public String getColorValue(){
			switch (this){
			case SELECTED: return "red";
			default:return "black";
			}
		}
	}

	private int currentLayerIndex = 0;
	private int highestLayer;
	private int lowestLayer;
	private Map<Integer, Integer> layerObjectCounts = new HashMap<Integer, Integer>();
	private static LayerPanel instance;


	private LayerPanel(){
		getElement().setId("layerPanel");
		clear();
	}


	public static LayerPanel getInstance(){
		if (instance==null)instance = new LayerPanel();
		return instance;
	}

	public void incrementObjectCount(int z){
		while (z>highestLayer){
			addLayerButton(highestLayer+1);
		}
		while (z<lowestLayer){
			addLayerButton(lowestLayer-1);
		}

		Integer layerCount = layerObjectCounts.get(z);
		if (layerCount==null)layerCount = 0;
		layerCount++;
		if (layerCount==1){
			setLayerColor(LayerColor.OCCUPIED, z);
		}
		layerObjectCounts.put(z, layerCount);
	}

	private void removeLayerButton(int layerIndex) {
		BattleMap.debug("remove layer "+layerIndex);
		int removeIndex = -1;
		for (int i = 0;i<this.getWidgetCount();i++){
			Anchor a = (Anchor) this.getWidget(i);
			int aLayer = Integer.parseInt(a.getHTML());
			if (aLayer==layerIndex){
				removeIndex = i;
				break;
			}
		}
		if (removeIndex>=0){
			this.remove(removeIndex);
			if (layerIndex==lowestLayer)lowestLayer++;
			if (layerIndex==highestLayer)highestLayer--;
		}
	}

	public void addLayerButton(int insertLayer){
		BattleMap.debug("add layer "+insertLayer);
		if (insertLayer>highestLayer){
			BattleMap.debug("highest layer changed "+insertLayer);
			highestLayer = insertLayer;
		}
		if (insertLayer<lowestLayer){
			BattleMap.debug("lowest layer changed "+insertLayer);
			lowestLayer = insertLayer;
		}
		//find the index at which the new layer is being inserted
		int insertIndex = 0;
		for (int i = 0;i<this.getWidgetCount();i++){
			Anchor a = (Anchor) this.getWidget(i);
			int aLayer = Integer.parseInt(a.getHTML());
			if (aLayer>insertLayer)insertIndex = i+1;
			else if (aLayer==insertLayer)return;
			else if (aLayer<insertLayer)break;
		}
		final Anchor lb = new Anchor(insertLayer+"");
		lb.setStylePrimaryName("toolButton");

		lb.addClickHandler(
				new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						int layer = Integer.parseInt(lb.getHTML());
						MapView.getInstance().switchLayer(layer);
						event.stopPropagation();
						event.preventDefault();
					}
				}
				);

		lb.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				MapEditorMouseHandler.getInstance().setEnabled(false);
				BattleMap.debug("add object disabled");
			}
		});
		lb.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				MapEditorMouseHandler.getInstance().setEnabled(true);
				BattleMap.debug("add object enabled");
			}
		});


		String color = LayerTextColor.VISIBLE.getColorValue();
		lb.getElement().getStyle().setColor(color);

		if (insertIndex>=this.getWidgetCount()){
			this.add(lb);	
		} else {
			this.insert(lb, insertIndex);
		}
	}


	public void switchLayer(int newLayerIndex) {
		BattleMap.debug("highest layer "+highestLayer);
		BattleMap.debug("lowest layer "+lowestLayer);

		int prevLayerIndex = currentLayerIndex;

		setLayerBorder(LayerBorder.NONE,prevLayerIndex);

		if (layerObjectCounts.get(prevLayerIndex)==null){
			if (prevLayerIndex==highestLayer-1 && prevLayerIndex>newLayerIndex)removeLayerButton(highestLayer);
			if (prevLayerIndex==lowestLayer+1 && prevLayerIndex<newLayerIndex)removeLayerButton(lowestLayer);
		}

		addLayerButton(newLayerIndex);
		if (prevLayerIndex>newLayerIndex)addLayerButton(newLayerIndex-1);
		else addLayerButton(newLayerIndex+1);
		setLayerBorder(LayerBorder.SELECTED,newLayerIndex);

		currentLayerIndex = newLayerIndex;
	}

	public void setLayerVisible(int layer, boolean visible) {
		String color = (visible)?LayerTextColor.VISIBLE.getColorValue():LayerTextColor.INVISIBLE.getColorValue();
		for (int i = 0;i<this.getWidgetCount();i++){
			Anchor a = (Anchor) this.getWidget(i);
			int aLayer = Integer.parseInt(a.getHTML());
			if (aLayer==layer){
				a.getElement().getStyle().setColor(color);
				return;
			}
		}
		//		throw new RuntimeException("I shouldn't be able to get here");		
	}

	public Integer getCurrentLayerIndex() {
		return currentLayerIndex;
	}

	public int getHighestLayer() {
		return highestLayer;
	}

	public void decrementObjectCount(int z){
		Integer lc = layerObjectCounts.get(z);
		if (lc!=null){
			lc--;
			BattleMap.debug("new lc "+lc);
			if (lc==0){
				layerObjectCounts.remove(z);
				setLayerColor(LayerColor.NONE,z);

			} else {
				layerObjectCounts.put(z, lc);
			}
		}
	}

	private void setLayerColor(LayerColor color, int z) {
		for (int i = 0;i<this.getWidgetCount();i++){
			Anchor a = (Anchor) this.getWidget(i);
			int aLayer = Integer.parseInt(a.getHTML());
			if (aLayer==z){
				a.getElement().getStyle().setBackgroundColor(color.getColorValue());
				return;
			}
		}
		//		throw new RuntimeException("I shouldn't be able to get here");

	}

	private void setLayerBorder(LayerBorder color, int z){
		BattleMap.debug("setLayerBorder:"+getWidgetCount());
		for (int i = 0;i<this.getWidgetCount();i++){
			Anchor a = (Anchor) this.getWidget(i);
			int aLayer = Integer.parseInt(a.getHTML());
			if (aLayer==z){
				a.getElement().getStyle().setBorderColor(color.getColorValue());
				return;
			}
		}
		//		throw new RuntimeException("I shouldn't be able to get here for layer "+z);
	}

	public int getLowestLayer() {
		return lowestLayer;
	}

	public Integer getObjectCountInLayer(int layer){
		return layerObjectCounts.get(layer);
	}


	@Override
	public void clear() {
		super.clear();
		addLayerButton(-1);
		addLayerButton(0);
		addLayerButton(1);
	}

}




















