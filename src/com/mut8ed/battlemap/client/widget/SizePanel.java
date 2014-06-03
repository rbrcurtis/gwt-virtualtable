package com.mut8ed.battlemap.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.mut8ed.battlemap.client.util.ClientUtil;
import com.mut8ed.battlemap.shared.dto.Dimensions;

public class SizePanel extends HorizontalPanel {

	private TextBox width;
	private TextBox height;
	private Dimensions dimensions = new Dimensions(1,1,1);

	public SizePanel(){

		
		this.setStylePrimaryName("sizePanel");

		this.setVerticalAlignment(ALIGN_BOTTOM);
		this.setHorizontalAlignment(ALIGN_CENTER);

		width = new TextBox();
		ClientUtil.disableKbOnFocus(width);
		width.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				GWT.log("width changed to "+width.getValue());
				if (!width.getValue().equals("") && !width.getValue().matches("[0-9]+")){
					String val = width.getValue();
					width.setValue(val.replaceAll("[^0-9]",""));
				}
				if (!width.getValue().equals("")){
					int val = Integer.parseInt(width.getValue());
					width.setValue(""+val);
					dimensions.setWidth(val);
				}				
			}
		});
		width.setStylePrimaryName("sizeField");
		add(width);

		add(new Label(" X "));
		height = new TextBox();
		ClientUtil.disableKbOnFocus(height);
		height.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				GWT.log("key down: height changed to "+height.getValue());
				if (!height.getValue().equals("") && !height.getValue().matches("[0-9]+")){
					String val = height.getValue();
					height.setValue(val.replaceAll("[^0-9]",""));
				}
				if (!height.getValue().equals("")){
					int val = Integer.parseInt(height.getValue());
					height.setValue(""+val);
					dimensions.setHeight(val);
				}				
			}
		});
		add(height);
		height.setStylePrimaryName("sizeField");

	}

	public Dimensions getDimensions(){
		return dimensions;
	}

	public void setDisplayedWidth(int width){
		this.width.setValue(""+width);
		dimensions.setWidth(width);
	}

	public void setDisplayedHeight(int height){
		this.height.setValue(""+height);
		dimensions.setHeight(height);
	}
}
