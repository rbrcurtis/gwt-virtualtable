package com.mut8ed.battlemap.client.widget.charactersheet;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability.Type;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;

public class StatsPanel extends FlexTable {

//	private Score strength;
//	private Score dexterity;
//	private Score constitution;
//	private Score intelligence;
//	private Score wisdom;
//	private Score charisma;

	public StatsPanel(CharacterSheet cs){
		
		setStylePrimaryName("statsPanel");
		
		Map<Ability.Type,Ability> stats = cs.getAbilityScores();
		int i = 0;
		for (Entry<Type, Ability> e : stats.entrySet()){
			
			final Ability ability = e.getValue();
			
			setHTML(i, 0, e.getKey()+"");
			final TextBox base = new TextBox();
			base.setStyleName("abilityScore");
			base.setValue(""+ability.getBase());
			setWidget(i, 1, base);
			
			final TextBox adjusted = new TextBox();
			adjusted.setStyleName("abilityScore");
			adjusted.setValue(""+e.getValue().getAdjusted());
			adjusted.setEnabled(false);
			setWidget(i, 2, adjusted);
			
			final TextBox mod = new TextBox();
			mod.setStyleName("abilityScore");
			mod.setValue(""+ability.getAbilityMod());
			mod.setEnabled(false);
			setWidget(i, 3, mod);
			
			
			
			base.addChangeHandler(new ChangeHandler() {
				
				@Override
				public void onChange(ChangeEvent event) {
					//BattleMap.debug(ability.getType()+" changed to "+base.getValue());
					ability.setBase(Integer.parseInt(base.getValue()));
					CharacterView.getInstance().save();
				}
			});
			
			ability.addWatcher(new Watcher() {
				
				@Override
				public void onChange() {
					adjusted.setValue(ability.getAdjusted()+"");
					mod.setValue(ability.getAbilityMod()+"");
				}
			});
			
			i++;
		}
	}
	
}
