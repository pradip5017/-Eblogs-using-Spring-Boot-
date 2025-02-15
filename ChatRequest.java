package com.eblog.demo.chatgpt.model.request;

import java.io.Serializable;


public class ChatRequest implements Serializable {
    private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ChatRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChatRequest(String content) {
		super();
		this.content = content;
	}
    
    
}
