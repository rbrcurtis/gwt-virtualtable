package com.mut8ed.battlemap.shared.dto.charactersheet;

import java.io.Serializable;

import com.mut8ed.battlemap.shared.dto.charactersheet.score.Score;

@SuppressWarnings("rawtypes")
public class Modifier implements Serializable {
	private static final long serialVersionUID = 1L;

	private int amount;
	private String modId = null;
	private Score score = null;
	private Enhancer source;
	private Enum[] targetTypes;
	private String type;

	public Modifier(){}

	public Modifier(String modId, int amount, String type, Enhancer source, Enum... targetTypes) {
		this(modId, amount, type, targetTypes);
		this.source = source;
	}

	public Modifier(String modId, int amount, String type, Enum... targetTypes) {
		this.modId = modId;
		this.targetTypes = targetTypes;
		this.amount = amount;
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public String getId() {
		if (modId==null)modId = "MOD-"+(Math.random()*100000);
		return modId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Modifier other = (Modifier) obj;
		if (getId() == null) {
			if (other.getModId() != null)
				return false;
		} else if (!getModId().equals(other.getModId()))
			return false;
		return true;
	}

	public String getModId() {
		return modId;
	}

	public Score getScore() {
		return score;
	}

	public Enhancer getSource() {
		return source;
	}

	public Enum[] getTargetTypes() {
		return targetTypes;
	}

	public String getType() {
		return type;
	}

	public void setAmount(int amount) {
		this.amount = amount;
		if (score!=null)score.informWatchers();
	}
	
	public void setModId(String modId) {
		this.modId = modId;
	}
	
	public void setScore(Score score){
		this.score = score;
	}

//	public void setTargetType(Enum targetType) {
//		this.targetType = targetType;
//	}
	
	public void setSource(Enhancer source) {
		this.source = source;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Modifier [amount=" + amount
				+ ", source=" + source + ", type=" + type + "]";
	}
	
	
}