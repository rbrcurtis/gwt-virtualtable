package com.mut8ed.battlemap.client.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.util.Jq;
import com.mut8ed.battlemap.client.widget.editor.DecalSlider;
import com.mut8ed.battlemap.client.widget.editor.EditorPanel;
import com.mut8ed.battlemap.client.widget.editor.FigureSlider;
import com.mut8ed.battlemap.client.widget.editor.TileSlider;
import com.mut8ed.battlemap.shared.MapObjectType;
import com.mut8ed.battlemap.shared.dto.Decal;
import com.mut8ed.battlemap.shared.dto.MapObject;
import com.mut8ed.battlemap.shared.dto.Tile;

public class AddMapObjectView extends VerticalPanel {

	MapObjectType selectedType = null;
	FormPanel form;
	private VerticalPanel formBody;
	private static AddMapObjectView instance;
	private HorizontalPanel radioPanel;
	private boolean returnToMap = false;

	public static AddMapObjectView getInstance(){
		if (instance==null){
			instance = new AddMapObjectView(null);
		}
		return instance;
	}

	private AddMapObjectView(String nul){

		getElement().setId("AddMapObjectView");

		form = new FormPanel();
		form.getElement().setId("ObjectUploadForm");
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.setAction(GWT.getModuleBaseURL()+"mapObjectUploadServlet");

		VerticalPanel formBodyWrapper = new VerticalPanel();
		formBodyWrapper.getElement().setId("formBodyWrapper");
		ClickHandler handler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RadioButton rb = (RadioButton)event.getSource();
				setRadioSelection(MapObjectType.valueOf(rb.getText()
				));
			}
		};
		radioPanel = new HorizontalPanel();
		radioPanel.getElement().setId("radioHeader");
		for (MapObjectType type : MapObjectType.values()){
			RadioButton rb = new RadioButton("object-type",type.name());
			rb.setFormValue(type.name());
			rb.addClickHandler(handler);
			radioPanel.add(rb);
		}
		formBody = new VerticalPanel();
		formBody.getElement().setId("formBody");
		form.setWidget(formBodyWrapper);
		formBodyWrapper.add(radioPanel);
		formBodyWrapper.add(formBody);
		formBodyWrapper.add( new Button("Submit", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				form.submit();
			}
		}));

		// Add an event handler to the form.
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String text = event.getResults();
				if (!text.toLowerCase().contains("success")){
					DialogBox dialogBox = new DialogBox(true);
					DOM.setStyleAttribute(dialogBox.getElement(), "backgroundColor", "#ABCDEF");
					GWT.log("map object upload returned "+text);
					text = text.replaceAll(" ", "&nbsp;");
					dialogBox.setHTML(text);
					dialogBox.center();	
				}
				if (text.toLowerCase().contains("success") && selectedType!=null && returnToMap){
					GWT.log("returning to game map and reloading images");
					Jq.hide(instance.getElement().getId(), "slow");
					MapView mapView = MapView.getInstance();
					Jq.show(mapView.getElement().getId(), "slow");
//					mapView.reloadMap();
					//TODO make the editor panel only reload the edited image, not all.
					switch (selectedType){
					case TILE:
						TileSlider.getInstance().load();
						break;
					case DECAL:
						DecalSlider.getInstance().load();
						break;
					case FIGURE:
						FigureSlider.getInstance().load();
						break;
					}

				}

				EditorPanel.setSelected(null, null, null);
				selectedType = null;
				formBody.clear();

			}
		});

		add(new HTML("<br/>"));
		add(new HTML("<br/>"));
		add(form);


	}


	private void setRadioSelection(MapObjectType selection) {
		if (selection==null)return;
		selectedType = selection;
		GWT.log(selectedType.name());
		formBody.clear();

		switch (selectedType){
		case TILE:
			buildTileAndDecalUploadForm(null,null,1,1);
			break;
		case DECAL:
			buildTileAndDecalUploadForm(null,null,1,1);
			break;
		default:
			formBody.add(new Label("This object type hasn't been coded for yet."));
			break;
		}
	}


	private void buildTileAndDecalUploadForm(String id, List<String> tags, Integer width, Integer height) {

		formBody.clear();

		FlexTable table = new FlexTable();
		Label label = new Label("Image File");
		FileUpload fu = new FileUpload();
		fu.setName("objectImage");
		table.setWidget(0, 0, label);
		table.setWidget(0, 1, fu);

		Label catL = new Label("Category/Set Name");
		TextBox cat = new TextBox();
		cat.setName("category");
		table.setWidget(1, 0, catL);
		table.setWidget(1, 1, cat);


		TextBox tagBox = new TextBox();
		tagBox.setName("tags");
		table.setWidget(2, 0, new Label("Tags, comma delimited"));
		table.setWidget(2, 1, tagBox);
		String tagStr = "";
		if (tags!=null && tags.size()>0){
			for (String tag : tags){
				if (tag.startsWith("c:")){
					cat.setValue(tag.substring(tag.indexOf(":")+1));
				} else {
					tagStr+=tag+",";
				}
			}
			tagStr = tagStr.substring(0,tagStr.length()-1);
			tagBox.setValue(tagStr);
		}

		table.setWidget(3, 0, new Label("Default Width x Height"));
		table.setWidget(3, 1, new HTML(
				"<input type=\"text\" name=\"width\" value=\""+width+"\" class=\"smallInput\"/>" +
				"x" +
				"<input type=\"text\" name=\"height\" value=\""+height+"\" class=\"smallInput\"/>"
		));
		formBody.add(table);
		if (id!=null){
			Hidden hidden = new Hidden("object-id", ""+id);
			hidden.getElement().setId("object-id");
			formBody.add(hidden);
			this.returnToMap  = true;
		}

	}

	public void setSelectedObject(MapObject mo) {
		GWT.log("add map object view setSelected called with MO of "+mo.getElementId());

		this.selectedType = mo.getMapObjectType();

		for (int i=0;i<radioPanel.getWidgetCount();i++){
			RadioButton rb = (RadioButton)radioPanel.getWidget(i);
			if (rb.getFormValue().equals(mo.getMapObjectType().name())){
				rb.setValue(true);
				break;
			}
		}

		switch (mo.getMapObjectType()){
		case TILE:
			Tile tile = (Tile)mo;
			buildTileAndDecalUploadForm(tile.getId(),tile.getTags(),tile.getWidth(),tile.getHeight());
			break;
		case DECAL:
			Decal decal = (Decal)mo;
			buildTileAndDecalUploadForm(decal.getId(),decal.getTags(),decal.getWidth(),decal.getHeight());
			break;
		default: GWT.log("cant handle right-click edit of "+mo.getMapObjectType(), new Throwable());
		}
	}






}
