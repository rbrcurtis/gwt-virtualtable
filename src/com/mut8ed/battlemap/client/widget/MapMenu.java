package com.mut8ed.battlemap.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.Page;
import com.mut8ed.battlemap.client.util.ClientUtil;
import com.mut8ed.battlemap.client.util.Jq;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.client.view.MapListView;
import com.mut8ed.battlemap.client.view.MapView;
import com.mut8ed.battlemap.client.widget.editor.CharacterSlider;
import com.mut8ed.battlemap.client.widget.editor.DecalSlider;
import com.mut8ed.battlemap.client.widget.editor.EditorPanel;
import com.mut8ed.battlemap.client.widget.editor.FigureSlider;
import com.mut8ed.battlemap.client.widget.editor.TileSlider;

public class MapMenu extends HorizontalPanel {

	private static MapMenu instance;
	private HorizontalPanel left = new HorizontalPanel();
	private HorizontalPanel right = new HorizontalPanel();
	private VerticalPanel dropMenu = new VerticalPanel();
	private TextBox mapName;
	private TextBox layerName;
	private TextBox selectedName;
	private CheckBox layerVisible;
	private CheckBox selectedVisible;
	
	private MapMenu(){
		

		getElement().setId("mapMenu");
		
		left.setHeight("100%");
		right.setHeight("100%");
		left.setVerticalAlignment(ALIGN_MIDDLE);
		right.setVerticalAlignment(ALIGN_MIDDLE);
		right.getElement().setAttribute("align", "right");
		add(left);
		add(right);
		
		setupMenu();
		setupLinks();
		add(dropMenu);
		
	}
	
	public static MapMenu getInstance(){
		if (instance==null)instance = new MapMenu();
		return instance;
	}

	private void setupMenu() {
		
		dropMenu.getElement().setId("dropMenu");
		
		Label label = null;
		
		/** DEBUG **/
		CheckBox cb = new CheckBox("debug");
		cb.setValue(BattleMap.debug);
		cb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				BattleMap.debug = event.getValue();				
			}
		});
		dropMenu.add(cb);

		/** RETURN to landing **/
		
		label = new Label("Return to Landing");
		label.setStyleName("menuLink");
		label.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hideDropMenu();
				Page.LANDING.redirect();
			}
		});
		dropMenu.add(label);
		
//		/** INVITE **/
//		
//		final InvitePanel inviter = new InvitePanel();
//
//		
//		label = new Label("Invite");
//		label.setStyleName("menuLink");
//		label.getElement().setId("inviteButton");
//		label.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				inviter.center();
//				inviter.show();
//				inviter.focus();
//			}
//		});
//		dropMenu.add(label);
		
		/** SWITCH MAP **/
		
		label = new Label("Switch Map");
		label.setStyleName("menuLink");
		label.getElement().setId("switchMap");
		label.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hideDropMenu();
//				Page.MAP_LIST.redirect();
				
				final DialogBox dialog = new DialogBox(true){
					@Override
					public boolean onKeyDownPreview(char key, int modifiers) {
						if (key == KeyCodes.KEY_ESCAPE){
							hide();
							return false;
						}

						return true;
					}
				};
				
				dialog.addDomHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						dialog.hide();
					}
				}, ClickEvent.getType());
				
				dialog.setWidget(new MapListView());
				dialog.center();
				
			}
		});
		dropMenu.add(label);
		
		
		

		
		
		
	}

	private void setupLinks() {
		
		Label label = null;
		
		
		/** MENU **/
		
		label = new Label("Menu");
		label.setStyleName("menuLink");
		label.getElement().setId("showMenu");
		label.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				showMenu();
			}
		});
		
		
		left.add(label);
		
		
		/** MAP NAME **/
		
		label = new Label(" Map: ");
		label.setStylePrimaryName("menuLabel");
		DOM.setStyleAttribute(label.getElement(), "display", "inline");
		left.add(label);
		mapName = new TextBox();
		mapName.setStylePrimaryName("menuTextInput");
