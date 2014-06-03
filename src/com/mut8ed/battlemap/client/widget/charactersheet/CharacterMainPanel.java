package com.mut8ed.battlemap.client.widget.charactersheet;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;

public class CharacterMainPanel extends FlowPanel {

//	private CharacterSheet cs;

	private FlowPanel header = new FlowPanel();
	private FlexTable sheet = new FlexTable();
	VerticalPanel left = new VerticalPanel();
	VerticalPanel right = new VerticalPanel();
	private FlexTable topLeft = new FlexTable();
	private HorizontalPanel middleLeft = new HorizontalPanel();
	private VerticalPanel bottomLeft = new VerticalPanel();
	private CharacterHeaderPanel characterHeader;


	public CharacterMainPanel(CharacterSheet cs) {
//		this.cs = cs;

		setStyleName("characterMainPanel");

		resetHeight();
		Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				resetHeight();
			}
		});

		FlexCellFormatter cf = sheet.getFlexCellFormatter();

		sheet.setWidget(0, 0, header);
		cf.setColSpan(0, 0, 2);

		sheet.setWidget(1, 0, left);
		sheet.setWidget(1, 1, right);
		sheet.getCellFormatter().setVerticalAlignment(1, 1, HasAlignment.ALIGN_TOP);

		left.add(topLeft);
		left.add(middleLeft);
		left.add(bottomLeft);

		FlowPanel shim = new FlowPanel();
		shim.setHeight("40px");
		sheet.setWidget(2, 0, shim);

		add(sheet);

		//BattleMap.debug("making call to get the character sheet");

		//BattleMap.debug("retrieved character");

		header.add(characterHeader = new CharacterHeaderPanel(cs));

		topLeft.setWidget(0, 0, new StatsPanel(cs));
		topLeft.getFlexCellFormatter().setRowSpan(0, 0, 2);
		topLeft.getFlexCellFormatter().setVerticalAlignment(0, 0, HasAlignment.ALIGN_TOP);

		topLeft.setWidget(0, 1, new HitPointsPanel(cs));

		topLeft.setWidget(0, 2, new SavingThrowPanel(cs));
		topLeft.getCellFormatter().setHorizontalAlignment(0, 1, HasAlignment.ALIGN_CENTER);

		topLeft.setWidget(1, 1, new CombatStatsPanel(cs));
		topLeft.getFlexCellFormatter().setColSpan(1, 1, 2);

		topLeft.removeCell(1, 0);

		middleLeft.add(new WeaponsPanel(cs));

		bottomLeft.add(new ModifiersPanel(cs));
		HorizontalPanel hp = new HorizontalPanel();
		bottomLeft.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		hp.add(new FeatsPanel(cs));
		hp.add(new PowersPanel(cs));
		bottomLeft.add(hp);

		right.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		right.add(new SkillsPanel(cs));

	}

	private void resetHeight() {
		setHeight((Window.getClientHeight()-DOM.getElementById("alerts").getClientHeight())+"px");		
	}

	public void showTemplateFlag(boolean show) {
		if (characterHeader==null)return;
		characterHeader.showTemplateFlag(show);
	}

}
