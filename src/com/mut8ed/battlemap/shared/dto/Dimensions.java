package com.mut8ed.battlemap.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Dimensions implements IsSerializable {

	private Integer depth; //UNUSED
	private Integer height;
	private Integer width;
	
	public Dimensions(Integer depth, Integer height, Integer width) {
		super();
		this.depth = depth;
		this.height = height;
		this.width = width;
	}

	public Integer getDepth() {
		return depth;
	}
	
	public Integer getHeight() {
		return height;
	}
	
	public Integer getWidth() {
		return width;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
}
