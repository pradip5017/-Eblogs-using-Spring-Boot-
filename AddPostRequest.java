package com.eblog.demo.controller.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public class AddPostRequest {
  private  String title;
  private  String content;
  private List<MultipartFile> postImage;
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
public List<MultipartFile> getPostImage() {
	return postImage;
}
public void setPostImage(List<MultipartFile> postImage) {
	this.postImage = postImage;
}
public AddPostRequest() {
	super();
	// TODO Auto-generated constructor stub
}
public AddPostRequest(String title, String content, List<MultipartFile> postImage) {
	super();
	this.title = title;
	this.content = content;
	this.postImage = postImage;
}
  
  
  
}
