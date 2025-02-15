package com.eblog.demo.service;

import com.eblog.demo.domain.RoleRepository;
import com.eblog.demo.domain.UserRepository;
import com.eblog.demo.dto.UserDto;
import com.eblog.demo.entity.Role;
import com.eblog.demo.entity.User;
import com.eblog.demo.utils.FileUploadUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {

  private UserRepository userRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository,RoleRepository roleRepository,PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User saveUser(UserDto userDto) throws IOException {
	  
	   //session.setAttributE( "user"   ,user)
    User user = new User();
    user.setName(userDto.getFirstName() + " " + userDto.getLastName());
    user.setEmail(userDto.getEmail());
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
   
    
    Role role = roleRepository.findByName("ROLE_ADMIN");
    if(role == null)
    {
      role = checkRoleExist();
    }
    
    
    
    user.setRoles(Arrays.asList(role));  //Class StringUtils. This class contains static methods for performing various operations on Strings
    String fileName = StringUtils.cleanPath(userDto.getProfileImage().getOriginalFilename());
    user.setProfileImage(fileName);
    User savedUser = userRepository.save(user);

    File saveFile  = new ClassPathResource("static/img").getFile();
    String uploadDir = "user-photos/" + savedUser.getId();
    Path path = Paths.get(saveFile.getAbsolutePath()).resolve(uploadDir);

    FileUploadUtil.saveFile(path.toString(), fileName, userDto.getProfileImage());
    return savedUser;
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public List<UserDto> findAllUsers() {
    List<User> users = userRepository.findAll();
    return users.stream().map((user) -> convertEntityToDto(user))
        .collect(Collectors.toList());
  }

  private UserDto convertEntityToDto(User user){
    UserDto userDto = new UserDto();
    String[] name = user.getName().split(" ");
    userDto.setFirstName(name[0]);
    userDto.setLastName(name[1]);
    userDto.setEmail(user.getEmail());
    return userDto;
  }

  private Role checkRoleExist() {
    Role role = new Role();
    role.setName("ROLE_ADMIN");
    return roleRepository.save(role);
  }
}