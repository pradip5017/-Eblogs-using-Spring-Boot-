package com.eblog.demo.chatgpt.service;

import com.eblog.demo.chatgpt.model.request.ChatGPTRequest;
import com.eblog.demo.chatgpt.model.request.ChatRequest;
import com.eblog.demo.chatgpt.model.request.Message;
import com.eblog.demo.chatgpt.model.response.ChatGPTResponse;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIClientService {

    private final static String ROLE_USER = "user";

    @Value("${openai-service.gpt-model}")
    private String model;

    @Value("${openai-service.api-key}")
    private String apiKey;

    @Value("${openai-service.urls.base-url}")
    private String baseURL;

    @Value("${openai-service.urls.chat-url}")
    private String chatURL;

    public ChatGPTResponse chat(ChatRequest chatRequest){
       /* Message message = Message.builder()
                .role(ROLE_USER)
                .content(chatRequest.getContent())
                .build();*/
        Message message=new Message();
        message.setRole(ROLE_USER);
        message.setContent(chatRequest.getContent());
      /*  ChatGPTRequest chatGPTRequest = ChatGPTRequest.builder()
                .model(model)
                .messages(Collections.singletonList(message))
                .build();*/
         ChatGPTRequest  chatGPTRequest =new ChatGPTRequest();
         chatGPTRequest.setModel(model);
         chatGPTRequest.setMessages(Collections.singletonList(message));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);

        HttpEntity<ChatGPTRequest> requestEntity = new HttpEntity<>(chatGPTRequest, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ChatGPTResponse> responseEntity = restTemplate.postForEntity(baseURL+chatURL, requestEntity, ChatGPTResponse.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException("Failed to make the API request. Status code: " + responseEntity.getStatusCodeValue());
        }
    }

}
