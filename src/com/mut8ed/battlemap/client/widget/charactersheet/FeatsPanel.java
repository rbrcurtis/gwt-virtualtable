package com.mut8ed.battlemap.client.widget.charactersheet;

import java.util.Collections;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.feat.Feat;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;

public class FeatsPanel extends VerticalPanel {

	private CharacterSheet cs;

	public FeatsPanel(CharacterSheet cs) {
		this.cs = cs;

		setStylePrimaryName("featsPanel");

		build();
		
		cs.addWatcher(new Watcher() {
			
			@Override
			public void onChange() {
				clear();
				build();
			}
		});
	}

	public void build() {
		add(new HTML("<b>Feats</b>"));
		List<Feat> feats = cs.getFeats();
		Collections.sort(feats);
		for (final Feat feat : feats){
			//BattleMap.debug("adding feat of type "+feat.getType());
			final TextBox featBox = new TextBox();
			featBox.setStylePrimaryName("featBox");
			if (feat.getType().contains("class"))featBox.addStyleName("classFeat");
			else featBox.addStyleName(feat.getType()+"Feat");
			featBox.setValue(feat.getName());

			featBox.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					feat.setName(featBox.getValue());
					CharacterView.getInstance().save();
				}
			});

			add(featBox);


		}
	}




}

