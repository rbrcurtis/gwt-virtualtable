package com.mut8ed.battlemap.shared.dto;

import java.util.ArrayList;
import java.util.List;

import com.mut8ed.battlemap.shared.Defaults;
import com.mut8ed.battlemap.shared.MapObjectType;

/**
 * map object for map decals
 * @author ryan
 *
 */
public class Decal extends MapObject {
	private static final long serialVersionUID = 1L;
	
	private Integer height;
	private String imageUrl;
	private List<String> tags = new ArrayList<String>();
	private Integer width;

	public Decal(){	
		super();
	}

	public Decal(
			Integer height, 
			String id,
			String imagePath,
			Integer width
	) {
		super();
		this.height = height;
		this.id = id;
		this.imageUrl = imagePath;
		this.width = width;
	}

	public Decal(Integer height, String id,
			String imagePath, Integer width, List<String> tags) {
		this(height, id, imagePath, width);
		this.tags = tags;
	}

	public Decal(String imagePath, int height, int width,
			List<String> tags) {
		this.imageUrl = imagePath;
		this.height = height;
		this.width = width;
		this.tags = tags;
	}

	public void addTag(String tag){
		tags.add(tag);
	}

	@Override
	public MapObject clone() {
		return new Decal(height, id, imageUrl, width, tags);
	}

	public Integer getHeight() {
		return height;
	}

	public String getImageUrl() {
		return Defaults.IMGURL+imageUrl;
	}

	public List<String> getTags(){
		return tags;
	}

	public Integer getWidth() {
		return width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setImageUrl(String imagePath) {
		this.imageUrl = imagePath;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}	

	@Override
	public MapObjectType getMapObjectType() {
		return MapObjectType.DECAL;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
