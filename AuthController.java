package com.eblog.demo.controller;

import com.eblog.demo.dto.UserDto;
import com.eblog.demo.entity.User;
import com.eblog.demo.service.UserService;
import jakarta.validation.Valid;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuthController {

	 @Autowired
     private UserService userService;

  
    
//  @GetMapping("/")
//  public String redirectToIndex() {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    return "redirect:/index";
//  }

  @GetMapping("/index")
  public String home(){
    return "index";   //index => view .html, .jsp
  }

  @GetMapping("/login")
  public String loginForm() {
    return "login";
  }

  @GetMapping("register")
  public String showRegistrationForm(Model model){
    UserDto user = new UserDto();
    model.addAttribute("user", user);
    return "register";
  }

  @PostMapping("/register/save")
  public String registration(@Valid @ModelAttribute("user") UserDto user,BindingResult result,Model model) throws IOException {
  
	        // jpa.findById(id)
	        // jpa.findByEmail(id)
	  
	  //pooja,sharma,poojabbh@,1234
	  
	  
	  
	  User existing = userService.findByEmail(user.getEmail());  //vasi
    
    
        //vais
    if (existing != null) {
      result.rejectValue("email", null, "There is already an account registered with that email");
    }
    if (result.hasErrors()) {
      model.addAttribute("user", user);
      return "register";
    }
    //userREpso.save(user)
    userService.saveUser(user);
    return "redirect:/register?success";
  }
  

  @GetMapping("/users")
  public String listRegisteredUsers(Model model){
    List<UserDto> users = userService.findAllUsers();
    model.addAttribute("users", users);
    return "users";
  }
} 