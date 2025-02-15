package com.eblog.demo.chatgpt.model.request;

import java.io.Serializable;
import java.util.List;



public class ChatGPTRequest implements Serializable {

    private String model;
    private List<Message> messages;
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	public ChatGPTRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChatGPTRequest(String model, List<Message> messages) {
		super();
		this.model = model;
		this.messages = messages;
	}
    
    
}
