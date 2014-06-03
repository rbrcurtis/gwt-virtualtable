package com.mut8ed.battlemap.client.widget;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.util.MapEditorMouseHandler;
import com.mut8ed.battlemap.shared.Defaults;

public class ToolPanel extends VerticalPanel {

	//	private List<Image> buttons = new ArrayList<Image>();
	private static ToolPanel instance = null; 

	public enum ToolType {
		SELECT, ROTATE, MULTI, HELP;

		public boolean equalsAny(ToolType...right){
			for (ToolType that : right){
				if (this.equals(that))return true;
			}
			return false;
		}
	}

	private class ToolDetails {
		public Image image;
		public String msg;

		public ToolDetails(Image image, String msg){
			this.image = image;
			this.msg = msg;
		}
	}

	private Map <ToolType,ToolDetails> buttons = new HashMap<ToolType, ToolDetails>();

	public static ToolType selectedTool = ToolType.SELECT;

	private DialogBox helpDialog;

	private ToolPanel(){

		helpDialog = new DialogBox(true){
			@Override
			public boolean onKeyDownPreview(char key, int modifiers) {
				if (key == KeyCodes.KEY_ESCAPE){
					hide();
				}

				return true;
			}
		};
		//		DOM.setStyleAttribute(dialogBox.getElement(), "backgroundColor", "#ABCDEF");
		DOM.setStyleAttribute(helpDialog.getElement(), "zIndex", "9999999");

		String text = "";
//		text+="H	Character Panel\n";
		text+="F	Figure Panel\n";
		text+="T	Tile Panel\n";
		text+="D	Decal Panel\n";
//		text+="Z	Selected Character Sheet\n";
		text+="S	select tool\n";
		text+="M	multi tool\n";
		text+="R 	rotate tool\n";
		text+="del	delete selected\n";
		text+="X	cut selected\n";
		text+="C	copy selected\n";
		text+="V	paste\n";
		text+="Q 	up level\n";
		text+="A	down level\n";
		text+="L	layer visibility toggle\n";
		text+="O	object visibility toggle\n";
		text+="?	this help dialog\n";


		helpDialog.setHTML("<div align='center'>HELP</div>");
		helpDialog.setWidget(new HTML("<pre>" + text + "</pre>"));

		getElement().setId("toolPanel");
		setVerticalAlignment(ALIGN_BOTTOM);
		setHorizontalAlignment(ALIGN_CENTER);

		String msg = "Scroll to zoom\nDrag to move the map\nOr drag to move a selected DECAL";
		Image button = createButton(ToolType.SELECT, "SELECT - S", Defaults.IMGURL+"/ToolButtons/selectTool.png", msg);
		button.addStyleName("selectedToolButton");
		buttons.put(ToolType.SELECT, new ToolDetails(button, msg));
		add(button);

		msg = "Drag to move the map to rotate the object in the direction of the cursor";
		button = createButton(ToolType.ROTATE, "ROTATE - R", Defaults.IMGURL+"/ToolButtons/rotateTool.png", msg);
		buttons.put(ToolType.ROTATE, new ToolDetails(button, msg));
		add(button);

		msg = "Drag to select or drop multiple items";
		button = createButton(ToolType.MULTI, "MULTI - M", Defaults.IMGURL+"/ToolButtons/multiTool.png", msg);
		buttons.put(ToolType.MULTI, new ToolDetails(button, msg)); 
		add(button);

		final Image helpButton = new Image(Defaults.IMGURL+"/ToolButtons/helpTool.png");
		helpButton.setAltText("HELP - ?");
		helpButton.setTitle("HELP - ?");
		helpButton.setStylePrimaryName("toolButton");
		helpButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {

				showHelp();


				event.stopPropagation();
				event.preventDefault();
			}

		});

		helpButton.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				MapEditorMouseHandler.getInstance().setEnabled(false);
				//				//BattleMap.debug("add object disabled");
			}
		});
		helpButton.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				MapEditorMouseHandler.getInstance().setEnabled(true);
				//				//BattleMap.debug("add object enabled");
			}
		});
		//		buttons.put(ToolType.HELP, new ToolDetails(button, msg)); 
		add(helpButton);

	}


	public void showHelp() {
		helpDialog.center();		
	}

	public void hideHelp(){
		helpDialog.hide();
	}


	public static ToolPanel getInstance(){
		if (instance==null)instance = new ToolPanel();
		return instance;
	}

	private Image createButton(final ToolType toolType, String alt, String imagepath, final String msg) {
		final Image button = new Image(imagepath);
		button.setAltText(alt);
		button.setTitle(alt);
		button.setStylePrimaryName("toolButton");
		button.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				selectButton(toolType);
				event.stopPropagation();
				event.preventDefault();
			}

		});

		button.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				MapEditorMouseHandler.getInstance().setEnabled(false);
				//				//BattleMap.debug("add object disabled");
			}
		});
		button.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				MapEditorMouseHandler.getInstance().setEnabled(true);
				//				//BattleMap.debug("add object enabled");
			}
		});

		return button;
	}

	public void selectButton(final ToolType toolType) {
		for (ToolDetails button : buttons.values()){
			button.image.removeStyleName("selectedToolButton");
		}
		ToolDetails tool = buttons.get(toolType); 
		tool.image.addStyleName("selectedToolButton");
		selectedTool = toolType;
		BattleMap.setMessage(tool.msg);
	}

}
