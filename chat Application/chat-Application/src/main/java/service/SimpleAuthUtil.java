package com.example.chat.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SimpleAuthUtil {

    private final Map<String, UserSession> activeSessions = new ConcurrentHashMap<>();

    public static class UserSession {
        private String username;
        private String email;
        private String fullName;
        private long createdAt;

        public UserSession(String username, String email, String fullName) {
            this.username = username;
            this.email = email;
            this.fullName = fullName;
            this.createdAt = System.currentTimeMillis();
        }

        // Getters
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getFullName() { return fullName; }
        public long getCreatedAt() { return createdAt; }
    }

    public String createSession(String username, String email, String fullName) {
        String sessionId = "session_" + UUID.randomUUID().toString();
        activeSessions.put(sessionId, new UserSession(username, email, fullName));
        return sessionId;
    }

    public UserSession getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }

    public boolean isValidSession(String sessionId) {
        UserSession session = activeSessions.get(sessionId);
        if (session == null) return false;

        // Session valid for 24 hours
        long twentyFourHours = 24 * 60 * 60 * 1000L;
        return (System.currentTimeMillis() - session.getCreatedAt()) < twentyFourHours;
    }

    public void removeSession(String sessionId) {
        activeSessions.remove(sessionId);
    }

    public String getUsernameFromSession(String sessionId) {
        UserSession session = getSession(sessionId);
        return session != null ? session.getUsername() : null;
    }
}