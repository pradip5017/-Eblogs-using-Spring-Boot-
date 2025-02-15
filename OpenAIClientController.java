package com.eblog.demo.chatgpt.controller;

import com.eblog.demo.chatgpt.model.request.ChatRequest;   
import com.eblog.demo.chatgpt.model.response.ChatGPTResponse;
import com.eblog.demo.chatgpt.service.OpenAIClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping(value = "/chatgpt")
public class OpenAIClientController {

	@Autowired
    private  OpenAIClientService openAIClientService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChatGPTResponse chat(@RequestBody ChatRequest chatRequest){
        return openAIClientService.chat(chatRequest);
    }

}
