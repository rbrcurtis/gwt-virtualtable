package com.mut8ed.battlemap.shared.dto.charactersheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Enhancer implements Serializable {
	private static final long serialVersionUID = 1L;


	protected String flavorText;
	protected String name;
	List<Modifier> modifiers = new ArrayList<Modifier>();

	public Enhancer(){}

	public Enhancer(Modifier...mods){
		for (Modifier mod : mods){
			mod.setSource(this);
			modifiers.add(mod);
		}
	}

	public String getFlavorText() {
		return flavorText;
	}

	public List<Modifier> getModifiers() {
		return modifiers;
	}

	public String getName() {
		return name;
	}

	public void setFlavorText(String flavorText) {
		this.flavorText = flavorText;
	}
	
	public void setModifiers(List<Modifier> modifiers) {
		this.modifiers = modifiers;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
