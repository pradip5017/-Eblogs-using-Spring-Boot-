package com.eblog.demo.controller.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class UpdatePostRequest {
  private long id;
  private  String title;
  private  String content;
  private List<MultipartFile> image;
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
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
public List<MultipartFile> getImage() {
	return image;
}
public void setImage(List<MultipartFile> image) {
	this.image = image;
}
public UpdatePostRequest() {
	super();
	// TODO Auto-generated constructor stub
}
public UpdatePostRequest(long id, String title, String content, List<MultipartFile> image) {
	super();
	this.id = id;
	this.title = title;
	this.content = content;
	this.image = image;
}
  
  
}
