package com.eblog.demo.controller;

import com.eblog.demo.domain.UserRepository;
import com.eblog.demo.entity.User;
import com.eblog.demo.service.EmailService;
import jakarta.servlet.http.HttpSession;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/forgot-password")
public class ForgetPasswordController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailService emailService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping
  public ModelAndView forgetPassword(){
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("forgot-password");
    return modelAndView;
  }

  @PostMapping("/send-otp")
  public String sendOTP(@RequestParam("email") String email, HttpSession session, Model model){
    User user = userRepository.findByEmail(email);
    
    
    
    
    if(user !=null){
    	//otp=873282
      int otp = emailService.generateOTP(6);
      
      
      
      
      
      
      String subject = "Password Reset OTP";
      String message = """
        <div style="font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2">
          <div style="margin:20px auto;width:70%;padding:20px 0">
            <div style="border-bottom:1px solid #eee">
              <a href="" style="font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600">E Blogs</a>
            </div>
            <p style="font-size:1.1em">Hi,</p>
            <p>It looks like you just requested to reset your account password. Below is your OTP required to confirm your password reset request</p>
            <h2 style="background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;letter-spacing:0.4rem;">
            """
            +otp+
            """
            </h2>
            <p style="font-size:0.9em;">Regards,<br>E Blogs</p>
            <hr style="border:none;border-top:1px solid #eee" />
          </div>
        </div>
        """;
      boolean isSent = emailService.sendMail(email, subject, message);  //isSent=true
      session.setAttribute("otp",otp);
      session.setAttribute("email",user.getEmail());
      if(isSent){
        model.addAttribute("message_success", "We have sent OTP to your email.");
      }else{
        model.addAttribute("message_error", "Failed to sent OTP to your email, please try again");
      }
      return "verify-otp";
    }

    model.addAttribute("message_error", "User not found");
    return "forgot-password";
  }

  @PostMapping("/verify-otp")  //    873282 873282
  public String verify(@RequestParam("otp") int otp, HttpSession session, Model model){
    int assignedOTP  = (int) session.getAttribute("otp");
       ///873282        873282
    if(assignedOTP == otp){
      model.addAttribute("message_success", "OTP verified successfully.");
      return "reset-password";
    }else{
      model.addAttribute("message_error", "Failed to verify OTP, please try again");
    }
    return "verify-otp";
  }

  @PostMapping("/reset-password")
  public String resetPassword(@RequestParam("password") String password, HttpSession session, Model model){
      String email = (String)session.getAttribute("email");
      User user = userRepository.findByEmail(email);
      user.setPassword(passwordEncoder.encode(password));
      userRepository.save(user);
      return "redirect:/login?change=Password changed successfully";
  }
}
