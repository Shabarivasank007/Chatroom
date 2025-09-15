package com.example.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.Map;

@Controller
public class ChatController {

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public Map<String, String> sendMessage(Map<String, String> message) throws Exception {
        return Map.of(
                "username", message.getOrDefault("username", "Anonymous"),
                "text", message.getOrDefault("text", "")
        );
    }
}
