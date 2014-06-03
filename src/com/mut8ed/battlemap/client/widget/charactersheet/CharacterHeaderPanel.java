package com.mut8ed.battlemap.client.widget.charactersheet;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.Race;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;

public class CharacterHeaderPanel extends FlexTable {

	private CheckBox isTemplate;
	private HorizontalPanel figureBox;
	private CharacterSheet cs;

	public CharacterHeaderPanel(CharacterSheet cs) {

		this.cs = cs;
		
		setStylePrimaryName("characterHeader");
		this.getRowFormatter().setVerticalAlign(0, HasAlignment.ALIGN_BOTTOM);
		this.getRowFormatter().setVerticalAlign(1, HasAlignment.ALIGN_TOP);
		
		build();
		
		
	}

	public void build() {
		
		clear();
		
		setWidget(0,0,new Label("Name"));
		final TextBox name = new TextBox();
		name.setStyleName("characterNameBox");
		name.setMaxLength(40);
		name.setValue(cs.getCharacterName());
		name.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				cs.setCharacterName(name.getValue());
				CharacterView.getInstance().save();
			}
		});
		setWidget(1,0,name);
		
		setWidget(0,2,new Label("Race"));
		final ListBox race = new ListBox();
		race.setStylePrimaryName("RaceBox");
		
		int i = 0;
		for (Race c : Race.values()){
			race.addItem(c.name().toLowerCase());
			if (c==cs.getRace())race.setSelectedIndex(i);
			i++;
		}
		
		race.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				try {
					Race rc = Race.valueOf(race.getItemText(race.getSelectedIndex()).toUpperCase());
					cs.setRace(rc);
				} catch (Exception e) {
					//BattleMap.debug(e.getMessage(),e);
				}
				CharacterView.getInstance().save();
			}
		});
		setWidget(1,2,race);

		Label label = new Label("Character Class");
		setWidget(0,4, label);
		final Button anchor = new Button(cs.getClassSummary());
		anchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				new CharacterClassPanel(cs).center();
			}
		});
		setWidget(1,4, anchor);
		cs.addWatcher(new Watcher() {
			
			@Override
			public void onChange() {
				anchor.setText(cs.getClassSummary());
			}
		});
		
		
//		figureBox = new HorizontalPanel();
//		figureBox.setStylePrimaryName("csFigureBox");
//		figureBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//		figureBox.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//		
//		final Image figureImg = new Image();
//		figureImg.setAltText("Click to change figure");
//		Figure figure = cs.getFigure();
//		if (figure!=null){
//			figureImg.setUrl(figure.getStanceUrl("SW"));	
//		}
//		
//		figureBox.addDomHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				FigureSlider fs = FigureSlider.getInstance(new FigureSlider.FigureClickHandler() {
//					
//					@Override
//					public void figureClicked(Image image, Figure figure) {
//						figureImg.setUrl(image.getUrl());
//						cs.setFigure((Figure)figure.clone());
//						CharacterView.getInstance().save();
//					}
//				});
//				if (!fs.isAttached())BattleMap.add(fs);
//				EditorPanel.toggle(fs);
//				fs.setWidthAndHeight();
//			}
//		}, ClickEvent.getType());
//		
//		figureBox.setVisible(false);
//		
//		setWidget(0,8,figureBox);
//		
//		figureBox.add(figureImg);
//		getFlexCellFormatter().setRowSpan(0, 8, 2);
		
//		isTemplate = new CheckBox("Template");
//		isTemplate.setTitle("Template Characters will be copied when added to the map, " +
//				"and multiple can be on the map at once. A non-template " +
//				"character is not copied when added but is a singular character. " +
//				"IE, guards vs the captain of the guard.");
//		
//		isTemplate.setValue(cs.isTemplate());
//		isTemplate.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//			
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				cs.isTemplate(isTemplate.getValue());
//				CharacterView.getInstance().save();
//			}
//		});
//		
//		isTemplate.setVisible(false);
//		
//		setWidget(1,0,isTemplate);
//		getFlexCellFormatter().setColSpan(1, 0, 2);
	}

	public void showTemplateFlag(boolean show) {
		if (show && isTemplate!=null){
			isTemplate.setVisible(show);
			figureBox.setVisible(show);
		}
	}

}
