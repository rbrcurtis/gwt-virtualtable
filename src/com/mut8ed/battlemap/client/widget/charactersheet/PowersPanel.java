package com.mut8ed.battlemap.client.widget.charactersheet;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.feat.Power;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;

public class PowersPanel extends VerticalPanel {

	private CharacterSheet cs;

	public PowersPanel(CharacterSheet cs) {
		this.cs = cs;

		setStylePrimaryName("powersPanel");

		build();

		cs.addWatcher(new Watcher() {

			@Override
			public void onChange() {
				build();
			}
		});
	}

	private void build() {
		clear();
		add(new HTML("<b>Powers</b>"));

		for (final Power power : cs.getRacePowers().values()){
			addPower("race", power);
		}
		add(new HTML("<hr/>"));

		for (CharacterClassModel ccm : cs.getClassModels()){
			for (final Power power : ccm.getClassPowers().values()){
				addPower("class", power);
			}
			add(new HTML("<hr/>"));
		}

		for (final Power power : cs.getExtraPowers()){
			addPower("extra", power);
		}

		addEmptyPower();
	}

	private void addPower(String type, final Power power) {
		//BattleMap.debug("adding power "+power.getName());
		final TextBox powerBox = new TextBox();
		powerBox.setStylePrimaryName("powerBox");
		powerBox.addStyleName(type+"Power");
		powerBox.setText(power.getName());

		powerBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				power.setName(powerBox.getValue());
				CharacterView.getInstance().save();
			}
		});

		add(powerBox);
	}

	private TextBox addEmptyPower() {
		final TextBox powerBox = new TextBox();
		powerBox.setStylePrimaryName("powerBox");
		powerBox.addStyleName("extraPower");

		powerBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				Power power = new Power();
				power.setName(powerBox.getValue());
				cs.addExtraPower(power);
				addEmptyPower().setFocus(true);
				CharacterView.getInstance().save();
			}
		});

		add(powerBox);
		return powerBox;

	}




}

