package com.library.library_backend.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
