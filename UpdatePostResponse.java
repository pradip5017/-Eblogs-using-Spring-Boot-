package com.eblog.demo.controller.response;

import com.eblog.demo.entity.Post;


public class UpdatePostResponse {
  private String status;
  private  String message;
  private  Post post;	
  
  
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public Post getPost() {
	return post;
}
public void setPost(Post post) {
	this.post = post;
}
public UpdatePostResponse() {
	super();
	// TODO Auto-generated constructor stub
}
public UpdatePostResponse(String status, String message, Post post) {
	super();
	this.status = status;
	this.message = message;
	this.post = post;
}
  
  
}
