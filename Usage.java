package com.eblog.demo.chatgpt.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;


public class Usage implements Serializable {

    @JsonProperty("prompt_tokens")
    private String promptTokens;

    @JsonProperty("completion_tokens")
    private String completionTokens;

    @JsonProperty("total_tokens")
    private String totalTokens;

	public String getPromptTokens() {
		return promptTokens;
	}

	public void setPromptTokens(String promptTokens) {
		this.promptTokens = promptTokens;
	}

	public String getCompletionTokens() {
		return completionTokens;
	}

	public void setCompletionTokens(String completionTokens) {
		this.completionTokens = completionTokens;
	}

	public String getTotalTokens() {
		return totalTokens;
	}

	public void setTotalTokens(String totalTokens) {
		this.totalTokens = totalTokens;
	}

	public Usage(String promptTokens, String completionTokens, String totalTokens) {
		super();
		this.promptTokens = promptTokens;
		this.completionTokens = completionTokens;
		this.totalTokens = totalTokens;
	}

	public Usage() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
