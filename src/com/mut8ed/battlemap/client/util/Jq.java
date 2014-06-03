package com.mut8ed.battlemap.client.util;

import com.google.gwt.user.client.Element;

public class Jq {

	public static native void toggle(String id, String speed)/*-{
		$wnd.$('#'+id).toggle(speed);
	}-*/;

	public static native void show(String id, String speed)/*-{
		$wnd.$('#'+id).fadeIn(speed);
	}-*/;

	public static native void hide(String id, String speed)/*-{
		$wnd.$('#'+id).fadeOut(speed);
	}-*/;

	public static native void disableSelect(Element target)/*-{
			if (typeof target.onselectstart!="undefined") //IE route
				target.onselectstart=function(){return false}
			else if (typeof target.style.MozUserSelect!="undefined"){ //Firefox route
				target.style.MozUserSelect="none"
				target.draggable = false;
			} else //All other route (ie: Opera)
				target.onmousedown=function(){return false}
			target.style.cursor = "default"
	}-*/;

	public static native void disableDrag(Element target)/*-{
		target.draggable = false;
		target.onmousedown=function(e) { 
			if(typeof e == 'undefined') e = window.event; 
			if(e.preventDefault) e.preventDefault(); 
		};
	}-*/;

	public static native void move(String id, int x, int y)/*-{
		$wnd.$('#'+id).animate({
			left:x+'px',
			top:y+'px'
		});
	}-*/;

	public static native void log(String string)/*-{
		console.log(string);
	}-*/;

	public static native void error(String string)/*-{
		console.error(string);
	}-*/;


}
