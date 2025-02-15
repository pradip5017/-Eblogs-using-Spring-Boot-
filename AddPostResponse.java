package com.eblog.demo.controller.response;

import com.eblog.demo.entity.Post;



public class AddPostResponse {
  private String status;
  private String message;
  private Post post;
public String getStatus() {
	return status;
}
public String getMessage() {
	return message;
}
public Post getPost() {
	return post;
}
public AddPostResponse() {
	super();
	// TODO Auto-generated constructor stub
}
public AddPostResponse(String status, String message, Post post) {
	super();
	this.status = status;
	this.message = message;
	this.post = post;
}
  
  
}
