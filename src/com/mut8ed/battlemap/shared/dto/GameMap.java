package com.mut8ed.battlemap.shared.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GameMap implements IsSerializable {

	private int cellCountX = 50;
	private int cellCountY = 50;
	private Date createDate;
	private String createdBy;
	private String id;

	private Map<Integer, String> layerNames = new HashMap<Integer, String>();

	private Map<Integer, Boolean> layerVisibilities = new HashMap<Integer, Boolean>();

	private Map<String, MapObjectWrapper> mapObjects = new HashMap<String,MapObjectWrapper>();

	private String name;
	private int ownerId;

	public GameMap(){
		this.createDate = new Date();
	}
	
	public GameMap(int cellCountX, int cellCountY, String createdBy, Date createDate, String id, String name, int ownerId) {
		super();
		this.cellCountX = cellCountX;
		this.cellCountY = cellCountY;
		this.createdBy = createdBy;
		this.createDate = createDate;
		this.id = id;
		this.name = name;
		this.ownerId = ownerId;
	}
	/**
	 * assumes you have already checked to make sure there is no overlap.
	 * @param obj
	 */
	public void addMapObject(MapObjectWrapper obj){
		if (obj==null)return;
		mapObjects.put(obj.getMapObject().getElementId(), obj);
	}
	
	public int getCellCountX() {
		return cellCountX;
	}

	public int getCellCountY() {
		return cellCountY;
	}

	public Date getCreateDate() {
		return createDate;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public String getId() {
		return id;
	}

	public String getLayerName(int layer){
		return layerNames.get(layer);
	}

	public Map<Integer, String> getLayerNames() {
		return layerNames;
	}
	
	public Map<Integer, Boolean> getLayerVisibilities() {
		return layerVisibilities;
	}

	public Map<String,MapObjectWrapper> getMapObjects() {
		return mapObjects;
	}

	public String getName() {
		return name;
	}

	public int getOwnerId() {
		return ownerId;
	}
	
	public boolean isLayerVisible(int layer){
		Boolean vis = layerVisibilities.get(layer);
		if (vis==null){
			vis = true;//default true
			layerVisibilities.put(layer, vis);
		} 
		return vis;
	}

	public void load(GameMap map) {
		this.createDate = map.createDate;
		this.id = map.id;
		this.mapObjects = map.mapObjects;
		this.name = map.name;
		this.layerNames = map.layerNames;
		this.layerVisibilities = map.layerVisibilities;
		this.cellCountX = map.cellCountX;
		this.cellCountY = map.cellCountY;
	}

	public MapObjectWrapper remove(String elementId){
		MapObjectWrapper mow = mapObjects.remove(elementId);
		if (mow==null)return null;
		return mow;
	}

	public void setCellCountX(int cellCountX) {
		this.cellCountX = cellCountX;
	}
	
	public void setCellCountY(int cellCountY) {
		this.cellCountY = cellCountY;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLayerName(int layer, String name){
		layerNames.put(layer, name);
	}

	public void setLayerVisible(int layer, boolean visible) {
		layerVisibilities.put(layer, visible);
	}

	public void setMapObjects(Map<String,MapObjectWrapper> mapObjectWrappers) {
		this.mapObjects = mapObjectWrappers;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	@Override
	public String toString() {
		return "GameMap [id=" + id + ", name=" + name + "]";
	}

}