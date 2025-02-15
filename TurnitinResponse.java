package com.eblog.demo.chatgpt.model.response;

import java.util.Map;

public class TurnitinResponse {
  private String status;
  private Map<String, Double> urlPercentMap;
  private String plagiarismPercentage;
  private String message;

  public TurnitinResponse(String status) {
    this.status = status;
  }

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public Map<String, Double> getUrlPercentMap() {
	return urlPercentMap;
}

public void setUrlPercentMap(Map<String, Double> urlPercentMap) {
	this.urlPercentMap = urlPercentMap;
}

public String getPlagiarismPercentage() {
	return plagiarismPercentage;
}

public void setPlagiarismPercentage(String plagiarismPercentage) {
	this.plagiarismPercentage = plagiarismPercentage;
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}
  
  
}
