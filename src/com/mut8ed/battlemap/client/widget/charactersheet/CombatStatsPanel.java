package com.mut8ed.battlemap.client.widget.charactersheet;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.CombatStatType;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Score;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;

public class CombatStatsPanel extends FlexTable {
	
	public CombatStatsPanel(CharacterSheet cs){

		setStyleName("combatStats");
		
		int i = 0;
		int j = 0;
		for (CombatStatType type : CombatStatType.values()){
			
			if (i>=CombatStatType.values().length/2 && j==0){
				j+= 2;
				i = 0;
			}
			
			final Score score = cs.getScore(type);
			this.setHTML(i, j, type.name());
			final TextBox input = new TextBox();
			input.setStyleName("combatStatsBox");
			input.setValue(score.getAdjusted()+"");
			input.setEnabled(false);
			this.setWidget(i, j+1, input);
			
			score.addWatcher(new Watcher() {
				
				@Override
				public void onChange() {
					input.setValue(score.getAdjusted()+"");
				}
			});
			
			
			i++;
		}
		
	}

}
