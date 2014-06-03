package com.mut8ed.battlemap.shared.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.mut8ed.battlemap.shared.Defaults;
import com.mut8ed.battlemap.shared.MapObjectType;

public class Figure extends MapObject {
	private static final long serialVersionUID = 1L;

	private int height = 1;
	private String name;
	private Map<String, String> stances = new TreeMap<String, String>();
	private List<String> tags = new ArrayList<String>();
	private int width = 1;

	public Figure(){
		super();
	}

	public Figure(String id, String name,
			Map<String, String> stances, List<String> tags) {
		super();
		this.id = id;
		this.name = name;
		this.stances = stances;
		this.tags = tags;
	}

	public Figure(String id, String name) {
		super();
		this.id = id;
		this.name = name;
		
	}

	public void addStance(String type, String path){
		if (type!=null && path!=null && !stances.containsKey(type))stances.put(type,path);
	}
	
	public void addTag(String tag){
		if (tag==null)return;
		tag = tag.trim();
		if (!tags.contains(tag)){
			tags.add(tag);
		}
	}

	@Override
	public MapObject clone() {
		return new Figure(id, name, stances, tags);
	}
	
	public int getHeight() {
		return height;
	}
	
	public String getName() {
		return name;
	}
	
	public Image getStance(String stance) {
		String url = stances.get(stance);
		final Image image = new Image(Defaults.IMGURL+url);
		image.getElement().setId(getElementId());
		return image;
	}

	public Map<String, String> getStances() {
		return stances;
	}

	public String getStanceUrl(String stance) {
		if (stance==null)return null;
		String path = stances.get(stance);
		//try for alternate stance.  this needs to be improved because missing SW could give SE which is the wrong direction
		if (path==null){
			if (stance.contains("-")){
				stance = stance.split("-")[0];
			} else if (stance.length()==2){
				stance = stance.substring(0,1);
			}
			for (String s : stances.keySet()){
				if ((stance.length()==1 && s.length()==2) || stance.length()>2){
					if (s.contains(stance)){
						path = stances.get(s);
						GWT.log("defaulted to "+s);
						break;
					}
				}
			}
		}
		if (path==null)return null;
		
		return Defaults.IMGURL+path;
	}

	public List<String> getTags(){
		return tags;
	}

	public int getWidth() {
		return width ;
	}

	public boolean hasTag(String category) {
		if (category==null)return false;
		return tags.contains(category);
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStances(Map<String, String> stances) {
		this.stances = stances;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public String toString(){
		return name+":"+id+":"+stances.size();
	}

	@Override
	public MapObjectType getMapObjectType() {
		return MapObjectType.FIGURE;
	}

}