package com.mut8ed.battlemap.client.widget;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.util.ClientUtil;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.client.widget.editor.DecalSlider;
import com.mut8ed.battlemap.client.widget.editor.EditorPanel;
import com.mut8ed.battlemap.client.widget.editor.FigureSlider;
import com.mut8ed.battlemap.client.widget.editor.TileSlider;
import com.mut8ed.battlemap.shared.MapObjectType;
import com.mut8ed.battlemap.shared.dto.MapObject;

public class TagPanel extends VerticalPanel implements KeyPressHandler {

	private MapObjectType type;
	private TextBox input;
	private Date changedAt;
	private EditorPanel slider;
	private final Timer timer = new Timer(){

		@Override
		public void run() {
			Date now = new Date();
			if (now.getTime()-changedAt.getTime()>300){

				if (input.getValue().equals("")){
					slider.resetList();
					
				} else {

					BattleMap.eventBus.getMapObjectsByTags(
							input.getValue(), 
							type, 
							new MTAsyncCallback<List<MapObject>>("trim by tag failed.") {

								@Override
								public void onSuccess(List<MapObject> ids) {
									if (ids!=null){
										slider.searchByTag(ids);
									}
								}
							}
							);
				}
			}
		}

	};

	public TagPanel(EditorPanel slider){
		this.slider = slider;

		if (slider instanceof TileSlider){
			type = MapObjectType.TILE;
		} else if (slider instanceof FigureSlider){
			type = MapObjectType.FIGURE;
		} else if (slider instanceof DecalSlider){
			type = MapObjectType.DECAL;
		} else {
			throw new RuntimeException("Invalid slider type");
		}

		this.setStylePrimaryName("tagPanel");

		this.setHorizontalAlignment(ALIGN_CENTER);

		add(new Label("Tag Search (seperated with commas)"));
		input = new TextBox();
		ClientUtil.disableKbOnFocus(input);
		input.addKeyPressHandler(this);

		add(input);


	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		if (event.getUnicodeCharCode()==0)return;
		if (input.getValue().equals(""))slider.resetList();
		changedAt = new Date();
		timer.cancel();
		timer.schedule(400);
	}
}
