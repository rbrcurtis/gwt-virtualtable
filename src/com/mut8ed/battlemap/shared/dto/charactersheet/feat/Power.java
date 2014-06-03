package com.mut8ed.battlemap.shared.dto.charactersheet.feat;

import java.io.Serializable;

import com.mut8ed.battlemap.shared.dto.charactersheet.Enhancer;

public class Power extends Enhancer implements Serializable {
	private static final long serialVersionUID = 1L;

	
	public Power(){}
	
	public Power(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Power [name=" + name + "]";
	}
}
