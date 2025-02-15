package com.eblog.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;


@Entity
@Table(name="posts")
public class Post {

  private static final long serialVersionUID = 1L;

  public Post(String title, String content, User user) {
    this.title = title;
    this.content = content;
    this.user = user;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date date;    //time,date,datetime(timestamp)

  @Column(nullable = true)
  private List<String> images;

  @ManyToOne       //Multiple posts belongs to one user
  @JsonIgnore  //means this user need not to be converted into JSON format
  @JoinColumn(name="user_id", nullable=false)
  private User user;

  @Transient
  public List<String> getImagePaths() {
    if (images == null || id == null) {
      return null;
    }
    List<String> imagePaths = new ArrayList<>();
    for (String image : images) {
      imagePaths.add("/img/post-photos/" + id + "/" + image);
    }
    return imagePaths;
  }

public Long getId() {
	return id;
}

public void setId(Long id) {
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

public Date getDate() {
	return date;
}

public void setDate(Date date) {
	this.date = date;
}

public List<String> getImages() {
	return images;
}

public void setImages(List<String> images) {
	this.images = images;
}

public User getUser() {
	return user;
}

public void setUser(User user) {
	this.user = user;
}

public Post() {
	super();
	// TODO Auto-generated constructor stub
}

public Post(Long id, String title, String content, Date date, List<String> images, User user) {
	super();
	this.id = id;
	this.title = title;
	this.content = content;
	this.date = date;
	this.images = images;
	this.user = user;
}


  
  
}
