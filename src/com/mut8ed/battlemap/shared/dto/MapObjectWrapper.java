package com.mut8ed.battlemap.shared.dto;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.mut8ed.battlemap.shared.MapObjectType;

public class MapObjectWrapper implements IsSerializable {

	private String id;
	private Date createDate;
	private Integer height;
	transient private String mapId;
	private String name;
	private MapObject obj;
	private Boolean visible;
	private Integer width;
	private Integer x;
	private Integer y;
	private Integer z;
	private Integer rotation;
	private String characterId;
	
	public MapObjectWrapper(){}
	
	public MapObjectWrapper(String id, Date createDate, Integer height,
			String mapId, String name,
			Boolean visible, Integer width, Integer x, Integer y, Integer z, Integer rotation) {
		super();
		this.id = id;
		this.createDate = createDate;
		this.height = height;
		this.mapId = mapId;
		this.name = name;
		this.visible = visible;
		this.width = width;
		this.x = x;
		this.y = y;
		this.z = z;
		this.rotation = rotation;
	}
	
	public Integer getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public Date getCreateDate() {
		if (createDate==null){
			createDate = new Date();
		}
		return createDate;
	}

	public Integer getHeight() {
		return height;
	}
	
	public String getId() {
		return id;
	}

	public String getMapId() {
		return mapId;
	}

	public MapObjectType getMapObjectType() {
		return obj.getMapObjectType();
	}

	public String getName() {
		return name;
	}

	public MapObject getMapObject() {
		return obj;
	}

	public String getObjectId(){
		return obj.getId();
	}

	public Boolean isVisible() {
		return visible;
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getX() {
		return x;
	}

	public Integer getY() {
		return y;
	}

	public Integer getZ() {
		return z;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setObj(MapObject obj) {
		this.obj = obj;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setX(Integer x) {
		this.x = x;
	}
	
	public void setY(Integer y) {
		this.y = y;
	}
	
	public void setZ(Integer z) {
		this.z = z;
	}
	
	@Override
	public String toString(){
		return id+":"+getMapObject().getElementId()+"/"+name+", "+x+"x"+y+"x"+z+", width="+width+", height="+height;
	}

	public MapObjectWrapper clone() {
		MapObjectWrapper mow = new MapObjectWrapper(
				null, 
				null,
				height, 
				null, 
				null, 
				true, 
				width, 
				x, 
				y,
				z,
				rotation
		);
		mow.setObj(obj.clone());
		return mow;

	}

	public String getElementId() {
		if (obj!=null)return obj.getElementId();
		return null;
	}

	public void setCharacterId(String characterId) {
		this.characterId = characterId;
	}

	public String getCharacterId(){
		return characterId;
	}
}
