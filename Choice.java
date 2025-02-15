package com.eblog.demo.chatgpt.model.response;

import com.eblog.demo.chatgpt.model.request.Message;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;



public class Choice implements Serializable {
    private Integer index;
    private Message message;
    @JsonProperty("finish_reason")
    private String finishReason;
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public String getFinishReason() {
		return finishReason;
	}
	public void setFinishReason(String finishReason) {
		this.finishReason = finishReason;
	}
	public Choice() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Choice(Integer index, Message message, String finishReason) {
		super();
		this.index = index;
		this.message = message;
		this.finishReason = finishReason;
	}
    
    
}
