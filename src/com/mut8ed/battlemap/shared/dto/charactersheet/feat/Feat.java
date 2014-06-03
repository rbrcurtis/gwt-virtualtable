package com.mut8ed.battlemap.shared.dto.charactersheet.feat;

public class Feat extends Power implements Comparable<Feat> {
	private static final long serialVersionUID = 1L;

	private String type;
	private int level;
	
	public Feat(){}
	
	public Feat(int level, String type, String name) {
		this.level = level;
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Feat [level=" + level + ", type=" + type + ", name=" + name + "]";
	}

	public int getLevel() {
		return level;
	}

	@Override
	public int compareTo(Feat left) {
		int l = level - left.getLevel();
		if (l!=0)return l;
		return type.compareTo(left.getType());
	}

}
