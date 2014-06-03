package com.mut8ed.battlemap.client.widget.charactersheet;

import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.Modifier;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability.Type;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.SavingThrow;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;

public class SavingThrowPanel extends FlexTable {

	public SavingThrowPanel(CharacterSheet cs) {
		
		setStylePrimaryName("savingThrowPanel");

		int i = 0;
		for (SavingThrow.Type type : SavingThrow.Type.values()){
			
			final SavingThrow save = (SavingThrow)cs.getScore(type);
			
			setHTML(0, i, type.name());
			final TextBox adjusted = new TextBox();
			adjusted.setStyleName("savingThrow");
			adjusted.setValue(save.getAdjusted()+"");
			adjusted.setTitle("Total Current Saving Throw");
			DOM.setStyleAttribute(adjusted.getElement(), "fontWeight", "700");
			adjusted.setReadOnly(true);
			setWidget(1, i, adjusted);
			
			final TextBox base = new TextBox();
			base.setStyleName("savingThrow");
			base.setValue(save.getBase()+"");
			base.setTitle("Base Saving Throw");
			base.setReadOnly(true);
			setWidget(2, i, base);
			
			final TextBox mods = new TextBox();
			mods.setStyleName("savingThrow");
			mods.setValue((save.getAdjusted()-save.getBase())+"");
			mods.setTitle("Modifiers");
			mods.setReadOnly(true);
			setWidget(3, i, mods);
			
			Watcher watcher = new Watcher() {
				@Override
				public void onChange() {
					//BattleMap.debug("saving throw mods for "+save);
					for (Entry<String, List<Modifier>> mod : save.getModifiers().entrySet()){
						//BattleMap.debug(mod.getKey()+"="+mod.getValue());
					}
					for (Entry<Type, Ability> mod : save.getModdingAbilities().entrySet()){
						//BattleMap.debug(mod.getKey()+"="+mod.getValue());
					}
					adjusted.setValue(save.getAdjusted()+"");
					base.setValue(save.getBase()+"");
					mods.setValue((save.getAdjusted()-save.getBase())+"");
				}
			};
			
			save.addWatcher(watcher);
//			cs.addWatcher(watcher);
			
			i++;
		}
		
		
		
	}

}
