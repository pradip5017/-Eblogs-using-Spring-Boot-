package com.eblog.demo.controller.request;


public class VerifyPostRequest {
  private String title;
  private String content;
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public VerifyPostRequest(String title, String content) {
	super();
	this.title = title;
	this.content = content;
}
public VerifyPostRequest() {
	super();
	// TODO Auto-generated constructor stub
}
  
  

}
