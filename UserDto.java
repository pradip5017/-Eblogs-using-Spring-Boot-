package com.eblog.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;


//DAO=> Data Access Object => database related opeartions

public class UserDto {    //DTO => Data Transfer Object Design Pattern is a frequently used design pattern. It is basically used to pass data with multiple attributes in one shot from client to server, to avoid multiple calls to a remote server.
  private Long id;

  @NotEmpty   //default msg => must not be empty
  private String firstName;

  @NotEmpty
  private String lastName;
  
  @NotEmpty(message = "Email should not be empty")
  @Email
  private String email;   //

  @NotEmpty(message = "Password should not be empty")
  @Length(min = 5,message = "password must be greater than 5 chars")
  private String password;

  private MultipartFile profileImage;

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getFirstName() {
	return firstName;
}

public void setFirstName(String firstName) {
	this.firstName = firstName;
}

public String getLastName() {
	return lastName;
}

public void setLastName(String lastName) {
	this.lastName = lastName;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public MultipartFile getProfileImage() {
	return profileImage;
}

public void setProfileImage(MultipartFile profileImage) {
	this.profileImage = profileImage;
}

public UserDto(Long id, @NotEmpty String firstName, @NotEmpty String lastName,
		@NotEmpty(message = "Email should not be empty") @Email String email,
		@NotEmpty(message = "Password should not be empty") String password, MultipartFile profileImage) {
	super();
	this.id = id;
	this.firstName = firstName;
	this.lastName = lastName;
	this.email = email;
	this.password = password;
	this.profileImage = profileImage;
}

public UserDto() {
	super();
	// TODO Auto-generated constructor stub
}
  
  
}