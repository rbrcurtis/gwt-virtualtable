package com.mut8ed.battlemap.shared.dto;

import java.util.ArrayList;
import java.util.List;

import com.mut8ed.battlemap.shared.Defaults;
import com.mut8ed.battlemap.shared.MapObjectType;

/**
 * map object for map tiles
 * @author ryan
 *
 */
public class Tile extends MapObject {
	private static final long serialVersionUID = 1L;
	
	private Integer height;
	private String imageUrl;
	private List<String> tags = new ArrayList<String>();
	private Integer width;

	public Tile(){	
		super();
	}

	public Tile(
			Integer height, 
			String id,
			String imagePath,
			Integer width
	) {
		super();
		this.height = height;
		this.id = id;
		this.width = width;
		this.imageUrl = imagePath;
	}


	public Tile(Integer height, String id,
			String imagePath, Integer width, List<String> tags) {
		this(height, id, imagePath, width);
		this.tags = tags;
	}

	public Tile(String imagePath, int height, int width,
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
		return new Tile(height, id, imageUrl, width, tags);
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

	public boolean hasTag(String tag) {
		return tags.contains(tag);
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setImageUrl(String imagePath) {
		this.imageUrl = imagePath;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setTags(List<String> tags){
		this.tags = tags;
	}

	@Override
	public MapObjectType getMapObjectType() {
		return MapObjectType.TILE;
	}

	@Override
	public String toString() {
		return "Tile [imageUrl=" + imageUrl + ", tags=" + tags + "]";
	}

}
