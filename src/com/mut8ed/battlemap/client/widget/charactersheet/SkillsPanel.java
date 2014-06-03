package com.mut8ed.battlemap.client.widget.charactersheet;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.MaxSkillPoints;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Misc;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Skill;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Skill.Type;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;

public class SkillsPanel extends FlexTable {

	//	private Score strength;
	//	private Score dexterity;
	//	private Score constitution;
	//	private Score intelligence;
	//	private Score wisdom;
	//	private Score charisma;

	public SkillsPanel(CharacterSheet cs){
		super();
		
		setStyleName("skillsPanel");

		setHTML(0, 0, "<b>Skills</b>");

		final MaxSkillPoints msp = (MaxSkillPoints)cs.getScore(Misc.MAX_SKILLS); 
		final Label max = new Label("max:"+msp.getAdjusted());
		msp.addWatcher(new Watcher() {
			
			@Override
			public void onChange() {
				max.setText("max:"+msp.getAdjusted());
			}
		});
		setWidget(0, 1, max);
		
		final Label current = new Label();
		setWidget(0, 2, current);
		
		this.getColumnFormatter().addStyleName(0, "skillName");
		setHTML(1, 0, "Name");
		setHTML(1, 1, "base");
		setHTML(1, 2, "stat");
		setHTML(1, 3, "other");
		setHTML(1, 4, "total");

		Map<Skill.Type,Skill> stats = cs.getSkills();
		int i = 2;
		final int[] total = {0};
		for (Entry<Type, Skill> e : stats.entrySet()){

			final Skill skill = e.getValue();

			setHTML(i, 0, e.getKey()+"");
			//skill points

			final TextBox base = new TextBox();
			base.setStyleName("skillBox");
			base.setValue(""+skill.getBase());
			setWidget(i, 1, base);
			
			total[0] += skill.getBase();

			//stat bonus
			final TextBox abilityMod = new TextBox();
			abilityMod.setStyleName("skillBox");
			final Ability ability = skill.getAbilityScore();
			abilityMod.setValue(""+ability.getAbilityMod());
			abilityMod.setEnabled(false);
			setWidget(i, 2, abilityMod);
			ability.addWatcher(new Watcher() {

				@Override
				public void onChange() {
					abilityMod.setValue(ability.getAbilityMod()+"");
				}
			});
			
			//other bonuses
			final TextBox others = new TextBox();
			others.setStyleName("skillBox");
			others.setValue(""+(skill.getAdjusted()-ability.getAbilityMod()-skill.getBase()));
			others.setEnabled(false);
			setWidget(i, 3, others);

			//adjusted skill
			final TextBox mod = new TextBox();
			mod.setStyleName("skillBox");
			mod.setValue(""+skill.getAdjusted());
			mod.setEnabled(false);
			setWidget(i, 4, mod);



			base.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					int prev = skill.getBase();
					//BattleMap.debug(skill.getType()+" changed to "+base.getValue());
					skill.setBase(Integer.parseInt(base.getValue()));
					int n = skill.getBase();
					CharacterView.getInstance().save();
					total[0]+=(n-prev);
					current.setText("total:"+total[0]);
				}
			});

			skill.addWatcher(new Watcher() {

				@Override
				public void onChange() {
					base.setValue(""+skill.getBase());
					mod.setValue(""+skill.getAdjusted());
					others.setValue(""+(skill.getAdjusted()-ability.getAbilityMod()-skill.getBase()));
				}
			});

			i++;
		}
		current.setText("total:"+total[0]);
	}

}