//		mapName.setValue(mapView.getGameMap().getName());
		ClientUtil.disableKbOnFocus(mapName);
		mapName.addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				GWT.log("mapName changed to "+mapName.getValue());
				BattleMap.eventBus.changeMapName(mapName.getValue(), new MTAsyncCallback<Void>());
			}
			
		});
		left.add(mapName);
		
		/** LAYER NAME **/
		
		label = new Label(" Layer: ");
		label.setStylePrimaryName("menuLabel");
		left.add(label);
		layerName = new TextBox();
		layerName.setStylePrimaryName("menuTextInput");
		ClientUtil.disableKbOnFocus(layerName);
		layerName.addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				GWT.log("layerName changed to "+layerName.getValue());
				MapView m = MapView.getInstance();
				m.getGameMap().setLayerName(m.getCurrentLayerIndex(), layerName.getValue());
			}
			
		});
		left.add(layerName);
		
		/** SELECTED NAME **/
		label = new Label(" Selected: ");
		label.setStylePrimaryName("menuLabel");
		left.add(label);
		selectedName = new TextBox();
		selectedName.setStylePrimaryName("menuTextInput");
		ClientUtil.disableKbOnFocus(selectedName);
		selectedName.setEnabled(false);
		selectedName.addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				GWT.log("selectedName changed to "+selectedName.getValue());
				MapView.getInstance().setSelectedName(selectedName.getValue());
			}
			
		});
		left.add(selectedName);
		
		/** LAYER VISIBILITY **/
		layerVisible = new CheckBox("Layer Visible");
		layerVisible.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				MapView.getInstance().toggleLayerVisible();
			}
			
		});
		left.add(layerVisible);
		
		/** SELECTED VISIBILITY **/
		selectedVisible = new CheckBox("Selected Visible");
		selectedVisible.setEnabled(false);
		selectedVisible.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				MapView.getInstance().toggleSelectedVisible();
			}
			
		});
		left.add(selectedVisible);
		
		
		
		
		/****************************************************************/

//		/** Character panel **/
//		label = new Label("Characters");
//		label.setStyleName("menuLink");
//		
//		label.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				CharacterSlider characterSlider = CharacterSlider.getInstance();
//				characterSlider.setWidthAndHeight();
//				EditorPanel.toggle(characterSlider);
//			}
//		});
//		right.add(label);
		
		/** FIGURE PANEL **/
		label = new Label("Figures");
		label.setStyleName("menuLink");
		
		label.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				FigureSlider figureSlider = FigureSlider.getInstance();
				figureSlider.setWidthAndHeight();
				EditorPanel.toggle(figureSlider);
			}
		});
		right.add(label);

		/** TILE PANEL **/
		label = new Label("Tiles");
		label.setStyleName("menuLink");
		label.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				TileSlider tileSlider = TileSlider.getInstance();
				tileSlider.setWidthAndHeight();
				EditorPanel.toggle(tileSlider);
			}
		});
		
		right.add(label);
		
		/** DECAL PANEL **/
		label = new Label("Decals");
		label.setStyleName("menuLink");
		label.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				DecalSlider decalSlider = DecalSlider.getInstance();
				decalSlider.setWidthAndHeight();
				EditorPanel.toggle(decalSlider);
			}
		});
		
		right.add(label);
	}

	private void showMenu() {
		Jq.toggle(dropMenu.getElement().getId(), "fast");
	}

	public void setMapName(String mapName) {
		this.mapName.setValue(mapName);
	}

	public void setLayerName(String layerName) {
		this.layerName.setText(layerName);
	}

	public void setSelectedName(String name) {
		this.selectedName.setValue(name);
	}
	
	/**
	 * sets the layer visibility checkbox on the menu
	 */
	public void setLayerVisible(boolean visible){
		layerVisible.setValue(visible);
	}
	
	/**
	 * sets the selected objects visibility on the menu
	 * @param visible
	 */
	public void setSelectedVisible(boolean visible){
		selectedVisible.setValue(visible);
	}
	
	public void setSelectedEnabled(boolean enabled){
		if (!enabled){
			selectedName.setValue("");
			selectedVisible.setValue(false);
		}
		selectedName.setEnabled(enabled);
		selectedVisible.setEnabled(enabled);
	}

	public void hideDropMenu() {
		Jq.hide(dropMenu.getElement().getId(), "fast");
	}
	
}
