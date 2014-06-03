package com.mut8ed.battlemap.client.util;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.FocusWidget;


public class ClientUtil {

	public static boolean equalsAny(Object left, Object... equalsAny){
		for (Object right : equalsAny){
			if (left==null && right==null)return true;
			if (left==null && right!=null)continue;
			if (left.getClass().equals(right.getClass()) && left.equals(right)){
				return true;
			}
		}
		return false;
	}

	public enum Test {
		A,
		B
	}
	public static Test thing = Test.A;
	public static void main(String[] args){
		System.out.println(equalsAny(thing,Test.B,"b"));
	}
	
	public static void disableKbOnFocus(FocusWidget input) {
		input.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				MapKeyboardHandler.getInstance().setEnabled(false);
			}
		});
		input.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				MapKeyboardHandler.getInstance().setEnabled(true);
			}
		});
	}
}
