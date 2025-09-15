package com.example.chat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiagnosticController {

    @GetMapping("/test")
    public String test() {
        return "Server is running! WebSocket endpoint should be available at /chat-websocket";
    }

    @GetMapping("/health")
    public String health() {
        return "OK - Server is healthy";
    }
}