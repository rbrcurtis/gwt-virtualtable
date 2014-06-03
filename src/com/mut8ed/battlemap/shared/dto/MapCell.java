package com.mut8ed.battlemap.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MapCell implements IsSerializable {

	public Integer col = null;
	public Integer row = null;
	public Integer layer = null;

	public MapCell(){}

	public MapCell(int col, int row, int layer){
		this.col = col;
		this.row = row;
		this.layer = layer;
	}
	
	@Override
	public String toString(){
		return col+"x"+row+"x"+layer;
	}

}