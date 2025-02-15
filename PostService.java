package com.eblog.demo.service;

import com.eblog.demo.chatgpt.model.request.ChatRequest;
import com.eblog.demo.chatgpt.model.response.ChatGPTResponse;
import com.eblog.demo.chatgpt.service.OpenAIClientService;
import com.eblog.demo.controller.request.AddPostRequest;
import com.eblog.demo.controller.request.DeletePostRequest;
import com.eblog.demo.controller.request.UpdatePostRequest;
import com.eblog.demo.controller.request.VerifyPostRequest;
import com.eblog.demo.controller.response.AddPostResponse;
import com.eblog.demo.controller.response.DeletePostResponse;
import com.eblog.demo.controller.response.UpdatePostResponse;
import com.eblog.demo.controller.response.VerifyPostResponse;
import com.eblog.demo.domain.PostRepository;
import com.eblog.demo.domain.UserRepository;
import com.eblog.demo.entity.Post;
import com.eblog.demo.entity.User;
import com.eblog.demo.utils.FileUploadUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PostService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private OpenAIClientService openAIClientService;

  public List<Post> retrieveAllPosts(String email){
    User user = userRepository.findByEmail(email);
    return postRepository.findAllByUser(user);
  };
                                               //  data        va
  public AddPostResponse addPost(AddPostRequest request, String  email) throws IOException {
   
	  User user = userRepository.findByEmail(email);
   
	  Post post = new Post(request.getTitle(), request.getContent(), user);
    
	  File saveFile  = new ClassPathResource("static/img").getFile();

    List<String> fileNames = new ArrayList<>();

    for (int i=0; i<request.getPostImage().size();i++)
    {
      fileNames.add(request.getPostImage().get(i).getOriginalFilename());
    }
    post.setImages(fileNames);
    Post savedPost = postRepository.save(post);
    String uploadDir = "post-photos/" + savedPost.getId();
    Path path = Paths.get(saveFile.getAbsolutePath()).resolve(uploadDir);
    for (int i=0; i<request.getPostImage().size();i++){
      
    	String fileName = StringUtils.cleanPath(request.getPostImage().get(i).getOriginalFilename());
      
    	FileUploadUtil.saveFile(path.toString(), fileName, request.getPostImage().get(i));
    }
    if(post !=null)
    {
        return new AddPostResponse("success","post added successfully",post);
    }
        return new AddPostResponse("error","failed to add post",null);
  }

  public DeletePostResponse deletePost(DeletePostRequest request)
  {
    postRepository.deleteById(request.getId());
      return new DeletePostResponse("success","post added successfully",null);
  }
  
  

  public UpdatePostResponse updatePost(UpdatePostRequest request, String email) throws IOException 
  {
    Optional<Post> optional = postRepository.findById(request.getId());
    if(optional.isEmpty()){
      return new UpdatePostResponse("error","failed to find post",null);
    }
    Post post = optional.get();
    post.setTitle(request.getTitle());
    post.setContent(request.getContent());
    File saveFile  = new ClassPathResource("static/img").getFile();
    List<String> fileNames = new ArrayList<>();
    if(request.getImage() !=null){
      for (int i=0; i<request.getImage().size();i++){
        fileNames.add(request.getImage().get(i).getOriginalFilename());
      }
      post.setImages(fileNames);
      Post savedPost = postRepository.save(post);
      String uploadDir = "post-photos/" + savedPost.getId();

      Path path = Paths.get(saveFile.getAbsolutePath()).resolve(uploadDir);
      for (int i=0; i<request.getImage().size();i++){
        String fileName = StringUtils.cleanPath(request.getImage().get(i).getOriginalFilename());
        FileUploadUtil.saveFile(path.toString(), fileName, request.getImage().get(i));
      }
      return new UpdatePostResponse("success","post updated",savedPost);
    }
    Post savedPost = postRepository.save(post);
    return new UpdatePostResponse("success","post updated",savedPost);
  }

  public VerifyPostResponse verifyPost(VerifyPostRequest verifyPostRequest) {
    List<CompletableFuture<ChatGPTResponse>> responseFutures = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      CompletableFuture<ChatGPTResponse> responseFuture = CompletableFuture.supplyAsync(() -> {
        ChatRequest chatRequest = new ChatRequest(verifyPostRequest.getContent() + "\\n\\n Is this above text generated by human or by AI, strictly give answer in one word: ai or human?");
        return openAIClientService.chat(chatRequest);
      });
      responseFutures.add(responseFuture);
    }
    List<String> responses = new ArrayList<>();
    for (CompletableFuture<ChatGPTResponse> responseFuture : responseFutures) {
      try {
        ChatGPTResponse response = responseFuture.get();
        responses.add(response.getChoices().get(0).getMessage().getContent());
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
    System.out.println(responses);
    String maxOccurringWord
        = responses.stream()
        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
        .entrySet()
        .stream()
        .max(Comparator.comparing(Entry::getValue))
        .get()
        .getKey();
    return new VerifyPostResponse("success",maxOccurringWord.toLowerCase());
  }
}
