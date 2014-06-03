package com.mut8ed.battlemap.shared.event;

import com.mut8ed.battlemap.shared.dto.ChatMessage;

public class ChatEvent extends MapEvent {

	private ChatMessage chatMessage;
	
	public ChatEvent(){}

	public ChatEvent(ChatMessage cm) {
		this.chatMessage = cm;
	}

	public ChatMessage getChatMessage() {
		return chatMessage;
	}

	public void setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
	}

	@Override
	public String toString() {
		return "ChatEvent [chatMessage=" + chatMessage + "]";
	}

	
}
