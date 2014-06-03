package com.mut8ed.battlemap.client.widget.charactersheet;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.Modifier;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.CombatStatType;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.SavingThrow;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Skill;

@SuppressWarnings("rawtypes")
public class ModifiersPanel extends FlexTable {

	private CharacterSheet cs;
	private static int columns = 6;
	
	//TODO add kb shortcuts for navigating around the table

	public ModifiersPanel(final CharacterSheet cs) {
		this.cs = cs;

		this.setStyleName("modifiersPanel");

		setHTML(0, 0, "<b>Modifiers</b>");
		
		
		setHTML(1, 0, "Type");
		for (int j = 1 ; j<=columns ; j++){
			TextBox typeBox = new TextBox();
			typeBox.setWidth("30px");
			if (("modPanel-type-"+j)!=null)typeBox.setValue(cs.getMisc("modPanel-type-"+j));
			setWidget(1, j, typeBox);
		}
		for (final Ability.Type type : Ability.Type.values()){
			handleType(type, null);
		}
		handleType(CombatStatType.BASEATTACK, "ATK");
		handleType(CombatStatType.DMG, null);
		handleType(CombatStatType.AC, null);
		handleType(SavingThrow.Type.FORT, null);
		handleType(SavingThrow.Type.REFLEX, null);
		handleType(SavingThrow.Type.WILL, null);
		
		int skillRow = this.getRowCount();
		this.setHTML(skillRow, 0, "SKILLS");
		for (int j = 1 ; j<=columns ; j++){
			final TextBox box = new TextBox();
			
			final String modId = "modPanel-"+j+"-skills";
			Modifier m = cs.getModifier(modId);
			final Modifier mod = (m!=null) ? m : new Modifier(modId, 0, "unnamed", Skill.Type.values());
//			if (m)cs.addModifier(mod);
			
			if (mod.getAmount()!=0)box.setValue(mod.getAmount()+"");
			
			box.addChangeHandler(new ChangeHandler() {
				
				@Override
				public void onChange(ChangeEvent event) {
					try {
						mod.setAmount(Integer.parseInt(box.getValue()));
					} catch (NumberFormatException e) {
						mod.setAmount(0);
					}
					if (mod.getAmount()==0){
						cs.removeModifier(modId);
					} else {
						cs.addModifier(mod);
					}
					CharacterView.getInstance().save();
				}
			});

			setWidget(skillRow, j, box);

		}
		//skills

		cs.reapplyModifiers();
	}




	private void handleType(final Enum type, String name) {

		int i = this.getRowCount();
		
		this.setHTML(i, 0, (name!=null)?name:type.name());

		
		for (int j = 1 ; j<=columns ; j++){
			final TextBox box = new TextBox();
			
			final String modId = "modPanel-"+j+"-"+type.name();
			Modifier m = cs.getModifier(modId);
			final Modifier mod = (m!=null) ? m : new Modifier(modId, 0, "unnamed", type);
			if (m==null)cs.addModifier(mod);
			
			if (mod.getAmount()!=0)box.setValue(mod.getAmount()+"");
			

			box.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					try {
						mod.setAmount(Integer.parseInt(box.getValue()));
					} catch (NumberFormatException e) {
						mod.setAmount(0);
					}
					if (mod.getAmount()==0){
						cs.removeModifier(modId);
					} else {
						cs.addModifier(mod);
					}
					CharacterView.getInstance().save();
				}
			});
			setWidget(i, j, box);
		}
	}

}
