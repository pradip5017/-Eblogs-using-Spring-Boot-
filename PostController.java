package com.eblog.demo.controller;

import com.eblog.demo.chatgpt.model.response.PlagReport;
import com.eblog.demo.chatgpt.model.response.TurnitinResponse;
import com.eblog.demo.chatgpt.service.Turnitin;
import com.eblog.demo.controller.request.AddPostRequest;
import com.eblog.demo.controller.request.DeletePostRequest;
import com.eblog.demo.controller.request.UpdatePostRequest;
import com.eblog.demo.controller.request.VerifyPostRequest;
import com.eblog.demo.controller.response.AddPostResponse;
import com.eblog.demo.controller.response.DeletePostResponse;
import com.eblog.demo.controller.response.UpdatePostResponse;
import com.eblog.demo.controller.response.VerifyPostResponse;
import com.eblog.demo.entity.Post;
import com.eblog.demo.service.PostService;
import com.eblog.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PostController {

  private PostService postService;
  private UserService userService;
  private Turnitin turnitin;

  @Autowired
  public PostController(PostService postService, UserService userService, Turnitin turnitin) {
    this.postService = postService;
    this.userService = userService;
    this.turnitin = turnitin;
  }

  @GetMapping("/dashboard")
  public ModelAndView dashboard(HttpSession session){
    ModelAndView modelAndView = new ModelAndView();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
    if (!authentication.isAuthenticated())  //if username or password is wrong
    	 modelAndView.setViewName("redirect:/login");
    
    
    String email = authentication.getName();
    session.setAttribute("user",userService.findByEmail(email));
   
    
    
    
    List<Post> blogs = postService.retrieveAllPosts(email);  //findAll
    modelAndView.addObject("blogs", blogs);
    modelAndView.setViewName("dashboard");
    return modelAndView;
  }

  @PostMapping(value = "/add-post", consumes = { "multipart/form-data" })  //
  public ResponseEntity<Object> addModule(@ModelAttribute AddPostRequest addModuleRequest) throws IOException {
    
	  
	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
	  if (addModuleRequest == null || Strings.isBlank(addModuleRequest.getTitle()) || Strings.isBlank(addModuleRequest.getContent())) {
    
    	return ResponseEntity.badRequest().body(new AddPostResponse("error", "invalid input",null));
    }
                                                              //ttiand con             vais@ga
    AddPostResponse addPostResponse = postService.addPost(addModuleRequest,authentication.getName());
    
    if (addPostResponse.getStatus().equalsIgnoreCase("error")) return ResponseEntity.badRequest().body(addPostResponse);
    
    return ResponseEntity.ok(addPostResponse);
  }

  @DeleteMapping(value = "/delete-post", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> deleteModule(@RequestBody DeletePostRequest deletePostRequest)
  {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
    if (deletePostRequest == null) {
      return ResponseEntity.badRequest().body(new DeletePostResponse("error", "invalid input", null));
    }
    DeletePostResponse deletePostResponse = postService.deletePost(deletePostRequest);

    if (deletePostResponse != null && Objects.equals(deletePostResponse.getStatus(), "error")) 
    	return ResponseEntity.badRequest().body(deletePostResponse);
    return ResponseEntity.ok().body(deletePostResponse);
  }

  @PatchMapping(value = "/update-post", consumes = { "multipart/form-data" })
  public ResponseEntity<Object> updateModule(@ModelAttribute UpdatePostRequest updatePostRequest) throws IOException 
  {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (updatePostRequest == null || Strings.isBlank(updatePostRequest.getTitle()) || Strings.isBlank(updatePostRequest.getContent())) {
      return ResponseEntity.badRequest().body(new UpdatePostResponse("error", "invalid input", null));
    }
    UpdatePostResponse updatePostResponse = postService.updatePost(updatePostRequest,authentication.getName());
    if (updatePostResponse.getStatus().equalsIgnoreCase("error")) return ResponseEntity.badRequest().body(updatePostResponse);
    return ResponseEntity.ok(updatePostResponse);
  }

  
  
  
  @PostMapping("/content-verify")
  @ResponseBody                 //id name pass               User    u
  public ModelAndView contentVerify(@RequestBody VerifyPostRequest verifyPostRequest) {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("title", verifyPostRequest.getTitle());
    modelAndView.addObject("content", verifyPostRequest.getContent());
    modelAndView.setViewName("content-verify");
    return modelAndView;
  }
  
  

  @PostMapping(value = "/verify-post", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> verifyModule(@RequestBody VerifyPostRequest verifyPostRequest) throws IOException
  {
	   //title,Content
    if (verifyPostRequest == null || Strings.isBlank(verifyPostRequest.getTitle()) || Strings.isBlank(verifyPostRequest.getContent())) {
      return ResponseEntity.badRequest().body(new UpdatePostResponse("error", "invalid input", null));
    }
//    VerifyPostResponse response = postService.verifyPost(verifyPostRequest);
    TurnitinResponse response = turnitin.checkPlagiarism(verifyPostRequest.getContent());
    if (response.getStatus().equalsIgnoreCase("error")) return ResponseEntity.badRequest().body(response);
    return ResponseEntity.ok(response);
  }
   


}
