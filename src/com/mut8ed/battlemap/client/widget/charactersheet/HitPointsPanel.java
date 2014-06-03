package com.mut8ed.battlemap.client.widget.charactersheet;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;

public class HitPointsPanel extends FlexTable {

	private CharacterSheet cs;
	//	private CharacterView cv;
	private TextBox current;
	private TextBox perc;
	private int conMod;
	private Ability con;

	public HitPointsPanel(final CharacterSheet cs) {
		this.cs = cs;

		setStylePrimaryName("hitPointsPanel");


		/** total HP **/
		setHTML(0, 0, "<b>HP</b>");
		getFlexCellFormatter().setColSpan(0, 0, 2);
		final TextBox total = new TextBox();
		total.setTitle("Total HP");
		//		total.setEnabled(false);
//		total.setReadOnly(true);
		total.setValue(cs.getHp()+"");
		setWidget(1, 0, total);
		getFlexCellFormatter().setColSpan(1, 0, 2);


		/** temp **/
		setHTML(2, 0, "temp");
		final TextBox temp = new TextBox();
		temp.setTitle("Temporary hitpoints");
		temp.setValue(cs.getTempHp()+"");
		setWidget(2, 1, temp);

		/** current HP **/
		setHTML(3, 0, "current");
		current = new TextBox();
		current.setTitle("Current HP");
		current.setEnabled(false);
		setWidget(3, 1, current);

		/** percentage **/
		setHTML(4, 0, "%");
		current.setTitle("Current percentage");
		perc = new TextBox();
		perc.setEnabled(false);
		setWidget(4, 1, perc);

		/** DMG **/
		setHTML(5, 0, "DMG");
		final TextBox adjust = new TextBox();
		adjust.setTitle("Current damage. Use +<dmg amount> or -<heal amount> to auto-calculate damage or healing");
		adjust.setValue(cs.getDmg()+"");
		setWidget(5, 1, adjust);

		computeCurrent();

//		total.addDoubleClickHandler(new DoubleClickHandler() {
//
//			@Override
//			public void onDoubleClick(DoubleClickEvent event) {
//				total.setReadOnly(false);
//				total.setFocus(true);
//				total.selectAll();
//			}
//		});
		
		total.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				cs.setHp(Integer.parseInt(total.getValue()));
				computeCurrent();
//				total.setReadOnly(true);
				CharacterView.getInstance().save();
			}
		});

		temp.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				try {
					cs.setTempHp(Integer.parseInt(temp.getValue()));
					int currentHp = cs.getHp()-cs.getDmg()+cs.getTempHp();
					current.setValue(currentHp+"");
					CharacterView.getInstance().save();
				} catch (NumberFormatException e) {
					if (temp.getValue().equals("")){
						temp.setValue("0");
						cs.setTempHp(0);
						CharacterView.getInstance().save();
					} else {
						temp.setValue(cs.getTempHp()+"");
					}
				}
			}
		});

		adjust.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				adjust.selectAll();
			}
		});

		adjust.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				try {
					String adj = adjust.getValue();
					if (adj.matches("[0-9]+")){

						cs.setDmg(Integer.parseInt(adj));
						computeCurrent();

					} else if (adj.matches("(\\+|-)[0-9]+")){

						int ch = Integer.parseInt(adj.replaceAll("[^0-9]", ""));
						if (adj.startsWith("-"))ch*=-1;
						if (!temp.getValue().equals("0") && ch>0){
							int t = Integer.parseInt(temp.getValue());
							ch -= t;
							if (ch<0){
								t = ch*-1;
								temp.setValue(t+"");
								cs.setTempHp(t);
								adjust.setValue(cs.getDmg()+"");
								computeCurrent();
								return;
							} else {
								cs.setTempHp(0);
								temp.setValue("0");
							}
						}
						int dmg = cs.getDmg()+ch;
						if (dmg<0)dmg = 0;
						cs.setDmg(dmg);
						adjust.setValue(cs.getDmg()+"");
						computeCurrent();

					} 
				} finally {
					CharacterView.getInstance().save();
				}
			}

		});
		
		con = cs.getAbility(Ability.Type.CON);
		conMod = con.getAbilityMod();
		con.addWatcher(new Watcher() {
			
			@Override
			public void onChange() {
				//BattleMap.debug("con changing from "+conMod+" to "+con.getAbilityMod());
				if (con.getAbilityMod()!=conMod){
					int change = con.getAbilityMod()-conMod;
					cs.setHp(cs.getHp()+(change*cs.getLevel()));
					computeCurrent();
					conMod = con.getAbilityMod();
				}
			}
		});
		

	}

	private void computeCurrent() {
		int currentHp = cs.getHp()-cs.getDmg()+cs.getTempHp();
		current.setValue(currentHp+"");
		int percentage = (int)((currentHp*1.0)/cs.getHp()*100);
		perc.setText(percentage+"%");
	}



}
